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
    
/**
   * List directory contents for a resource folder. Not recursive.
   * This is basically a brute-force implementation.
   * Works for regular files and also JARs.
   * 
   * @author Greg Briggs
   * @param clazz Any java class that lives in the same place as the resources you want.
   * @param path Should end with "/", but not start with one.
   * @return Just the name of each member item, not the full paths.
   * @throws URISyntaxException 
   * @throws IOException 
   */
  public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
      URL dirURL = clazz.getClassLoader().getResource(path);
      if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
        return new File(dirURL.toURI()).list();
      } 

      if (dirURL == null) {
        /* 
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
        String me = clazz.getName().replace(".", "/")+".class";
        dirURL = clazz.getClassLoader().getResource(me);
      }
      
      if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
        Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
        while(entries.hasMoreElements()) {
          String name = entries.nextElement().getName();
          if (name.startsWith(path)) { //filter according to the path
            String entry = name.substring(path.length());
            int checkSubdir = entry.indexOf("/");
            if (checkSubdir >= 0) {
              // if it is a subdirectory, we just return the directory name
              entry = entry.substring(0, checkSubdir);
            }
            result.add(entry);
          }
        }
        return result.toArray(new String[result.size()]);
      } 
        
      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
  }    
    
}
