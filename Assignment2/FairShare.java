import java.util.List;
import java.util.ArrayList;

public class FairShare implements Algorithm
{
    int gLocationInQueue = -1;
    //10 per quant same as RR?
    int gTimeQuant = 10;
    List<Task> gTaskList;
    List<Integer> gUserList;
    float gQuantSplit = 0.0f;
    public FairShare(List<Task> aTaskList)
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
        //Get the quant
        for (int i = 0; i < gTaskList.size(); i++)
        {
            //We need to get the number of unique priorities
            //YOU'RE ALLOWED TO DELETE BRUH NO CAP FR ON G-D
            //amen.
            if (!gUserList.contains(gTaskList.get(gLocationInQueue).getPriority()))
            {
                gUserList.add(gTaskList.get(gLocationInQueue).getPriority());
            }
        }
        gQuantSplit = (1.0f / ((float)gUserList.size())) * gTimeQuant;
        //Now we have the time per user to assign tasks
        return gTaskList.get(gLocationInQueue);
    }
}
