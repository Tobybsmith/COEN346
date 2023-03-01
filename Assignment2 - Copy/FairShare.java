import java.util.List;
import java.util.ArrayList;

public class FairShare implements Algorithm {
    int gLocationInQueue = 0;
    // 10 per quant same as RR?
    int gTimeQuant = 12;
    int gUserCount = -1;
    List<Task> gTaskList;
    List<Integer> gUsers;
    Task[] gTasks;
    int[] gTimeRemain;
    int[] gTimeAlloc;
    int gQuantSplit = 0;

    public FairShare(List<Task> aTaskList) {
        gTaskList = aTaskList;
        gLocationInQueue = 0;
        gUsers = new ArrayList<Integer>();
        // I hate linked lists >:(
        gTasks = new Task[gTaskList.size()];
        gTimeRemain = new int[gTaskList.size()];
        for (int i = 0; i < gTaskList.size(); i++) {
            gTasks[i] = gTaskList.get(i);
            gTimeRemain[i] = gTaskList.get(i).getBurst();
        }
    }

    public void schedule() {
        // A queue is first in first out, so we just iterate normally
        Task lCurrent = pickNextTask();
        for (;;) {
            // Get the number of unique users
            for (int i = 0; i < gTaskList.size(); i++) {
                if (!gUsers.contains(gTasks[i].getPriority())) {
                    gUsers.add(gTaskList.get(i).getPriority());
                }
            }
            int lHigh = 0;
            for (int i = 0; i < gTaskList.size(); i++) {
                if (gTaskList.get(i).getPriority() > lHigh) {
                    lHigh = gTaskList.get(i).getPriority();
                }
            }
            gUserCount = gUsers.size();
            // Will contain each user's priority
            gTimeAlloc = new int[lHigh];
            // Split per user
            gQuantSplit = (gTimeQuant) / (gUserCount);

            for (int i = 0; i < gTimeAlloc.length; i++)
            {
                //Before this, gTimeAlloc holds the process count for each user
                //After this it holds the time allocation per process
                if (gTimeAlloc[i] > 0)
                    gTimeAlloc[i] = gTimeQuant / gTimeAlloc[i];
            }

            //Get the time for this task:
            int lTime = gTimeAlloc[lCurrent.getPriority()];
            int lBR = lCurrent.getBurstRemain();

            if (lBR > lTime)
            {
                lCurrent.setBurstRemain(lBR - lTime);
                CPU.run(lCurrent, lTime);
            }
            else if (lBR - lTime >= 0)
            {
                lCurrent.setBurstRemain(0);
                CPU.run(lCurrent, lCurrent.getBurstRemain());
                System.out.println("Finished Process: " + lCurrent.getName() + ".");
                gTaskList.remove(lCurrent);
            }
            else
            {
                //Process is finished.
            }
            
            boolean lDone = true;
            for (int i = 0; i < gTaskList.size(); i++)
            {
                if (gTaskList.get(i).getBurstRemain() != 0)
                {
                    lDone = false;
                }
            }
            
            if (lDone == true)
            {
                return;
            }
            gLocationInQueue += 1;
            gLocationInQueue %= gTaskList.size();
        }
    }

    public Task pickNextTask() {
        return gTaskList.get(gLocationInQueue);
    }
}
