/*
 * Copyright (C) 2011 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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
package cz.cuni.amis.planning4j.pddl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An object correspoding to a predicate in PDDL.
 * @author Martin Cerny
 */
public class PDDLPredicate {
    private String name;
    private List<PDDLParameter> parameters;

    public PDDLPredicate(String name) {
        this.name = name;
        this.parameters = Collections.EMPTY_LIST;
    }
    
    
    public PDDLPredicate(String name, List<PDDLParameter> parameters) {
        this.name = name;
        this.parameters = parameters;
    }
    
    public PDDLPredicate(String name, PDDLParameter ... parameters ){
        this(name,Arrays.asList(parameters));        
    }

    public String getName() {
        return name;
    }

    public List<PDDLParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return name + "/" + parameters.size();
    }
    
    
}
