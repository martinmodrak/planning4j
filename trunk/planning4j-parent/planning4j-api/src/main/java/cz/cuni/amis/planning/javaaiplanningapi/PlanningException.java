package cz.cuni.amis.planning.javaaiplanningapi;

/**
 *
 * @author Martin Cerny
 */
public class PlanningException extends RuntimeException {

    public PlanningException(Throwable cause) {
        super(cause);
    }

    public PlanningException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlanningException(String message) {
        super(message);
    }

    public PlanningException() {
    }
    
}
