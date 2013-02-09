/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

import cz.cuni.amis.utils.future.FutureStatus;
import cz.cuni.amis.utils.future.IFutureListener;
import java.util.concurrent.Future;

/**
 *
 * @author Martin Cerny
 */
public interface IPlanFuture<RESULT extends IPlanningResult> extends Future<RESULT> {
	/**
	 * Current status of the plan computation.
	 * @return
	 */
	public FutureStatus getStatus();
	
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
        public RESULT get();
        
	/**
	 * Adds a listener on a future status (using strong reference). Listeners are automatically
	 * removed whenever the future gets its result (or is cancelled or an exception happens).
	 * @param listener
	 */
	public void addFutureListener(IFutureListener<RESULT> listener);
	
	/**
	 * Removes a listener from the future.
	 * @param listener
	 */
	public void removeFutureListener(IFutureListener<RESULT> listener);
	
	/**
	 * Whether some listener is listening on the future.
	 * @param listener
	 * @return
	 */
	public boolean isListening(IFutureListener<RESULT> listener);
        
        /**
         * Gets excetption (if any) that terminated the computation of this future 
         * @return 
         */
        public Exception getException();
    
}
