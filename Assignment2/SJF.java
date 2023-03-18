import java.util.List;
import java.util.stream.Collectors;


public class SJF implements Algorithm {
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    int TurnAroundTime = 0;
    int StartTime = 0;
    int WaitTime = 0;
    int WaitSum = 0;
    int TurnSum = 0;
    float avgWait = 0; 
    float avgTurn = 0; 

    public SJF(List<Task> aTaskList) {
        gTaskList = aTaskList;
        gTaskList = sort();
    }

    public void schedule() {
        // Everything is sorted already, so we bing chilling
        Task lCurrent = null;
        for (int i = 0; i < gTaskList.size(); i++) {
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

    public Task pickNextTask() {
        if (gLocationInQueue >= gTaskList.size()) {
            return null;
        }
        gLocationInQueue += 1;
        return gTaskList.get(gLocationInQueue);
    }

    private List<Task> sort() {
        // Java7cels hate to see a streamchad winning
        List<Task> lSortedList = gTaskList.stream().sorted(new TimeSorter()).collect(Collectors.toList());
        return lSortedList;
    }
}
