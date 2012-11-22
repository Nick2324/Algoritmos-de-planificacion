
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class Process1 extends Process implements Runnable{
    
    private long i;
    
    public Process1(ArrayList<String> parameters){
        super(parameters);
    }
    
    public Process1(int ID, ArrayList<String> parameters){
        super(ID, parameters);
    }
    
    protected int realizarProceso(){
        try{
            int hasta = (int)(Math.random()*100000000 + 100);
            for(i = 0;i < hasta;i++);
            return 0;
        }catch(Exception e){
            return 1;
        }
    }
    
    @Override
    public void run(){
        realizarProceso();
        super.state = PState.TERMINATED;
    }
    
}