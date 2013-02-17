/*
 * Copyright (C) 2011 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.utils;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.*;
import cz.cuni.amis.planning4j.pddl.PDDLDomain;
import cz.cuni.amis.planning4j.pddl.PDDLProblem;
import cz.cuni.amis.planning4j.translators.domain.NoTranslationDomainTranslator;
import cz.cuni.amis.planning4j.translators.problem.NoTranslationProblemTranslator;
import java.io.File;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A collection of utility methods.
 * @author Martin Cerny
 */
public class Planning4JUtils {

    private static ServiceLoader<IDomainTranslator> domainTranslatorLoader = ServiceLoader.load(IDomainTranslator.class);    
    private static ServiceLoader<IProblemTranslator> problemTranslatorLoader = ServiceLoader.load(IProblemTranslator.class);
    
    /**
     * Normalizes an identifier. Some planners modify indentifier names,
     * namely turn them into upper/lower case and/or replace minuses with underscores or
     * vice-versa. Identifier returned in planning result is guaranteed to be equal
     * to a normalized form of the identifier as it was submitted to the planner.
     * The identifier is returned in uppercase and minuses are translated to underscores.
     * @param identifier
     * @return 
     */
    public static String normalizeIdentifier(String identifier){
        return identifier.toUpperCase().replace('-', '_');
    }

    /**
     * Tries to find a domain translator for given pair of domain classes through Java SPI
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param sourceDomainClass
     * @param destinationDomainClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider> IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> findDomainTranslator(Class<SOURCE_DOMAIN> sourceDomainClass, Class<DESTINATION_DOMAIN> destinationDomainClass){
        if(destinationDomainClass.isAssignableFrom(sourceDomainClass)){
            return new NoTranslationDomainTranslator(destinationDomainClass);
        }
        //first search for an exact match
        for(IDomainTranslator translator : domainTranslatorLoader){
            if(translator.getSourceDomainClass().equals(sourceDomainClass) && translator.getDestinationDomainClass().equals(destinationDomainClass)){
                return translator;
            }
        }
        for(IDomainTranslator translator : domainTranslatorLoader){
            if(translator.getSourceDomainClass().isAssignableFrom(sourceDomainClass) && translator.getDestinationDomainClass().isAssignableFrom(destinationDomainClass)){
                return translator;
            }
        }
        return null;
    }

    /**
     * Tries to find a translator through Java SPI and throws an exception, if no such translator can be found
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param sourceDomainClass
     * @param destinationDomainClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider> IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> findDomainTranslatorThrowException(Class<SOURCE_DOMAIN> sourceDomainClass, Class<DESTINATION_DOMAIN> destinationDomainClass){
        IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> domainTranslator = findDomainTranslator(sourceDomainClass, destinationDomainClass);
        if(domainTranslator == null){
            throw new PlanningException("Could not find domain translator from " + sourceDomainClass + " to " + destinationDomainClass);
        }
        return domainTranslator;
    }

    
    /**
     * Tries to find a problem translator for given pair of domain classes through Java SPI
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param sourceProblemClass
     * @param destinationProblemClass
     * @return 
     */
    public static <SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider> IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> findProblemTranslator(Class<SOURCE_PROBLEM> sourceProblemClass, Class<DESTINATION_PROBLEM> destinationProblemClass){
        if(destinationProblemClass.isAssignableFrom(sourceProblemClass)){
            return new NoTranslationProblemTranslator(destinationProblemClass);
        }
        //first search for an exact match
        for(IProblemTranslator translator : problemTranslatorLoader){
            if(translator.getSourceProblemClass().equals(sourceProblemClass) && translator.getDestinationProblemClass().equals(destinationProblemClass)){
                return translator;
            }
        }
        for(IProblemTranslator translator : problemTranslatorLoader){
            if(translator.getSourceProblemClass().isAssignableFrom(sourceProblemClass) && translator.getDestinationProblemClass().isAssignableFrom(destinationProblemClass)){
                return translator;
            }
        }
        return null;
    }

    /**
     * Tries to find a translator through Java SPI and throws an exception, if no such translator can be found
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param sourceProblemClass
     * @param destinationProblemClass
     * @return 
     */
    public static <SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider> IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> findProblemTranslatorThrowException(Class<SOURCE_PROBLEM> sourceProblemClass, Class<DESTINATION_PROBLEM> destinationProblemClass){
        IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> problemTranslator = findProblemTranslator(sourceProblemClass, destinationProblemClass);
        if(problemTranslator == null){
            throw new PlanningException("Could not find problem translator from " + sourceProblemClass + " to " + destinationProblemClass);
        }
        return problemTranslator;
    }
    
    /**
     * Creates a proxy planner that accepts SOURCE_DOMAIN and SOURCE_PROBLEM type input, 
     * translates the source domain and problem to forms understood by
     * the given planner and delegates the actual planning to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param original
     * @param sourceDomainClass
     * @param sourceProblemClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
            SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
            IAsyncPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM> getTranslatingAsyncPlanner(IAsyncPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> original,Class<SOURCE_DOMAIN> sourceDomainClass, Class<SOURCE_PROBLEM> sourceProblemClass){
        
        if(original.getDomainClass().isAssignableFrom(sourceDomainClass) && original.getProblemClass().isAssignableFrom(sourceProblemClass)){
            //no translation required
            return (IAsyncPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM>)original;
        }
        return new TranslatingAsyncPlanner<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>(original, findDomainTranslatorThrowException(sourceDomainClass, original.getDomainClass()), findProblemTranslatorThrowException(sourceProblemClass, original.getProblemClass()));
    }
    
    /**
     * Creates a proxy planner that accepts SOURCE_DOMAIN and SOURCE_PROBLEM type input, 
     * translates the source domain and problem to forms understood by
     * the given planner and delegates the actual planning to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param original
     * @param sourceDomainClass
     * @param sourceProblemClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
            SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
            IPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM> getTranslatingPlanner(IPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> original,Class<SOURCE_DOMAIN> sourceDomainClass, Class<SOURCE_PROBLEM> sourceProblemClass){
        
        if(original.getDomainClass().isAssignableFrom(sourceDomainClass) && original.getProblemClass().isAssignableFrom(sourceProblemClass)){
            //no translation required
            return (IPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM>)original;
        }        
        return new TranslatingPlanner<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>(original, findDomainTranslatorThrowException(sourceDomainClass, original.getDomainClass()), findProblemTranslatorThrowException(sourceProblemClass, original.getProblemClass()));
    }

    /**
     * Creates a proxy planner that accepts given domain and problem input, 
     * translates the source domain and problem to forms understood by
     * the given planner and delegates the actual planning to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanner getTranslatingPlanner(IPlanner planner, IDomainProvider domain, IProblemProvider problem) {
        return getTranslatingPlanner(planner, domain.getClass(), problem.getClass());
    }
    
    /**
     * Creates a proxy planner that accepts given domain and problem input, 
     * translates the source domain and problem to forms understood by
     * the given planner and delegates the actual planning to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IAsyncPlanner getTranslatingAsyncPlanner(IAsyncPlanner planner, IDomainProvider domain, IProblemProvider problem) {
        return getTranslatingAsyncPlanner(planner, domain.getClass(), problem.getClass());
    }    
    
    /**
     * Runs given planner on a domain and a problem spec, translating them if needed.
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanningResult plan(IPlanner planner, IDomainProvider domain, IProblemProvider problem){
        return getTranslatingPlanner(planner, domain, problem).plan(domain, problem);
    }

    /**
     * Runs given planner on a domain and a problem spec, translating them if needed.
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanningResult plan(IPlanner planner, PDDLDomain domain, PDDLProblem problem){
        return plan(planner, new PDDLObjectDomainProvider(domain), new PDDLObjectProblemProvider(problem));
    }

    /**
     * Runs given planner on a domain and a problem spec from specified files (in PDDL), translating them if needed.
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanningResult planPDDL(IPlanner planner, File domainFile, File problemFile){
        return plan(planner, new PDDLFileDomainProvider(domainFile), new PDDLFileProblemProvider(problemFile));
    }

    /**
     * Runs given planner on a domain and a problem spec (in PDDL), translating them if needed.
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanningResult planPDDL(IPlanner planner, String domain, String problem){
        return plan(planner, new PDDLStringDomainProvider(domain), new PDDLStringProblemProvider(problem));
    }
    
    
    /**
     * Runs given planner asynchronously on a domain and a problem spec, translating them if needed.
     * @param planner
     * @param domain
     * @param problem
     * @return 
     */
    public static IPlanFuture<IPlanningResult> planAsync(IAsyncPlanner planner, IDomainProvider domain, IProblemProvider problem){
        return getTranslatingAsyncPlanner(planner, domain, problem).planAsync(domain, problem);
    }
    

    /**
     * Creates a proxy validator that accepts SOURCE_DOMAIN and SOURCE_PROBLEM type input, 
     * translates the source domain and problem to forms understood by
     * the given validator and delegates the actual validation to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param original
     * @param sourceDomainClass
     * @param sourceProblemClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
            SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
            IValidator<SOURCE_DOMAIN, SOURCE_PROBLEM> getTranslatingValidator(IValidator<DESTINATION_DOMAIN, DESTINATION_PROBLEM> original,Class<SOURCE_DOMAIN> sourceDomainClass, Class<SOURCE_PROBLEM> sourceProblemClass){
        
        if(original.getDomainClass().isAssignableFrom(sourceDomainClass) && original.getProblemClass().isAssignableFrom(sourceProblemClass)){
            //no translation required
            return (IValidator<SOURCE_DOMAIN, SOURCE_PROBLEM>)original;
        }        
        return new TranslatingValidator<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>(original, findDomainTranslatorThrowException(sourceDomainClass, original.getDomainClass()), findProblemTranslatorThrowException(sourceProblemClass, original.getProblemClass()));
    }

    /**
     * Creates a proxy validator that accepts SOURCE_DOMAIN and SOURCE_PROBLEM type input, 
     * translates the source domain and problem to forms understood by
     * the given validator and delegates the actual validation to it. The translators are looked up with {@link #findDomainTranslator(java.lang.Class, java.lang.Class) }
     * and {@link #findProblemTranslator(java.lang.Class, java.lang.Class) }
     * @param validator
     * @param domain
     * @param problem
     * @return 
     */
    public static IValidator getTranslatingValidator(IValidator validator, IDomainProvider domain, IProblemProvider problem) {
        return getTranslatingValidator(validator, domain.getClass(), problem.getClass());
    }
    

    /**
     * Runs given validator on a domain and a problem spec, translating them if needed.
     */
    public static IValidationResult validate(IValidator validator, IDomainProvider domain, IProblemProvider problem, List<ActionDescription> plan){
        return getTranslatingValidator(validator, domain, problem).validate(domain, problem, plan);
    }
}
