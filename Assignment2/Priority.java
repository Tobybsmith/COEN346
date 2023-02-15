import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;

public class Priority implements Algorithm
{
    int gLocationInQueue = -1;
    List<Task> gTaskList;
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
            System.out.println("Task " + lCurrent.getName() + " finished.");
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
