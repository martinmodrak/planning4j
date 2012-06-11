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
package cz.cuni.amis.planning4j.utils;

import cz.cuni.amis.planning4j.*;
import cz.cuni.amis.planning4j.impl.TranslatingAsyncPlanner;
import cz.cuni.amis.planning4j.impl.TranslatingPlanner;
import cz.cuni.amis.planning4j.translators.domain.NoTranslationDomainTranslator;
import cz.cuni.amis.planning4j.translators.problem.NoTranslationProblemTranslator;
import java.util.ServiceLoader;

/**
 * A collection of utility methods employed by most planners.
 * @author Martin Cerny
 */
public class Planning4JUtils {

    private static ServiceLoader<IDomainTranslator> domainTranslatorLoader = ServiceLoader.load(IDomainTranslator.class);    
    private static ServiceLoader<IProblemTranslator> problemTranslatorLoader = ServiceLoader.load(IProblemTranslator.class);    
    
    /**
     * Normalizes an identifier. Some planners modify indentifier names,
     * namely turn them into upper/lower case and/or replace minuses with underscores or
     * vice-versa. Identifier returned in planning result is guaranteed to be equal
     * to a normalized form of the identifier as it was submitted to the planner.
     * The identifier is returned in uppercase and minuses are translated to underscores.
     * @param identifier
     * @return 
     */
    public static String normalizeIdentifier(String identifier){
        return identifier.toUpperCase().replace('-', '_');
    }

    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider> IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> findDomainTranslator(Class<SOURCE_DOMAIN> sourceDomainClass, Class<DESTINATION_DOMAIN> destinationDomainClass){
        if(destinationDomainClass.isAssignableFrom(sourceDomainClass)){
            return new NoTranslationDomainTranslator(destinationDomainClass);
        }
        //first search for an exact match
        for(IDomainTranslator translator : domainTranslatorLoader){
            if(translator.getSourceDomainClass().equals(sourceDomainClass) && translator.getDestinationDomainClass().equals(destinationDomainClass)){
                return translator;
            }
        }
        for(IDomainTranslator translator : domainTranslatorLoader){
            if(translator.getSourceDomainClass().isAssignableFrom(sourceDomainClass) && translator.getDestinationDomainClass().isAssignableFrom(destinationDomainClass)){
                return translator;
            }
        }
        return null;
    }

    /**
     * Tries to find a translator and throws an exception, if no such translator can be found
     * @param <SOURCE_DOMAIN>
     * @param <DESTINATION_DOMAIN>
     * @param sourceDomainClass
     * @param destinationDomainClass
     * @return 
     */
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider> IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> findDomainTranslatorThrowException(Class<SOURCE_DOMAIN> sourceDomainClass, Class<DESTINATION_DOMAIN> destinationDomainClass){
        IDomainTranslator<SOURCE_DOMAIN, DESTINATION_DOMAIN> domainTranslator = findDomainTranslator(sourceDomainClass, destinationDomainClass);
        if(domainTranslator == null){
            throw new PlanningException("Could not find domain translator from " + sourceDomainClass + " to " + destinationDomainClass);
        }
        return domainTranslator;
    }

    
    public static <SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider> IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> findProblemTranslator(Class<SOURCE_PROBLEM> sourceProblemClass, Class<DESTINATION_PROBLEM> destinationProblemClass){
        if(destinationProblemClass.isAssignableFrom(sourceProblemClass)){
            return new NoTranslationProblemTranslator(destinationProblemClass);
        }
        //first search for an exact match
        for(IProblemTranslator translator : problemTranslatorLoader){
            if(translator.getSourceProblemClass().equals(sourceProblemClass) && translator.getDestinationProblemClass().equals(destinationProblemClass)){
                return translator;
            }
        }
        for(IProblemTranslator translator : problemTranslatorLoader){
            if(translator.getSourceProblemClass().isAssignableFrom(sourceProblemClass) && translator.getDestinationProblemClass().isAssignableFrom(destinationProblemClass)){
                return translator;
            }
        }
        return null;
    }

    /**
     * Tries to find a translator and throws an exception, if no such translator can be found
     * @param <SOURCE_PROBLEM>
     * @param <DESTINATION_PROBLEM>
     * @param sourceProblemClass
     * @param destinationProblemClass
     * @return 
     */
    public static <SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider> IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> findProblemTranslatorThrowException(Class<SOURCE_PROBLEM> sourceProblemClass, Class<DESTINATION_PROBLEM> destinationProblemClass){
        IProblemTranslator<SOURCE_PROBLEM, DESTINATION_PROBLEM> problemTranslator = findProblemTranslator(sourceProblemClass, destinationProblemClass);
        if(problemTranslator == null){
            throw new PlanningException("Could not find problem translator from " + sourceProblemClass + " to " + destinationProblemClass);
        }
        return problemTranslator;
    }
    
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
            SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
            IAsyncPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM> getTranslatingAsyncPlanner(IAsyncPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> original,Class<SOURCE_DOMAIN> sourceDomainClass, Class<SOURCE_PROBLEM> sourceProblemClass){
        
        if(original.getDomainClass().isAssignableFrom(sourceDomainClass) && original.getProblemClass().isAssignableFrom(sourceProblemClass)){
            //no translation required
            return (IAsyncPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM>)original;
        }
        return new TranslatingAsyncPlanner<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>(original, findDomainTranslatorThrowException(sourceDomainClass, original.getDomainClass()), findProblemTranslatorThrowException(sourceProblemClass, original.getProblemClass()));
    }
    
    public static <SOURCE_DOMAIN extends IDomainProvider, DESTINATION_DOMAIN extends IDomainProvider,
            SOURCE_PROBLEM extends IProblemProvider, DESTINATION_PROBLEM extends IProblemProvider>
            IPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM> getTranslatingPlanner(IPlanner<DESTINATION_DOMAIN, DESTINATION_PROBLEM> original,Class<SOURCE_DOMAIN> sourceDomainClass, Class<SOURCE_PROBLEM> sourceProblemClass){
        
        if(original.getDomainClass().isAssignableFrom(sourceDomainClass) && original.getProblemClass().isAssignableFrom(sourceProblemClass)){
            //no translation required
            return (IPlanner<SOURCE_DOMAIN, SOURCE_PROBLEM>)original;
        }        
        return new TranslatingPlanner<SOURCE_DOMAIN, DESTINATION_DOMAIN, SOURCE_PROBLEM, DESTINATION_PROBLEM>(original, findDomainTranslatorThrowException(sourceDomainClass, original.getDomainClass()), findProblemTranslatorThrowException(sourceProblemClass, original.getProblemClass()));
    }
    
    public static IPlanningResult plan(IPlanner planner, IDomainProvider domain, IProblemProvider problem){
        return getTranslatingPlanner(planner, domain.getClass(), problem.getClass()).plan(domain, problem);
    }
    
    public static IPlanFuture<IPlanningResult> planAsync(IAsyncPlanner planner, IDomainProvider domain, IProblemProvider problem){
        return getTranslatingAsyncPlanner(planner, domain.getClass(), problem.getClass()).planAsync(domain, problem);
    }
    
}
