
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author nicolas
 */

public class Priority {
    
    private int priority;
    private long quantum;
    private Clock suspendedClock;
    private Clock blockedClock;
    private ProcMoveManager moveManager;
    private ArrayList<NodeProcess> ready;
    private ArrayList<NodeProcess> suspended;
    private ArrayList<NodeProcess> blocked;
    
    public Priority(int priority){
        this.priority = priority;
        this.ready = new ArrayList<NodeProcess>();
        this.suspended = new ArrayList<NodeProcess>();
        this.blocked = new ArrayList<NodeProcess>();
    }
    
    public Priority(int priority, long quantum){
        this.priority = priority;
        this.quantum = quantum;
        this.ready = new ArrayList<NodeProcess>();
        this.suspended = new ArrayList<NodeProcess>();
        this.blocked = new ArrayList<NodeProcess>();
    }
    
    public void addProcess(Process process){
        NodeProcess nuevo = new NodeProcess(process);
        nuevo.setPriority(this.priority);
        nuevo.getProcess().setID(this.ready.size() + 
                this.suspended.size() + this.blocked.size());
        this.ready.add(nuevo);
    }
    
    public void addProcess(Process process, int ID){
        NodeProcess nuevo = new NodeProcess(process);
        nuevo.getProcess().setID(ID);
        this.ready.add(nuevo);
    }
    
    public void addProcess(Process process, long priority){
        NodeProcess nuevo = new NodeProcess(process);
        nuevo.getProcess().setPriority((int)(priority));
        this.ready.add(nuevo);
    }
    
    public void addProcess(Process process, int ID, int priority){
        NodeProcess nuevo = new NodeProcess(process);
        nuevo.getProcess().setID(ID);
        nuevo.getProcess().setPriority(priority);
        this.ready.add(nuevo);
    }
    
    public void addProcess(NodeProcess process){
        this.ready.add(process);
    }
    
    public void addProcessToHead(Process process){
        NodeProcess nuevo = new NodeProcess(process);
        nuevo.setPriority(this.priority);
        nuevo.getProcess().setID(this.ready.size() + 
                this.suspended.size() + this.blocked.size());
        if(this.ready.size() == 0)
            this.ready.add(nuevo);
        else
            this.ready.add(0,nuevo);
    }
    
    public void startCurrentProcess(){
        this.ready.get(0).getProcess().startProcess();
    }
    
    public void suspendProcess(){
        this.ready.get(0).getProcess().suspendProcess();
    }
    
    public void suspendCurrentProcess(){
        this.ready.get(0).getProcess().suspendProcess();
        this.suspended.add(ready.remove(0));
    }
    
    public void blockCurrentProcess(){
        this.ready.get(0).getProcess().blockProcess();
        this.blocked.add(this.ready.get(0));
    }
    
    public void unblockCurrentProcess(){
        this.ready.get(0).getProcess().resume();
    }
    
    public void resumeCurrentProcess(){
       this.ready.get(0).getProcess().resumeProcess();
    }
    
    public void endCurrentProcess(){
       this.ready.get(0).getProcess().endProcess(); 
    }
    
    public void offerSuspendedProcess(){
        if(this.suspended.size() > 0)
            this.ready.add(this.suspended.remove(0));
    }
    
    public void offerBlockedProcess(){
        if(this.blocked.size() > 0)
            this.ready.add(this.blocked.remove(0));
    }
    
    public NodeProcess pollProcess(){
        return this.ready.remove(0);
    }
    
    public void toHead(int index){
        if(this.ready.size() > 1)
            this.ready.add(0,this.ready.remove(index));
    }
    
    public void toLastPosition(){
        this.ready.add(this.ready.remove(0));
    }
    
    public void sortByPriority(){
        for(int i = 1; i < this.ready.size(); i++)
            for(int j = 0; j < this.ready.size() - i; j++)
                if(this.ready.get(j).getPriority() < 
                        this.ready.get(j+1).getPriority()){
                    this.ready.add(j,this.ready.get(j+1));
                    this.ready.remove(j+2);
                }
    }
    
    public void sortByQuantum(){
        for(int i = 1; i < this.ready.size(); i++)
            for(int j = 0; j < this.ready.size() - i; j++)
                if(this.ready.get(j).getQuantum() > 
                        this.ready.get(j+1).getQuantum()){
                    this.ready.add(j,this.ready.get(j+1));
                    this.ready.remove(j+2);
                }
    }
    
    public Thread.State getState(){
        return this.ready.get(0).getProcess().getState();
    }
    
    public Process.PState getCurrentPState(){
        if(ready.size() == 0){
            if(suspended.size() > 0)
                while(suspended.size() > 0)
                    ready.add(suspended.remove(0));
            else if(blocked.size() > 0)
                while(blocked.size() > 0)
                    ready.add(blocked.remove(0));
            else
                return Process.PState.TERMINATED;
        }
        return this.ready.get(0).getProcess().getPState();
    }
    
    public void setCurrentQuantum(long quantum){
       this.ready.get(0).setQuantum(quantum);
    }

    public void setCurrentPriority(int priority) {
        this.ready.get(0).setPriority(priority);
    }

    public void setQuantum(long quantum) {
        this.quantum = quantum;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
    public long getQuantum() {
        return quantum;
    }
    
    public long getCurrentQuantum(){
        return this.ready.get(0).getQuantum();
    }
    
    public int getCurrentPriority(){
        return this.ready.get(0).getPriority();
    }
    
    public int getCurrentID(){
        return this.ready.get(0).getProcess().getID();
    }
    
    public int getCurrentSuspendedID(){
        if(this.suspended.size() > 0)
            return this.suspended.get(0).getID();
        else
            return -1;
    }
    
    public int getCurrentBlockedID(){
        if(this.blocked.size() > 0)
            return this.blocked.get(0).getID();
        else
            return -1;
    }
    
    public void generateCurrentQuantum(){
        this.ready.get(0).generateQuantum();
    }
    
    public void generatePriorityQuantum(){
        this.quantum = (long)(Math.random()*100) + 100;
    }
    
    public boolean emptyQueues(){
        return this.ready.isEmpty() && this.suspended.isEmpty()
                && this.blocked.isEmpty();
    }
    
    public int getNumberProcess(){
        return ready.size() + suspended.size() + blocked.size();
    }
    
    public void initializeSuspendedClock(){
        try{
            this.suspendedClock = new Clock((long)
                    (Math.random()*25/this.suspended.size()));
            this.suspendedClock.setName("Suspended Clock");
            this.suspendedClock.beginCount();
        }catch(Exception e){
            this.suspendedClock = new Clock(50);
        }
    }
    
    public void initializeBlockedClock(){
        try{
            this.blockedClock = new Clock((long)
                    (Math.random()*25/this.blocked.size()));
            this.blockedClock.setName("Blocked Clock");
            this.blockedClock.beginCount();
        }catch(Exception e){
            this.blockedClock = new Clock(50);
        }
    }
    
    public void initializeProcMoveManager(){
        this.moveManager = new ProcMoveManager(this.priority,this.quantum);
        this.moveManager.beginCount();
    }
    
    public void endProcMoveManager(){
        try{
            this.moveManager.endCount();
        }catch(NullPointerException e){}
    }
    
    public void endSuspendedClock(){
        try{
            this.suspendedClock.endCount();
        }catch(NullPointerException e){}
    }
    
    public void endBlockedClock(){
        try{
            this.blockedClock.endCount();
        }catch(NullPointerException e){}
    }
    
    public boolean getProcMoveManagerState() throws Exception{
        return this.moveManager.getClockState();
    }
    
    public int moveProcess(ArrayList<Priority> priority){
        return this.moveManager.movePriority(priority);
    }
    
    public void setSuspendedClock(Clock suspendedClock){
        this.suspendedClock = suspendedClock;
    }
    
    public void setBlockedClock(Clock blockedClock){
        this.blockedClock = blockedClock;
    }
    
    public Clock getSuspendedClock(){
        return this.suspendedClock;
    }
    
    public Clock getBlockedClock(){
        return this.blockedClock;
    }
    
}
