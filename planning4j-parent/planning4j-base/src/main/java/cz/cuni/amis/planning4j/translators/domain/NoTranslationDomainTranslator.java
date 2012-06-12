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

package cz.cuni.amis.planning4j.translators.domain;

import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.impl.AbstractDomainTranslator;

/**
 * A helper class that provides no translation. Not advertised through Java SPI.
 * @author Martin Cerny
 */
public class NoTranslationDomainTranslator<DOMAIN_TYPE extends IDomainProvider> extends AbstractDomainTranslator<DOMAIN_TYPE, DOMAIN_TYPE> {

    public NoTranslationDomainTranslator(Class<DOMAIN_TYPE> domainClass) {
        super(domainClass, domainClass);
    }

    
    
    @Override
    public DOMAIN_TYPE translateDomain(DOMAIN_TYPE problem) {
        return problem;
    }

}
