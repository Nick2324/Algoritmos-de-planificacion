import java.util.ArrayList;
import java.util.Scanner;

public class Planificador extends Thread{
    public ArrayList<Priority> priorityQueues;
    public String nombreAlgoritmo;
    public Clock clockProcess;
    public int currentPriority;
    public int nProcess;
    
    public Planificador(String tipoAlgoritmo){
        nombreAlgoritmo = tipoAlgoritmo;
        priorityQueues = new ArrayList<Priority>();
    }
    
    public void run(){
        //Adicion de procesos de acuerdo al algoritmo elegido
        //Si es un algoritmo apropiativo o no apropiativo ordena la
        //lista ready contenida en la clase Priority
        int nProcess = (int) (Math.random()*3) + 2;
        if(nombreAlgoritmo.equals("FCFS")){
            this.currentPriority = 0;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess;i++)
                this.priorityQueues.get(this.currentPriority).addProcess(new Process1(null));
            wraperFCFS();
        }else if(nombreAlgoritmo.equals("RR")){
            this.currentPriority = 0;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess;i++)
                this.priorityQueues.get(this.currentPriority).addProcess(new Process1(null));
            wraperRR();
        }else if(nombreAlgoritmo.equals("CM")){
            this.currentPriority = 4;
            NodeProcess newProcess = null;
            for(int i = 0; i < currentPriority; i++)
                this.priorityQueues.add(new Priority(i));
            for(int i = 0; i < currentPriority; i++){
                nProcess = (int)(Math.random()*((i+1)*2));
                for(int j = 0; j < nProcess; j++){
                    newProcess = new NodeProcess(new Process1(null));
                    newProcess.generateQuantum();
                    newProcess.setPriority(i);
                    newProcess.setID(this.nProcess++);
                    this.priorityQueues.get(i).addProcess(newProcess);
                }
            }
            colasMultiples();
        }else if(nombreAlgoritmo.equals("CMR")){
            this.currentPriority = 4;
            NodeProcess newProcess = null;
            /*
             * Inicializando las prioridades definidas en 4 prioridades
             */
            for(int i = 0; i < currentPriority; i++)
                this.priorityQueues.add(new Priority(i,
                        (long)(Math.random()*100 + 100)));
            /*
             * Inicializando procesos para cada una de las colas de prioridad
             */
            for(int i = 0; i < currentPriority; i++){
                nProcess = (int)(Math.random()*((i+1)*2));
                for(int j = 0; j < nProcess; j++){
                    newProcess = new NodeProcess(new Process1(null));
                    newProcess.generateQuantum();
                    newProcess.setPriority(i);
                    newProcess.setID(this.nProcess++);
                    this.priorityQueues.get(i).addProcess(newProcess);
                }
            }
            /*
             * Iniciando el manejador de movimiento de procesos entre
             * colas. Solo es iniciado para las prioridades diferentes
             * de la máxima.
             */
            for(int i = 0; i < currentPriority - 1; i++)
                this.priorityQueues.get(i).initializeProcMoveManager();
            colasMultiplesRetroalimentado();
        }else if(nombreAlgoritmo.equals("SJF")){
            this.currentPriority = 0;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess;i++){
                this.priorityQueues.get(this.currentPriority).addProcessToHead(new Process1(null)); 
                this.priorityQueues.get(this.currentPriority).generateCurrentQuantum();
                this.priorityQueues.get(this.currentPriority).setCurrentQuantum(
                        this.priorityQueues.get(this.currentPriority).getCurrentQuantum()+100000);
            }
            this.priorityQueues.get(this.currentPriority).sortByQuantum();
            wraperSJF();
        }else if(nombreAlgoritmo.equals("SRTF")){
            this.currentPriority = 0;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess;i++){
                this.priorityQueues.get(this.currentPriority).addProcessToHead(new Process1(null)); 
                this.priorityQueues.get(this.currentPriority).generateCurrentQuantum();
            }
            this.priorityQueues.get(this.currentPriority).sortByQuantum();
            wraperSRTF();
        }else if(nombreAlgoritmo.equals("PNA")){
            this.currentPriority = 0;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess;i++){
                this.priorityQueues.get(this.currentPriority).addProcessToHead(new Process1(null)); 
                this.priorityQueues.get(this.currentPriority).setCurrentPriority(
                        (int)(Math.random()*4));
            }
            this.priorityQueues.get(this.currentPriority).sortByPriority();
            prioridadNoApropiativo();
        }else if(this.nombreAlgoritmo.equals("PA")){
            this.currentPriority = 0;
            NodeProcess newProcess;
            this.priorityQueues.add(new Priority(this.priorityQueues.size()+1));
            for(int i = 0;i < nProcess; i++){
                newProcess = new NodeProcess(new Process1(null));
                newProcess.setPriority((int)(Math.random()*4));
                newProcess.setID(this.nProcess++);
                this.priorityQueues.get(this.currentPriority).addProcess(newProcess);
            }
            this.priorityQueues.get(this.currentPriority).sortByPriority();
            prioridadApropiativo();
        }
        System.out.println("Algoritmo terminado");
    }
    
    public void FCFS(){
        if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.NEW)){
            System.out.println("Iniciando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).startCurrentProcess();
        }else if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.TERMINATED)){
            this.priorityQueues.get(this.currentPriority).endCurrentProcess();
            System.out.println("Terminando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).pollProcess();
        }
    }
    
    public void roundRobin(){
        /*
         * Inicializando relojes de bloqueo y de suspension
         * para aniadir procesos bloqueados y suspendidos
         * a la cola de listo
         */
        if(this.priorityQueues.get(this.currentPriority).getBlockedClock() == null)
            this.priorityQueues.get(this.currentPriority).initializeBlockedClock();
        
        if(this.priorityQueues.get(this.currentPriority).getSuspendedClock() == null)
            this.priorityQueues.get(this.currentPriority).initializeSuspendedClock();
        
        /*
         * Empieza el proceso si esta en estado NUEVO (NEW)
         */
        if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.NEW)){
            if(this.clockProcess != null && this.clockProcess.isAlive())
                this.clockProcess.endCount();
            this.priorityQueues.get(this.currentPriority).generateCurrentQuantum();
            this.clockProcess = new Clock(
                        this.priorityQueues.get(this.currentPriority).getCurrentQuantum());
            System.out.println("Comenzando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID() + " con cuanto "
                    + this.priorityQueues.get(this.currentPriority).getCurrentQuantum());
            this.clockProcess.beginCount();
            this.priorityQueues.get(this.currentPriority).startCurrentProcess(); 
        }else 
            /*
             * Suspende el proceso si el reloj de proceso ha terminado de
             * contar segun el cuanto del proceso en estado critico
             */
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.CRITICAL) && this.clockProcess.ended){
            System.out.println("Suspendiendo proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.clockProcess.endCount();
            this.clockProcess = null;
            this.priorityQueues.get(this.currentPriority).generateCurrentQuantum();
            this.priorityQueues.get(this.currentPriority).suspendCurrentProcess();
        }else 
         /*
          * Si el proceso esta en estado SUSPENDIDO (SUSPENDED) se reinicia
          */
           if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.SUSPENDED)){
            long minorQuantum = 
                    this.priorityQueues.get(this.currentPriority).getCurrentQuantum() % 20;
            this.priorityQueues.get(this.currentPriority).generateCurrentQuantum();
            this.priorityQueues.get(this.currentPriority).setCurrentQuantum(
                    this.priorityQueues.get(this.currentPriority).getCurrentQuantum() - 
                    minorQuantum);
            System.out.println("Reanudando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID()+ " con cuanto "
                    + this.priorityQueues.get(this.currentPriority).getCurrentQuantum());
            if(this.clockProcess != null && this.clockProcess.isAlive())
                this.clockProcess.endCount();
            this.clockProcess = new Clock(
                        this.priorityQueues.get(this.currentPriority).getCurrentQuantum());
            this.clockProcess.beginCount();
            this.priorityQueues.get(this.currentPriority).resumeCurrentProcess();
        }else 
        /*
         * Se bloquea el proceso actual segun un numero aleatorio.
         */
         if(Math.random() == 0.5){
            System.out.println("Bloqueando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).blockCurrentProcess();
        }else 
        /*
         * Desbloquea el proceso para poder ejecutarlo
         */
         if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.BLOCKED)){
            System.out.println("Desbloqueando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).unblockCurrentProcess();
        }else 
        /*
         * Desencola si el proceso ya ha sido terminado
         */  
        if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.TERMINATED)){
            this.priorityQueues.get(this.currentPriority).endCurrentProcess();
            System.out.println("Terminando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).pollProcess();
            this.clockProcess.endCount();
        }else 
        /*
         * Soluciona el problema de que el proceso este en critico
         * cuando el hilo que representa el proceso esta en estado
         * TERMINATED (TERMINADO)
         */    
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.CRITICAL) && 
                this.priorityQueues.get(this.currentPriority).getState().equals(
                Thread.State.TERMINATED)){
                this.priorityQueues.get(this.currentPriority).endCurrentProcess();
            this.priorityQueues.get(this.currentPriority).pollProcess();
        }
        /*
         * Encola procesos suspendidos despues de que el reloj de
         * suspendidos termine de contar
         */
        if(this.priorityQueues.get(this.currentPriority).getSuspendedClock().ended){
            this.priorityQueues.get(this.currentPriority).offerSuspendedProcess();
            this.priorityQueues.get(this.currentPriority).endSuspendedClock();
            this.priorityQueues.get(this.currentPriority).setSuspendedClock(null);
        }
        /*
         * Encola procesos bloqueados cuando haya recursos, los cuales
         * son emulados con el reloj de bloqueados: despues de que el 
         * reloj de bloqueados termine de contar
         */
        if(this.priorityQueues.get(this.currentPriority).getBlockedClock().ended){
            this.priorityQueues.get(this.currentPriority).offerBlockedProcess();
            this.priorityQueues.get(this.currentPriority).endBlockedClock();
            this.priorityQueues.get(this.currentPriority).setBlockedClock(null);
        }
    }
    
    public void srtf(){
        /*
         * Inicia el proceso si este esta en estado NEW (NUEVO)
         */
        if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.NEW)){
            System.out.println("Iniciando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).startCurrentProcess();
        }
        /*
         * Desencola el proceso cuando este haya acabado.
         */
        else{
            this.priorityQueues.get(this.currentPriority).endCurrentProcess();
            System.out.println("Terminando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).pollProcess();
        }
    }
    
    public void sjf(){
        /*
         * Inicia el proceso si el proceso en cabeza está en estado
         * NEW (NUEVO)
         */
        if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.NEW) ||
                this.priorityQueues.get(this.currentPriority).getState().equals(Thread.State.NEW)){
            this.priorityQueues.get(this.currentPriority).startCurrentProcess();
            System.out.println("Iniciando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
        }
        /*
         * Reanuda un proceso si está en estado suspendido
         */
        else if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.SUSPENDED)){
            System.out.println("Reiniciando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).resumeCurrentProcess();
        }
        /*
         * Resta el cuanto del proceso si este esta en estado CRITICAL
         * (CRITICO)
         */
        else if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                Process.PState.CRITICAL)){
                this.priorityQueues.get(this.currentPriority).setCurrentQuantum(
                        this.priorityQueues.get(this.currentPriority).getCurrentQuantum()-1);
        }
        /*
         * Desencola el proceso en cabeza si este ya ha acabado
         */
        else{
            this.priorityQueues.get(this.currentPriority).endCurrentProcess();
            System.out.println("Terminando proceso "+
                    this.priorityQueues.get(this.currentPriority).getCurrentID());
            this.priorityQueues.get(this.currentPriority).pollProcess();
        }
    }
    
    public void prioridadNoApropiativo(){
        //Mientras hayan procesos en cola
        while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
            /*
             * Inicia el proceso en cabeza si está en estado NEW (NUEVO) 
             */
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.NEW)){
                System.out.println("Iniciando proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
                this.priorityQueues.get(this.currentPriority).startCurrentProcess();
            }
            /*
             * Desencola el proceso cuando ha acabado
             */
            else{
                this.priorityQueues.get(this.currentPriority).endCurrentProcess();
                System.out.println("Terminando proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
                this.priorityQueues.get(this.currentPriority).pollProcess();
            }
        }
    }
    
    public void prioridadApropiativo(){
        while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.NEW) || 
                this.priorityQueues.get(this.currentPriority).getState().equals(Thread.State.NEW)){
                System.out.println("Iniciando proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
                this.priorityQueues.get(this.currentPriority).startCurrentProcess();
            }else if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.SUSPENDED)){
                System.out.println("Reiniciando proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
                this.priorityQueues.get(this.currentPriority).resumeCurrentProcess();
            }else if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.TERMINATED)){
                this.priorityQueues.get(this.currentPriority).endCurrentProcess();
                System.out.println("Terminando proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
                this.priorityQueues.get(this.currentPriority).pollProcess();
            }
        }
    }
    
    public void colasMultiples(){
        boolean terminated = false;
        /*
         * Mientras no se hayan atendido todos los procesos
         * se ejecuta el algoritmo.
         */
        while(!terminated){
            int i = this.priorityQueues.size()-1;
            /*
             * Busca cual es la cola de prioridad mayor que 
             * contiene procesos.
             */
            do{
                this.currentPriority = i--;
            }while(i >= 0 && this.priorityQueues.get(
                    this.currentPriority).emptyQueues());
            /*
             * Comprueba si las colas están totalmente vacias.
             */
            if(this.priorityQueues.get(this.currentPriority).emptyQueues())
                terminated = true;
            /*
             * Si aún quedan procesos por ejecutar, ejecuta los procesos
             * de la cola de prioridad actual con el algoritmo especificado.
             */
            if(!terminated){
                if(this.currentPriority == 0){
                    System.out.println("Iniciando FCFS en cola "+
                            this.currentPriority);
                    wraperFCFS();
                }else if(this.currentPriority == this.priorityQueues.size() - 1){
                    System.out.println("Iniciando STRF en cola "+
                            this.currentPriority);
                    wraperSRTF();
                }else{
                    System.out.println("Iniciando Round Robin en cola "+
                            this.currentPriority);
                    wraperRR();
                }
            }else
                System.out.println("Listas de todas las colas vacias");
        }
    }
    
    public void colasMultiplesRetroalimentado(){
        boolean terminated = false;
        /*
         * Mientras no se hayan acabado todos los procesos en las colas
         * de prioridad se ejecuta el algoritmo
         */
        while(!terminated){
            int i = this.priorityQueues.size() - 1;   
            /*
             * Buscando la primera prioridad que tenga un proceso en cola
             */
            do{
                this.currentPriority = i--;
            }while(i >= 0 && this.priorityQueues.get(
                    this.currentPriority).emptyQueues());
            if(this.priorityQueues.get(this.currentPriority).emptyQueues())
                terminated = true;
            /*
             * Ejecutando el algoritmo correspondiente segun la cola 
             * de prioridad
             */
            if(!terminated){
                while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
                    if(this.currentPriority == this.priorityQueues.size() - 1)
                        roundRobin();
                    else
                        FCFS();
                    for(i = 0; i < this.priorityQueues.size() - 1; i++)
                        try{
                            if(this.priorityQueues.get(i).getProcMoveManagerState()){
                                int idProcessMoved = this.priorityQueues.get(i).moveProcess(
                                    this.priorityQueues);
                                if(idProcessMoved != -1)
                                    System.out.println("Moviendo el proceso" + 
                                    idProcessMoved + " de la cola de prioridad "+
                                    i + " a la" + (i + 1));
                                this.priorityQueues.get(i).initializeProcMoveManager();
                            }
                        }catch(Exception e){
                            System.out.println(e.toString());
                        }
                }
            }else
                System.out.println("Listas de todas las colas vacias");
        }
        for(int i = 0; i < this.priorityQueues.size(); i++){
            this.priorityQueues.get(i).endProcMoveManager();
            this.priorityQueues.get(i).endBlockedClock();
            this.priorityQueues.get(i).endSuspendedClock();
        }
        if(this.clockProcess != null)
            this.clockProcess.endCount();
    }
    
    public void addProcess(Process process){
        /*
         * Maneja la adicion de procesos si el algoritmo es SRTF:
         * Si hay un proceso ejecutandose y el cuanto del proceso
         * nuevo es menor que el del actual, suspende el proceso
         * actual y pone en cabeza el proceso nuevo. Si no es asi,
         * la adicion del proceso se hace, se ordena la lista de
         * procesos segun el cuanto de los procesos y el proceso 
         * actual continua en cabeza.
         */
        NodeProcess newProcess = new NodeProcess(process);
        if(this.nombreAlgoritmo.equals("SJF")){
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.CRITICAL))
                this.priorityQueues.get(this.currentPriority).suspendProcess();
            System.out.println("Añadiendo proceso");
            newProcess.generateQuantum();
            newProcess.setQuantum(newProcess.getQuantum()+1000);
            newProcess.setID(this.nProcess++);
            newProcess.setPriority(this.currentPriority);
            if(newProcess.getQuantum() >
                    this.priorityQueues.get(this.currentPriority).getCurrentQuantum())
                if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                        Process.PState.SUSPENDED))
                    System.out.println("Suspendiendo proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
            else
                this.priorityQueues.get(this.currentPriority).resumeCurrentProcess();
            this.priorityQueues.get(this.currentPriority).addProcess(newProcess);
            this.priorityQueues.get(this.currentPriority).sortByQuantum();
            System.out.println("Proceso añadido");
        }else if(this.nombreAlgoritmo.equals("PA")){
            if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                    Process.PState.CRITICAL))
                this.priorityQueues.get(this.currentPriority).suspendProcess();
            System.out.println("Añadiendo proceso");
            newProcess.setPriority((int)(Math.random()*4));
            newProcess.setID(this.nProcess++);
            if(this.priorityQueues.get(this.currentPriority).getCurrentPriority()
                    < newProcess.getPriority()){
                if(this.priorityQueues.get(this.currentPriority).getCurrentPState().equals(
                        Process.PState.SUSPENDED))
                    System.out.println("Suspendiendo proceso "+
                        this.priorityQueues.get(this.currentPriority).getCurrentID());
            }else
                this.priorityQueues.get(this.currentPriority).resumeCurrentProcess();
            this.priorityQueues.get(this.currentPriority).addProcess(newProcess);
            System.out.println("Proceso añadido");
            this.priorityQueues.get(this.currentPriority).sortByPriority();
        }else if(this.nombreAlgoritmo.equals("RR") || 
                this.nombreAlgoritmo.equals("FCFS")){
            newProcess.setID(this.nProcess);
            newProcess.generateQuantum();
            newProcess.setPriority(this.currentPriority);
            this.priorityQueues.get(this.currentPriority).addProcess(newProcess);
        }
    }
    
    public void wraperFCFS(){
        //Mientras hayan proceso en cola
        while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
            FCFS();
        }
    }
    
    public void wraperRR(){
         //Mientas que no se acaben los procesos
            while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
                roundRobin();
            }
             /*
         * Termina los hilos de los relojes
         */
        if(this.priorityQueues.get(this.currentPriority).getBlockedClock() != null)
            this.priorityQueues.get(this.currentPriority).getBlockedClock().endCount();
        if(this.priorityQueues.get(this.currentPriority).getSuspendedClock() != null)
            this.priorityQueues.get(this.currentPriority).getSuspendedClock().endCount();
        if(this.clockProcess != null)
            this.clockProcess.endCount();
    }
    
    public void wraperSJF(){
        //Mientras hayan proceso en cola
        while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
            sjf();
        }
    }
    
    public void wraperSRTF(){
        //Mientras hayan proceso en cola
        while(!this.priorityQueues.get(this.currentPriority).emptyQueues()){
            srtf();
        }
    }
    
    public String getNombreAlgoritmo() {
        return nombreAlgoritmo;
    }
    
}