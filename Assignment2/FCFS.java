import java.util.List;
import java.util.ArrayList;

public class FCFS implements Algorithm {
    // Ugh
    int gLocationInQueue = -1;
    List<Task> gTaskList;
    public int StartTime = 0;
    public int TurnAround = 0;
    public int avgTurn = 0;
    public int TurnSum = 0;
    public int WaitSum = 0;
    public int avgWait = 0;
    ArrayList<Integer> TurnList = new ArrayList<Integer>();
    ArrayList<Integer> WaitList = new ArrayList<Integer>();

    public FCFS(List<Task> aTaskList) {
        gTaskList = aTaskList;
    }

    public void schedule() {
        // A queue is first in first out, so we just iterate normally
        Task lCurrent = null;
        for (int i = 0; i < gTaskList.size(); i++) {
            // Get task, run task, done task
            lCurrent = pickNextTask();
            CPU.run(lCurrent, lCurrent.getBurst());
            TurnAround = lCurrent.getBurst() - StartTime;
            StartTime += TurnAround;
            TurnSum += TurnAround;
            WaitSum += StartTime;
            TurnList.add(TurnAround);
            WaitList.add(StartTime);
            System.out.println("Task " + lCurrent.getName() + " finished.");
            System.out.println("Turnaround Time = " + TurnAround);
            System.out.println("Wait Time = " + StartTime);
            System.out.println();
        }

        avgTurn = TurnSum / gTaskList.size();
        avgWait = WaitSum / gTaskList.size();
        System.out.println("Average Turnaround Time = " + avgTurn);
        System.out.println("Average Wait Time =" + avgWait);
        System.out.println();
    }

    public Task pickNextTask() {
        if (gLocationInQueue >= gTaskList.size()) {
            return null;
        }
        gLocationInQueue += 1;
        return gTaskList.get(gLocationInQueue);
    }
}
