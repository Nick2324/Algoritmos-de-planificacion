
import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nicolas
 */
public class ProcMoveManager {
    private int priority;
    private long quantum;
    private Clock moveProcess;
    
    public ProcMoveManager(int priority, long quantum){
        this.priority = priority;
        this.quantum = quantum;
    }
    
    public void movePriority(ArrayList<Priority> priority){
        if(!priority.get(this.priority).emptyQueues() && 
                priority.size() - 1 == this.priority)
            priority.get(this.priority + 1).addProcess(
                priority.get(this.priority).pollProcess());
    }
    
    public void beginCount(){
        this.moveProcess = null;
        this.moveProcess = new Clock(this.quantum);
        this.moveProcess.beginCount();
    }
    
    public void endCount(){
        if(this.moveProcess != null)
            this.moveProcess.endCount();
    }
    
    public boolean getClockState() throws Exception{
        if(this.moveProcess != null)
            return this.moveProcess.ended;
        else throw new Exception();
    }
}