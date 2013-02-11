/*** 
 * This file is part of Java API for AI planning
 * 
 * This file is based on code from project itSIMPLE.
 *
 * Java API for AI planning is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version. Other licenses might be available
 * upon written agreement.
 * 
 * Java API for AI planning is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Java API for AI planning.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Authors:	Matheus Haddad,
 *              Tiago S. Vaquero, Martin Cerny
 *			.
 **/
package cz.cuni.amis.planning4j.external.impl.itsimple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * A class that works with XML definition of planners in ItSimple formats
 * and allows to select a suitable planner for current platform.
 * This class is thread safe.
 * @author Matheus, Martin Cerny
 */
public class PlannerListManager extends SimplePlannerListManager {
        
    private final Logger logger = Logger.getLogger(PlannerListManager.class);
    
    public PlannerListManager(Element plannersXml) {
        super(plannersXml);
    }
    
    /**
     * Uses current working directory to call {@link #extractAndPreparePlanner(java.io.File, org.jdom.Element) }
     * @param selectedPlanner 
     */
    public final void extractAndPreparePlanner(ItSimplePlannerInformation selectedPlanner){
        extractAndPreparePlanner(new File("."), selectedPlanner);
    }
    
    
    /**
     * If the planner does not exist in specified location, it is extracted from the planners pack,
     * then {@link #preparePlanner(java.io.File, org.jdom.Element) } is called.
     * @param targetDirectory the directory where the binary will be stored
     * @param selectedPlanner 
     * @throws ItSimplePlanningException if there is an IO error
     */
    public void extractAndPreparePlanner(File targetDirectory, ItSimplePlannerInformation selectedPlanner) {

        if(selectedPlanner == null){
            throw new NullPointerException("Planner can't be null");
        }
        
        File binaryFile = ItSimpleUtils.getPlannerExecutableFile(targetDirectory, selectedPlanner);
        final String plannerRelativeFileName = selectedPlanner.getSettings().getExecutableFilePath();
        String plannerResourcePath = "/" + plannerRelativeFileName;
        
        ItSimpleUtils.extractFileIfNotExists(binaryFile, plannerResourcePath);                
        preparePlanner(targetDirectory, selectedPlanner);        
        
    }
        
    /**
     * Prepares a planner for execution.
     * As of now, it only tries to set executable permission on the planner file on linux
     * systems.
     * @param selectedPlanner 
     * @param plannersDirectory directory where the planners were unpacked
     */
    @Override
    public void preparePlanner(File plannersDirectory, ItSimplePlannerInformation selectedPlanner){
        super.preparePlanner(plannersDirectory, selectedPlanner);
        File plannerFile = ItSimpleUtils.getPlannerExecutableFile(plannersDirectory, selectedPlanner);
        ItSimpleUtils.prepareExecutableForExecution(plannerFile);
    }

}
