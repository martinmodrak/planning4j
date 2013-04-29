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

import cz.cuni.amis.planning4j.pddl.PDDLRequirement;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author cernm6am
 */
public class ItSimplePlannerInformation {
    String name;
    String version;
    String date;
    String author;
    String link;
    String description;
    Set<EPlannerPlatform> supportedPlatforms;
    Set<PDDLRequirement> supportedRequirements;
    ItSimplePlannerSettings settings;

    public ItSimplePlannerInformation() {
        supportedPlatforms = Collections.EMPTY_SET;
        supportedRequirements = Collections.EMPTY_SET;
    }

    
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItSimplePlannerSettings getSettings() {
        return settings;
    }

    public void setSettings(ItSimplePlannerSettings settings) {
        this.settings = settings;
    }

    public Set<EPlannerPlatform> getSupportedPlatforms() {
        return supportedPlatforms;
    }

    public void setSupportedPlatforms(Set<EPlannerPlatform> supportedPlatforms) {
        this.supportedPlatforms = supportedPlatforms;
    }

    public Set<PDDLRequirement> getSupportedRequirements() {
        return supportedRequirements;
    }

    public void setSupportedRequirements(Set<PDDLRequirement> supportedRequirements) {
        this.supportedRequirements = supportedRequirements;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ItSimplePlannerInfo{" + name + " " + version + '}';
    }
    
    
}
