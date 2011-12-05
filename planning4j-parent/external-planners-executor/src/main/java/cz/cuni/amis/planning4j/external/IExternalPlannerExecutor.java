package cz.cuni.amis.planning4j.external;

import java.io.File;

/**
 *
 * @author Martin Cerny
 */
public interface IExternalPlannerExecutor {

    /**
     * A name of a logger that classes calling external planners should use
     */
    public static final String LOGGER_NAME = "ExternalPlanners.ExecPlanner";
    
    
    /**
     *
     * @param chosenPlanner
     * @param domainFile
     * @param problemFile
     * @return an xml representation of the plan
     */
    IExternalPlanningResult executePlanner(File domainFile, File problemFile);
}
