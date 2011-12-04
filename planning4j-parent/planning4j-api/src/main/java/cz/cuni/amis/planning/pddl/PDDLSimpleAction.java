/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
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
        preconditionList = Collections.EMPTY_LIST;
        positiveEffects = Collections.EMPTY_LIST;
        negativeEffects = Collections.EMPTY_LIST;;
    }
    

    public List<String> getNegativeEffects() {
        return negativeEffects;
    }

    public void setNegativeEffects(List<String> negativeEffects) {
        this.negativeEffects = negativeEffects;
    }
    
    public void setNegativeEffects(String ... negativeEffects){
        setNegativeEffects(Arrays.asList(negativeEffects));        
    }

    public List<String> getPositiveEffects() {
        return positiveEffects;
    }

    public void setPositiveEffects(List<String> positiveEffects) {
        this.positiveEffects = positiveEffects;
    }

    public void setPositiveEffects(String ... positiveEffects){
        setPositiveEffects(Arrays.asList(positiveEffects));        
    }

    public List<String> getPreconditionList() {
        return preconditionList;
    }

    public void setPreconditionList(List<String> preconditionList) {
        this.preconditionList = preconditionList;
    }

    public void setPreconditionList(String ... preconditionList){
        setPreconditionList(Arrays.asList(preconditionList));
    }
    

    @Override
    public String getPrecondition() {
        if(preconditionList == null || preconditionList.isEmpty()){
            return null;
        }
        return PDDLOperators.makeAnd(preconditionList);
    }

    @Override
    public String getEffect() {
        if(negativeEffects.isEmpty()){
            if(positiveEffects.isEmpty()){
                return "";
            } else {
                return PDDLOperators.makeAnd(positiveEffects);
            }
        } else {
            String negativeEffExpression = PDDLOperators.makeNot(PDDLOperators.makeAnd(negativeEffects));
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
