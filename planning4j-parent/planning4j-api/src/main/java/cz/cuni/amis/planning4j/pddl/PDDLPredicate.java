/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class PDDLPredicate {
    private String name;
    private List<PDDLParameter> parameters;

    public PDDLPredicate(String name) {
        this.name = name;
        this.parameters = Collections.EMPTY_LIST;
    }
    
    
    public PDDLPredicate(String name, List<PDDLParameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }
    
    public PDDLPredicate(String name, PDDLParameter ... parameters ){
        this(name,Arrays.asList(parameters));        
    }

    public String getName() {
        return name;
    }

    public List<PDDLParameter> getParameters() {
        return parameters;
    }
    
    
}
