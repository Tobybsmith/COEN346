import java.util.List;
import java.util.Collections;

public class Priority implements Algorithm
{
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    int TurnAroundTime = 0;
    int StartTime = 0;
    int WaitTime = 0;
    int WaitSum = 0;
    int avgWait = 0; 
    int avgTurn = 0; 

    public Priority(List<Task> aTaskList)
    {
        gTaskList = aTaskList;
        gTaskList.sort(new PrioritySorter());
        Collections.reverse(gTaskList);
    }
    
    public void schedule()
    {
        //Everything is sorted already, so we bing chilling
        Task lCurrent = null;
        for(int i = 0; i < gTaskList.size(); i++)
        {
            //Get task, run task, done task
            lCurrent = pickNextTask();
            CPU.run(lCurrent, lCurrent.getBurst());
            TurnAroundTime = lCurrent.getBurst() - StartTime;
            WaitTime = StartTime;
            StartTime += TurnAroundTime;
            WaitSum += WaitTime;
            System.out.println("Task " + lCurrent.getName() + " finished.");
        }

        avgWait = WaitSum/gTaskList.size();
        avgTurn = StartTime/gTaskList.size();
        System.out.println("The Turnaroudn time average is " + avgTurn);
        System.out.println("The wait time average is " + avgWait);
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
