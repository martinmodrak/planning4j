package cz.cuni.amis.planning4j;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Cerny
 */
public class ActionDescription {
    
    /**
     * The name of the action (uppercase)
     */
    String name;
    
    /**
     * List of parameter values (uppercase)
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

    
    
    
}
