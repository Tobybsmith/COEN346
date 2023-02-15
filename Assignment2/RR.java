import java.util.List;
import java.util.ArrayList;

public class RR implements Algorithm
{
    int gLocationInQueue = 0;
    List<Task> gTaskList;

    List<Integer> gTimeList;
    int gCompletionCounter = 0;
    boolean gTermFlag = false;
    public RR(List<Task> aTaskList)
    {
        gTaskList = aTaskList;
        gTimeList = new ArrayList<Integer>();
        for (int i = 0; i < gTaskList.size(); i++)
        {
            gTimeList.add(gTaskList.get(i).getBurst());
        }
    }
    
    public void schedule()
    {
        //A queue is first in first out, so we just iterate normally
        Task lCurrent = null;
        while (true)
        {
            lCurrent = pickNextTask();
            //Juicy lil trick to keep a number in a range with no conditional logic
            gLocationInQueue += 1;
            gLocationInQueue %= gTaskList.size();
            //Break once everything is done
            if (gCompletionCounter == gTaskList.size())
            {
                break;
            }
            
            if (lCurrent == null)
            {
                continue;
            }
            //Run here
            CPU.run(lCurrent, 10);
            if (gTermFlag)
            {
                System.out.println("Task " + lCurrent.getName() + " finished.");
                System.out.println();
            }
            gTermFlag = false;
        }
    }
    
    public Task pickNextTask()
    {
        //Check the current element's remaining time:
        int lTime = gTimeList.get(gLocationInQueue);
        if (lTime <= 0)
        {
            //task has already been completed, so we don't care
            //Why not just delete the task from the list? I'm a dummy
            gCompletionCounter += 1;
            return null;
        }
        else if (lTime <= 10 && lTime > 0)
        {
            //At most, one cycle left - we have to print "Task x has finished" POST CPU.run() call, ick
            gTermFlag = true;
            gTimeList.set(gLocationInQueue, lTime - 10);
            gCompletionCounter = 0;
            return gTaskList.get(gLocationInQueue);
        }
        else
        {
            //There's more than one cycle left - so we don't care about printing "Task x has finished"
            gTimeList.set(gLocationInQueue, lTime - 10);
            gCompletionCounter = 0;
            return gTaskList.get(gLocationInQueue);
        }
    }
}
