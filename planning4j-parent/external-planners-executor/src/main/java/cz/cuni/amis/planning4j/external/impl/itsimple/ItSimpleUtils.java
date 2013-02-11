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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author cernm6am
 */
public class ItSimpleUtils {
    
    private static final Object fileOperationsMutex = new Object();
    
    private static final Logger logger = Logger.getLogger(ItSimpleUtils.class);
    
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
    
    public static void extractFileIfNotExists(File targetFile, String resourcePath){
        synchronized(fileOperationsMutex){
            if(!targetFile.exists()){        
                InputStream inputStream = ItSimpleUtils.class.getResourceAsStream(resourcePath);
                if(inputStream == null){
                    throw new ItSimplePlanningException("Could not find file resource on classpath. Resource path:" + resourcePath);
                }
                if(!targetFile.getParentFile().exists() && !targetFile.getParentFile().mkdirs()){
                    throw new ItSimplePlanningException("Could not create parent dirs for file resource " + targetFile);                
                }
                try {
                    FileOutputStream outputfilestream = new FileOutputStream(targetFile);
                    IOUtils.copy(inputStream, outputfilestream);
                    outputfilestream.close();
                } catch (IOException ex){
                    throw new ItSimplePlanningException("Could not extract file resource to " + targetFile, ex);
                }
            }
        }
    }
    
    public static void prepareExecutableForExecution(File plannerFile) {
        if(ItSimpleUtils.getOperatingSystem().equals(EPlannerPlatform.LINUX)){
            try {
                Runtime.getRuntime().exec(new String[] { "chmod", "+x", plannerFile.getAbsolutePath()});
            } catch (IOException ex) {
                logger.warn("Could not set planner file permissions", ex);
            }            
        }
    }    
    
}
