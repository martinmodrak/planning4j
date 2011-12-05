/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

/**
 *
 * @author Martin Cerny
 */
public interface IPlanner {
    public IPlanningResult plan(IDomainProvider domainProvider, IProblemProvider problemProvider);
}
