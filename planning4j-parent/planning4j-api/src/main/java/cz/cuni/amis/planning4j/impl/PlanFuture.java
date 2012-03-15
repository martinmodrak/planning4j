/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j.impl;

import cz.cuni.amis.planning4j.IPlanFuture;
import cz.cuni.amis.planning4j.IPlanningResult;
import cz.cuni.amis.utils.future.FutureWithListeners;

/**
 * Simple implemantation of IPlanFuture that relies on external thread
 * setting it's result. If the future should be cancellable, you need to override the {@link #cancelComputation(boolean) } method.
 * @author Martin Cerny
 * @see FutureWithListeners for more detail
 */
public class PlanFuture<RESULT extends IPlanningResult> extends FutureWithListeners<RESULT> implements IPlanFuture<RESULT> {
        
}
