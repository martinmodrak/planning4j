package cz.cuni.amis.planning4j;

import java.io.IOException;

/**
 * An IO exception occurred when planning
 * @author Martin Cerny
 */
public class PlanningIOException extends PlanningException {

    public PlanningIOException(IOException cause) {
        super(cause);
    }

    public PlanningIOException(String message, IOException cause) {
        super(message, cause);
    }

    public PlanningIOException(String message) {
        super(message);
    }

    public PlanningIOException() {
    }

    @Override
    public IOException getCause() {
        return (IOException)super.getCause();
    }
    
    
    
}
