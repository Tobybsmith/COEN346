import java.util.List;

public class FCFS implements Algorithm
{
    //Ugh
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    public FCFS(List<Task> aTaskList)
    {
        gTaskList = aTaskList;
    }
    
    public void schedule()
    {
        //A queue is first in first out, so we just iterate normally
        Task lCurrent = null;
        for(int i = 0; i < gTaskList.size(); i++)
        {
            //Get task, run task, done task
            lCurrent = pickNextTask();
            CPU.run(lCurrent, lCurrent.getBurst());
            System.out.println("Task " + lCurrent.getName() + " finished.");
            System.out.println();
        }
    }
    
    public Task pickNextTask()
    {
        if (gLocationInQueue >= gTaskList.size())
        {
            return null;
        }
        gLocationInQueue += 1;
        return gTaskList.get(gLocationInQueue);
    }
}
