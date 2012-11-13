
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
            for(i = 0;i < 10000000;i++);
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