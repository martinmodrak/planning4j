package cz.cuni.amis.planning4j.sicstus;

import java.util.HashMap;
import org.apache.log4j.Logger;
import se.sics.jasper.SICStus;
import se.sics.jasper.SPException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try {            
            SICStus sp = new SICStus();        
            sp.restore("D:/Martin/Desktop/bordel/relaxPlan.sav");
            HashMap varMap = new HashMap();
            sp.query("tell('out.tmp'),plan,flush_output,told.", varMap);
        } catch (SPException ex) {
            Logger.getLogger(App.class).warn(ex);
        }
    }
}
