/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nicolas
 */
public interface FlagsManager {
    public abstract void setQuantum(long quantum);
    public abstract void setPriority(int priority);
    public abstract long getQuantum();
    public abstract int getPriority();
    public abstract void generateQuantum();
}