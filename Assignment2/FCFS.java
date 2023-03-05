import java.util.List;

public class FCFS implements Algorithm
{
    //Ugh
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    int TurnAroundTime = 0;
    int StartTime = 0;
    int WaitTime = 0;
    int WaitSum = 0;
    int TurnSum = 0;
    float avgWait = 0; 
    float avgTurn = 0; 

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
            // Get task, run task, done task
            lCurrent = pickNextTask();
            CPU.run(lCurrent, lCurrent.getBurst());
            TurnAroundTime += lCurrent.getBurst();
            WaitTime = StartTime;
            StartTime += lCurrent.getBurst();
            WaitSum += WaitTime;
            TurnSum += TurnAroundTime;
            System.out.println("Task " + lCurrent.getName() + " finished.");
            System.out.println();
        }
        
        avgWait = ((float) WaitSum) / gTaskList.size();
        avgTurn = ((float)TurnSum) / gTaskList.size();
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
