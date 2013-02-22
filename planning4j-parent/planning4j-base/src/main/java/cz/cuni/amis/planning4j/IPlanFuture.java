/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.amis.planning4j;

import cz.cuni.amis.utils.future.FutureStatus;
import cz.cuni.amis.utils.future.IFutureListener;
import cz.cuni.amis.utils.future.IFutureWithListeners;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Martin Cerny
 */
public interface IPlanFuture<RESULT extends IPlanningResult> extends IFutureWithListeners<RESULT> {
	
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
        public RESULT get();
        
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
        RESULT get(long timeout, TimeUnit unit);        
        
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
	public void addFutureListener(IFutureListener<RESULT> listener);
	
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
	public void removeFutureListener(IFutureListener<RESULT> listener);
	
        /**
         * Overriden here to provide correct generic parameter binding
         * @return 
         */
        @Override
	public boolean isListening(IFutureListener<RESULT> listener);
        
        
}
