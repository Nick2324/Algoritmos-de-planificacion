
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nicolas
 */
public class Main {
   
    private static Clock clockAdded;
    
    public static void main(String[] args) throws InterruptedException{
        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el nombre del algoritmo que utilizara el "
                + "planeador");*/
        System.out.println("Colas multiples retroalimentadas y"
                + " algoritmo de Peterson");
        Planificador p = new Planificador("CMR");
        p.setName("Planificador");
        p.start();
        /*if(p.getNombreAlgoritmo().equals("SRTF") || 
                p.getNombreAlgoritmo().equals("PNA")){
            int i = 3;
            while(i>0){
                if(clockAdded == null){
                    clockAdded = new Clock((long)(Math.random()*50)+100);
                    clockAdded.beginCount();
                }else if(clockAdded.ended && 
                        !p.getState().equals(Thread.State.TERMINATED)){
                    p.suspend();
                    p.addProcess(new Process1(null));
                    p.resume();
                    i--;
                }
            }
        }*/
    }
    
}