/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

import com.google.common.collect.Collections2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class PDDLOperators {
    public static String makeOperatorExpression(String operator, List<String> operands){
        if(operands.isEmpty()){
            throw new IllegalArgumentException("Operands cannot be emtpy");
        }
        StringBuilder sb = new StringBuilder(operator);
        for(String op : operands){
            sb.append(" (").append(op).append(")");
        }
        return sb.toString();
    }
    
    public static String makeAnd(List<String> operands){
        if(operands.size() == 1){
            return operands.get(0) ;
        }
        return makeOperatorExpression("and", operands);
    }
    
    public static String makeAnd(String ... operands){
        return makeAnd(Arrays.asList(operands));
    }
    
    
    public static String makeNot(String operand){
        return makeOperatorExpression("not", Collections.singletonList(operand));
    }
}
