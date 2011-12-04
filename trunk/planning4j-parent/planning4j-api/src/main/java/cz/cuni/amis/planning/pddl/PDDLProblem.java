/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning.pddl;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class PDDLProblem {
    String problemName;
    String domainName;
    
    List<String> initialLiterals;
    String goalCondition;

    public PDDLProblem(String problemName, String domainName) {
        this.problemName = problemName;
        this.domainName = domainName;
        initialLiterals = Collections.EMPTY_LIST;       
    }

    public void setGoalCondition(String goalCondition) {
        this.goalCondition = goalCondition;
    }

    public void setInitialLiterals(List<String> initialLiterals) {
        this.initialLiterals = initialLiterals;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getGoalCondition() {
        return goalCondition;
    }

    public List<String> getInitialLiterals() {
        return initialLiterals;
    }

    public String getProblemName() {
        return problemName;
    }

    
    
    
    
    
    
}
