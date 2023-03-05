/**
 * Task to be scheduled by the scheduling alogrithm.
 *
 * Each task is represented by
 *
 *  String name - a task name, not necessarily unique
 *
 *  int tid - unique task identifier
 *
 *  int priority - the relative priority of a task where a higher number indicates
 *  higher relative priority.
 *
 *  int burst - the CPU burst of this this task
 */

import java.util.concurrent.atomic.AtomicInteger;

public class Task
{
    // the representation of each task
    private String name;
    private int tid;
    private int priority;
    private int burst;
    private int owner;
    private int burstRemain;
    boolean isDone = false;
    int waitTime = 0;


    /**
     * We use an atomic integer to assign each task a unique task id.
     */
    private static AtomicInteger tidAllocator = new AtomicInteger();

    public Task(String name, int priority, int burst) {
        this.name = name;
        this.priority = priority;
        this.burst = burst;
        this.burstRemain = burst;

        this.tid = tidAllocator.getAndIncrement();
    }

    /**
     * Appropriate getters
     */
    public String getName() {
        return name;
    }

    public int getTid() {
        return tid;
    }

    public int getPriority() {
        return priority;
    }

    public int getBurst() {
        return burst;
    }

    public int getBurstRemain()
    {
        return this.burstRemain;
    }

    /**
     * Appropriate setters
     */
    public int setPriority(int priority) {
        this.priority = priority;

        return priority;
    }
    
    public int setBurst(int burst) {
        this.burst = burst;

        return burst;
    }

    public void setOwner(int o)
    {
        owner = 0;
    }
    
    public int getOwner()
    {
        return owner;
    }

    public void setBurstRemain(int a)
    {
        this.burstRemain = a;
    }

    public int setWaitTime(int wait)
    {
        this.waitTime = wait;
        return this.waitTime;
    }

    /**
     * We override equals() so we can use a
     * Task object in Java collection classes.
     */
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (!(other instanceof Task))
            return false;

        /**
         * Otherwise we are dealing with another Task.
         * two tasks are equal if they have the same tid.
         */
        Task rhs = (Task)other;
        return (this.tid == rhs.tid) ? true : false; //should take out  ? true : false cus return () is already bool
    }

    public String toString() {
        return
            "Name: " + name + "\n" + 
            "Tid: " + tid + "\n" + 
            "Priority: " + priority + "\n" + 
            "Burst: " + burst + "\n";
    }

    //decrement remaining burst
    public void decrementRemainBurst(int remainBurstToSubtract){
        this.burstRemain -= remainBurstToSubtract;
        isDone = this.burstRemain <= 0;
    }
}
