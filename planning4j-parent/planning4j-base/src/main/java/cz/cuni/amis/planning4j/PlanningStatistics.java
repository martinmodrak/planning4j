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
