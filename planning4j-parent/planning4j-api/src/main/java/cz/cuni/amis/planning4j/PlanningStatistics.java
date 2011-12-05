/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Planning statistics returned by the planner. Some property values
 * might be null, if the planner did not provide these statistics.
 * @author Martin Cerny
 */
public class PlanningStatistics {
    Double time;
    Double parsingTime;
    Integer numberOfActions;
    Double makeSpan;
    Double metricValue;
    String planningTechnique;
    
    List<String> additionalStats;

    public PlanningStatistics() {
        additionalStats = new ArrayList<String>();
    }

    public void setMakeSpan(double makeSpan) {
        this.makeSpan = makeSpan;
    }

    public void setMetricValue(double metricValue) {
        this.metricValue = metricValue;
    }

    public void setNumberOfActions(int numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    public void setParsingTime(double parsingTime) {
        this.parsingTime = parsingTime;
    }

    public void setPlanningTechnique(String planningTechnique) {
        this.planningTechnique = planningTechnique;
    }

    public void setTime(double time) {
        this.time = time;
    }
    

    public void addAdditionalStat(String stat){
        additionalStats.add(stat);
    }

    public List<String> getAdditionalStats() {
        return additionalStats;
    }

    public Double getMakeSpan() {
        return makeSpan;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public Integer getNumberOfActions() {
        return numberOfActions;
    }

    public Double getParsingTime() {
        return parsingTime;
    }

    public String getPlanningTechnique() {
        return planningTechnique;
    }

    public Double getTime() {
        return time;
    }
    
    
    
}
