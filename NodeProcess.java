import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class NodeProcess implements FlagsManager {
    private Process process;
    private ArrayList<Long> flags;
    private long quantum;
    private int priority;
    
    public NodeProcess(Process process){
        this.process = process;
        flags = new ArrayList<Long>();
    }
    
    public Process getProcess(){
        return this.process;
    }
    
    public ArrayList<Long> getFlags(){
        return flags;
    }

    public void setQuantum(long quantum) {
        this.quantum = quantum;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public void setID(int ID){
        this.process.setID(ID);
    }
    
    public int getID(){
        return this.process.getID();
    }
    
    public long getQuantum() {
        return this.quantum;
    }

    public int getPriority() {
        return this.priority;
    }

    public void generateQuantum() {
        this.quantum = (long) (Math.random()*100) + 50;
    }
    
}