import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SJF implements Algorithm
{
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    public SJF(List<Task> aTaskList)
    {
        gTaskList = aTaskList;
        gTaskList = sort();
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
    
    private List<Task> sort()
    {
        //Java7cels hate to see a streamchad winning
        List<Task> lSortedList = gTaskList.stream().sorted(new TimeSorter()).collect(Collectors.toList());
        return lSortedList;
    }
}
