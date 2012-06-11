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

import cz.cuni.amis.planning4j.*;

/**
 *
 * @author Martin Cerny
 */
public class TranslatingAsyncPlanner 
    <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
     SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
        extends TranslatingPlanner<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>
        implements IAsyncPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM>
        {
    
    IAsyncPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> originalAsyncPlanner;

    public TranslatingAsyncPlanner(IAsyncPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> originalPlanner, IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> domainTranslator, IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> problemTranslator) {
        super(originalPlanner, domainTranslator, problemTranslator);
        this.originalAsyncPlanner = originalPlanner;
    }

    @Override
    public IPlanFuture planAsync(SOURCE_DOMAIN domainProvider, SOURCE_PROBLEM problemProvider) {
        return originalAsyncPlanner.planAsync(domainTranslator.translateDomain(domainProvider), problemTranslator.translateProblem(problemProvider));        
    }

    
}
