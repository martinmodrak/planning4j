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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set of utility methods to create PDDL strings involving operators.
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
