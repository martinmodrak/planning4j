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
package cz.cuni.amis.planning4j;

import java.io.IOException;
import java.io.Writer;

/**
 * A domain provider that is capable of writing the problem with a Writer in PDDL form.
 * Writer based implementations will likely perform better
 * with external planners.
 * @author Martin Cerny
 */
public interface IPDDLWriterDomainProvider extends IDomainProvider{

    public void writeDomain(Writer writer) throws IOException;
    
}
