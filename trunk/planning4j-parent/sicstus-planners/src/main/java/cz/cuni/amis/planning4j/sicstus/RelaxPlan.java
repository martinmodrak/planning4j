/*
 * Copyright (C) 2012 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.amis.planning4j.sicstus;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.PlanningResult;
import cz.cuni.amis.planning4j.pddl.*;
import cz.cuni.amis.planning4j.utils.PlanningUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import se.sics.jasper.Query;
import se.sics.jasper.SPException;
import se.sics.jasper.Term;

/**
 * Relaxplan planner. This class is not thread safe, only single planner
 * execution should run at a time.
 *
 * @author Martin Cerny
 */
public class RelaxPlan extends AbstractSicstusPlanner {

    private final Logger logger = Logger.getLogger(RelaxPlan.class);
    
    private long timeout;

    boolean debug;
    
    public static final long DEFAULT_TIMEOUT = 300000;
    
    public RelaxPlan(){
        this(DEFAULT_TIMEOUT);
    }            
    
    public RelaxPlan(long timeout){
        this(timeout, false);
    }
    
    public RelaxPlan(long timeout, boolean debug) {
        super(AbstractSicstusPlanner.class.getResourceAsStream("relaxplan.sav"));
        this.timeout = timeout;
        this. debug = debug;
    }
    
    List<String> indexedActions; //0-based
    List<String> indexedAtoms; //0-based
    Map<String, Integer> atomsToIndex; //1-based as needed by the prolog program
    Map<String, Integer> actionsToIndex; //1-based as needed by the prolog program

    protected static final String PLAN_VAR_NAME = "Plan";
    
    @Override
    protected Query preparePrologQuery(IDomainProvider domainProvider, IProblemProvider problemProvider, Map variableMap) {
        try {
            File problemFile = writePrologProblemDefinition(domainProvider, problemProvider);
            prolog.query("['" + problemFile.getAbsolutePath().replace("\\","/") + "'].", null);
            return prolog.openPrologQuery("plan("+ PLAN_VAR_NAME + "," + timeout + ").", variableMap);
        } catch (IOException ex) {
            throw new PlanningIOException(ex);
        } catch (SPException ex) {
            throw new PlanningException("Prolog error", ex);
        } catch (Exception ex){
            throw new PlanningException("Unrecognized error", ex);            
        }

    }

    @Override
    protected IPlanningResult parseResultFromBoundVariables(Map variableMap, String plannerOutput) {
        try {
            Term[] planTerms = ((Term)variableMap.get(PLAN_VAR_NAME)).toPrologTermArray();
            List<ActionDescription> actions = new ArrayList<ActionDescription>(planTerms.length);
            
            int time = 0;
            for(Term planElement : planTerms){
                String actionName = PlanningUtils.normalizeIdentifier(indexedActions.get(((int)planElement.getInteger()) - 1));
                
                ActionDescription actionDescription = new ActionDescription();
                actionDescription.setName(actionName);
                actionDescription.setStartTime(time);
                actionDescription.setDuration(1);
                
                actions.add(actionDescription);
                time++;
            }
            
            PlanningResult result = new PlanningResult(true, actions, new PlanningStatistics());
            return result;
        } catch (Exception ex) {
            throw new PlanningException("Prolog error.", ex);
        }
        
    }

    
    
    /**
     * Excerpt from the relaxPlan docs:
     * <p>
     * %% vsechny predikaty  ocislovany od 1 a podobne  ocislovany vsechny akce od 1 <br>
     * %% - d(pocetPredikatu,pocetAkci). <br>
     * %% - predikaty platne v pocatecnim stavu budou znaceny i(cislo).<br>
     * %% - predikaty pozadovane v cili budou znacene g(cislo).<br>
     * %% - predpoklady akce budeme psat p(cisloAkce,cisloPredikatu).<br>
     * %% - efekty akce budeme psat e(cisloAkce,cisloPredikatu).<br>
     * %% - cenu akce budeme znacit c(cisloAkce,cena).<br>
     * </p>
     * @param domainProvider
     * @param problemProvider
     * @throws PlanningException
     * @throws IOException 
     */
    protected File writePrologProblemDefinition(IDomainProvider domainProvider, IProblemProvider problemProvider) throws PlanningException, IOException {
        File problemDefinitionTempFile = File.createTempFile("relaxPlan_problem_", ".pl");
        problemDefinitionTempFile.deleteOnExit();
        Writer problemWriter = new FileWriter(problemDefinitionTempFile);

        PDDLDomain domain = domainProvider.getDomainAsPDDL();
        if (domain == null) {
            throw new PlanningException("Given domain does not support getDomainAsPDDL()");
        }

        PDDLProblem problem = problemProvider.getProblemAsPDDL();
        if (problem == null) {
            throw new PlanningException("Given problem does not support getProblemAsPDDL()");
        }

        indexedAtoms = new ArrayList<String>(domain.getPredicates().size());
        atomsToIndex = new HashMap<String, Integer>(domain.getPredicates().size());                        
        
        //translate predicates
        for (PDDLPredicate predicate : domain.getPredicates()) {
            if (!predicate.getParameters().isEmpty()) {
                throw new PlanningException("Only ground predicates and actions are supported");
            }
            indexedAtoms.add(predicate.getName());
            int currentAtomIndex = indexedAtoms.size(); //1-based
            atomsToIndex.put(predicate.getName(), currentAtomIndex);
        }

        indexedActions = new ArrayList<String>(domain.getActions().size());
        actionsToIndex = new HashMap<String, Integer>(domain.getActions().size());
        
        //translate actions
        for (PDDLAction action : domain.getActions()) {
            
            
            if (!action.getParameters().isEmpty()) {
                throw new PlanningException("Only ground predicates and actions are supported");
            }

            if (!(action instanceof PDDLSimpleAction)) {
                throw new PlanningException("Only PDDLSimpleActions are currently supported");
            }
            
            PDDLSimpleAction simpleAction = (PDDLSimpleAction) action;

            if (!simpleAction.getNegativeEffects().isEmpty()) {
                throw new PlanningException("Negative effects are not supported");
            }

            indexedActions.add(action.getName());
            int currentActionIndex = indexedActions.size(); //1-based
            actionsToIndex.put(action.getName(), currentActionIndex);           

            for (String precondition : simpleAction.getPreconditionList()) {
                Integer preconditionIndex = atomsToIndex.get(precondition);
                if(preconditionIndex == null){
                    throw new PlanningException("Precondition " + precondition + " for action "+ action.getName() +" is not defined.");
                }
                problemWriter.write("p(" + currentActionIndex + "," + preconditionIndex + ").");
                if(debug){
                    problemWriter.write("%% action: " + action.getName() + " prec: " + precondition);
                }
                problemWriter.write("\n");
            }
            for (String effect : simpleAction.getPositiveEffects()) {
                Integer effectIndex = atomsToIndex.get(effect);
                if(effectIndex == null){
                    throw new PlanningException("Effect " + effect + " for action "+ action.getName() +" is not defined.");
                }
                problemWriter.write("e(" + currentActionIndex + "," + effectIndex + ").");
                if(debug){
                    problemWriter.write("%% action: " + action.getName() + " effect: " + effect);
                }
                problemWriter.write("\n");
            }
            //Action cost, unit for now
            problemWriter.write("c(" + currentActionIndex + ",1).\n");

        }

        //The number of actions and predicates
        problemWriter.write("d(" + indexedAtoms.size() + "," + indexedActions.size()  + ").\n");            


        for (String initialLiteral : problem.getInitialLiterals()) {
            Integer literalIndex = atomsToIndex.get(initialLiteral);
            if(literalIndex == null){
                throw new PlanningException("Literal " + initialLiteral + " for initial state is not defined.");
            }
            problemWriter.write("i(" + literalIndex + ").");
            if(debug){
                problemWriter.write("%% " + initialLiteral);
            }
            problemWriter.write("\n");
        }
        for (String goalLiteral : problem.getGoalConditions()) {
            Integer literalIndex = atomsToIndex.get(goalLiteral);
            if(literalIndex == null){
                throw new PlanningException("Literal " + goalLiteral + " for initial state is not defined.");
            }
            problemWriter.write("g(" + literalIndex + ").");
            if(debug){
                problemWriter.write("%% " + goalLiteral);
            }
            problemWriter.write("\n");
        }


        problemWriter.close();
        return problemDefinitionTempFile;
    }
}
