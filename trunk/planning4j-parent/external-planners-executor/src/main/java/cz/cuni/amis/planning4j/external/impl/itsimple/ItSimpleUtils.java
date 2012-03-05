/*
 * Copyright (C) 2012 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.external.impl.itsimple;

import java.io.File;
import org.jdom.Element;

/**
 *
 * @author cernm6am
 */
public class ItSimpleUtils {
    
    /**
     * This method gets the operating system name on which the program is running as
     * used in itSimple xml format
     * @return "linux" OR "windows" OR "mac"
     */
    public static String getOperatingSystem() {        
        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if (operatingSystem.indexOf("linux") == 0) {
            operatingSystem = "linux";
        } else if (operatingSystem.indexOf("windows") == 0) {
            operatingSystem = "windows";
        } else if (operatingSystem.indexOf("mac") == 0) {
            operatingSystem = "mac";
        }

        return operatingSystem;
    }    
    

    public static String getPlannerRelativeFileName(Element chosenPlanner){
        Element settings = chosenPlanner.getChild("settings");

        String plannerRelativeFile = settings.getChildText("filePath");
        return plannerRelativeFile;
    }
    
    public static File getPlannerExecutableFile(File baseDirectory, Element chosenPlanner){
        //System.out.println(plannerFile);
        File plannerExecutableFile = new File(baseDirectory, getPlannerRelativeFileName(chosenPlanner));        
        return plannerExecutableFile;
    }
}
