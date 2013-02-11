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

package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.IDomainProvider;
import cz.cuni.amis.planning4j.IProblemProvider;
import cz.cuni.amis.planning4j.IValidator;

/**
 *
 * @author Martin Cerny
 */
public abstract class AbstractValidator<DOMAIN_TYPE extends IDomainProvider, PROBLEM_TYPE extends IProblemProvider> implements IValidator<DOMAIN_TYPE, PROBLEM_TYPE> {
   
    private Class domainClass;
    private Class problemClass;

    public AbstractValidator(Class domainClass, Class problemClass) {
        this.domainClass = domainClass;
        this.problemClass = problemClass;
    }

    @Override
    public Class getDomainClass() {
        return domainClass;
    }

    @Override
    public Class getProblemClass() {
        return problemClass;
    }
    
    
}
