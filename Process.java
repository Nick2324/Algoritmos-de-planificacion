
import java.util.ArrayList;

public abstract class Process extends Thread{
    static enum PState{NEW, CRITICAL, SUSPENDED, BLOCKED, TERMINATED};
    
    protected int ID;
    protected PState state;
    protected ArrayList<String> parametros;
    
    public Process(ArrayList<String> parametros){
        this.state = PState.NEW;
        this.parametros = parametros;
    }
    
    public Process(int ID,ArrayList<String> parametros){
        this.ID = ID;
        super.setName("Process " + this.ID);
        this.state = PState.NEW;
    }
    
    protected abstract int realizarProceso();
    
    public void startProcess(){
        start();
        this.state = PState.CRITICAL;
    }
    
    public void suspendProcess(){
        suspend();
        this.state = PState.SUSPENDED;
    }
    
    public void resumeProcess(){
        this.state = PState.CRITICAL;
        resume();
    }
    
    public void blockProcess(){
        this.state = PState.BLOCKED;
    }
    
    public void unblockProcess(){
        this.state = PState.CRITICAL;
    }
    
    public void endProcess(){
        stop();
        this.state = PState.TERMINATED;
    }
    
    public PState getPState(){
        return this.state;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    
    public int getID(){
        return this.ID;
    }
    
}