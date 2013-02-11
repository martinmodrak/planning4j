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

import cz.cuni.amis.planning4j.utils.Planning4JUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract representation of action returned by the planner.
 * All planning results are represented by this class.
 * The name of the action and it's parameters are always returned normalized 
 * by {@link  Planning4JUtils#normalizeIdentifier(java.lang.String) }.
 * @author Martin Cerny
 */
public class ActionDescription {
    
    /**
     * The name of the action (normalized)
     */
    String name;
    
    /**
     * List of instantied parameter values (normalized)
     */
    List<String> parameters;
    double startTime;
    double duration;
    
    String notes;

    public ActionDescription() {
        parameters = new ArrayList<String>();
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Notes for action - any metadata added by the planner.
     * @return 
     */
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getStartTime()).append(":");
        sb.append("(");
        sb.append(name);
        for(String param : parameters){
            sb.append(" ").append(param);
        }
        sb.append(")");
        return sb.toString();
    }
    
    
}
