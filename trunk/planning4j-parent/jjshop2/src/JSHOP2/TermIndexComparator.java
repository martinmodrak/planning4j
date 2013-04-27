/*
 * Copyright (C) 2013 AMIS research group, Faculty of Mathematics and Physics, Charles University in Prague, Czech Republic
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

package JSHOP2;

import java.util.Comparator;

/**
 * A helper class to map user specified term comparators to indexed term array comparators requested by the JSHOP
 * engine.
 * @author Martin Cerny
 */
public class TermIndexComparator implements Comparator<Term[]> {

    private Comparator<Term> termComparator;
    private int index;

    public TermIndexComparator(Comparator<Term> termComparator, int index) {
        this.termComparator = termComparator;
        this.index = index;
    }


    @Override
    public int compare(Term[] o1, Term[] o2) {
        return termComparator.compare(o1[index], o2[index]);
    }
    

}
