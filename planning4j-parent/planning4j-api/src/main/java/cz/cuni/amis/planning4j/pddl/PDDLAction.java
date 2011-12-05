/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.pddl;

import com.google.common.collect.Collections2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public abstract class PDDLAction {
    String name;
    List<PDDLParameter> parameters;

    public PDDLAction(String name, List<PDDLParameter> parameters) {
        this.name= name;
        this.parameters = parameters;
    }
    
    public PDDLAction(String name,PDDLParameter ... parameters) {
        this(name,Arrays.asList(parameters));
    }

    public PDDLAction(String name) {
        this(name, Collections.EMPTY_LIST);
    }

    
    public abstract String getEffect();

    public List<PDDLParameter> getParameters() {
        return parameters;
    }

    public abstract String getPrecondition();

    public String getName() {
        return name;
    }

    
    
    
}
