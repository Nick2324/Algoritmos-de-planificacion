
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nicolas
 */
public class Clock extends Thread{
    public boolean ended;
    private long count;
    
    public Clock(long count){
        this.count = count;
        this.ended = false;
    }
    
    @Override
    public void run(){
        try {
            sleep(this.count);
            this.ended = true;
        } catch (InterruptedException ex) {
            Logger.getLogger(Clock.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void beginCount(){
        start();
    }
    
    public void endCount(){
        stop();
    }
}