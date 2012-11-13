
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
    
    public int movePriority(ArrayList<Priority> priority){
        int idProcessMoved = -1;
        if(!priority.get(this.priority).emptyQueues() && 
                priority.size() - 1 != this.priority){
            idProcessMoved = priority.get(this.priority).getCurrentID();
            priority.get(this.priority + 1).addProcess(
                priority.get(this.priority).pollProcess());
        }
        return idProcessMoved;
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
