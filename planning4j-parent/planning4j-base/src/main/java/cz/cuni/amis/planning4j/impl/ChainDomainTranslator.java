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

package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.IDomainTranslator;

/**
 * A class that allows to chain two translators to form a new one.
 * @author Martin Cerny
 */
public abstract class ChainDomainTranslator <SOURCE extends IDomainProvider, MIDDLE extends IDomainProvider, DESTINATION extends IDomainProvider> 
    extends AbstractDomainTranslator<SOURCE, DESTINATION> {


    private IDomainTranslator<SOURCE, MIDDLE> translator1;
    private IDomainTranslator<MIDDLE, DESTINATION> translator2;

    public ChainDomainTranslator(IDomainTranslator<SOURCE, MIDDLE> translator1, IDomainTranslator<MIDDLE, DESTINATION> translator2, Class<SOURCE> sourceDomainClass, Class<DESTINATION> destinationDomainClass) {
        super(sourceDomainClass, destinationDomainClass);
        this.translator1 = translator1;
        this.translator2 = translator2;
    }

    @Override
    public DESTINATION translateDomain(SOURCE domain) {
        return translator2.translateDomain(translator1.translateDomain(domain));
    }
    
    
}
