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
package cz.cuni.amis.planning4j.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple implementation of {@link PDDLAction} that
 * seperates precondition and effects into lists of literals.
 * @author Martin Cerny
 */
public class PDDLSimpleAction extends PDDLAction{

    private List<String> preconditionList;
    private List<String> positiveEffects;
    private List<String> negativeEffects;

    public PDDLSimpleAction(String name, PDDLParameter... parameters) {
        super(name, parameters);
        init();
    }

    public PDDLSimpleAction(String name, List<PDDLParameter> parameters) {
        super(name, parameters);
        init();
    }

    public PDDLSimpleAction(String name) {
        super(name);
        init();
    }
    
    private void init(){
        preconditionList = new ArrayList<String>();
        positiveEffects =  new ArrayList<String>();
        negativeEffects =  new ArrayList<String>();
    }
    

    public List<String> getNegativeEffects() {
        return negativeEffects;
    }

    public void setNegativeEffects(List<String> negativeEffects) {
        this.negativeEffects = negativeEffects;
    }
    
    public void setNegativeEffects(String ... negativeEffects){
        setNegativeEffects(new ArrayList(Arrays.asList(negativeEffects)));        
    }
    
    public void addNegativeEffect(String negativeEffect){
        this.negativeEffects.add(negativeEffect);
    }

    public List<String> getPositiveEffects() {
        return positiveEffects;
    }

    public void setPositiveEffects(List<String> positiveEffects) {
        this.positiveEffects = positiveEffects;
    }

    public void setPositiveEffects(String ... positiveEffects){
        setPositiveEffects(new ArrayList(Arrays.asList(positiveEffects)));        
    }
    
    public void addPositiveEffect(String positiveEffect){
        this.positiveEffects.add(positiveEffect);
    }

    public List<String> getPreconditionList() {
        return preconditionList;
    }

    public void setPreconditionList(List<String> preconditionList) {
        this.preconditionList = preconditionList;
    }

    public void setPreconditionList(String ... preconditionList){
        setPreconditionList(new ArrayList(Arrays.asList(preconditionList)));
    }
    
    public void addPrecondition(String precondition){
        this.preconditionList.add(precondition);
    }

    @Override
    public String getPreconditionAsString() {
        if(preconditionList == null || preconditionList.isEmpty()){
            return null;
        }
        return PDDLOperators.makeAnd(preconditionList);
    }

    @Override
    public String getEffectAsString() {
        if(negativeEffects.isEmpty()){
            if(positiveEffects.isEmpty()){
                return "";
            } else {
                return PDDLOperators.makeAnd(positiveEffects);
            }
        } else {
            List<String> negativeEffectsNegated = new ArrayList<String>(negativeEffects.size());
            for(String negEff : negativeEffects){
                negativeEffectsNegated.add(PDDLOperators.makeNot(negEff));
            }
            String negativeEffExpression = PDDLOperators.makeAnd(negativeEffectsNegated);
            if(positiveEffects.isEmpty()){
                return negativeEffExpression;
            } else {
                List<String> allEffects  = new ArrayList<String>(positiveEffects);
                allEffects.add(negativeEffExpression);
                return PDDLOperators.makeAnd(allEffects);
            }
        }
    }
    
    
    
}
