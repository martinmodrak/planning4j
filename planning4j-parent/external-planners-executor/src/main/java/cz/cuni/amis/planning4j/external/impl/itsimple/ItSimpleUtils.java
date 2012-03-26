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
    public static EPlannerPlatform getOperatingSystem() {        
        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if (operatingSystem.indexOf("linux") == 0) {
            return EPlannerPlatform.LINUX;
        } else if (operatingSystem.indexOf("windows") == 0) {
            return EPlannerPlatform.WINDOWS;
        } else if (operatingSystem.indexOf("mac") == 0) {
            return EPlannerPlatform.MAC;
        }

        throw new IllegalStateException("Current platform could not be guessed");
    }    
    
    
    public static File getPlannerExecutableFile(File baseDirectory, ItSimplePlannerInformation chosenPlanner){
        File plannerExecutableFile = new File(baseDirectory, chosenPlanner.getSettings().getExecutableFilePath());        
        return plannerExecutableFile;
    }
}
