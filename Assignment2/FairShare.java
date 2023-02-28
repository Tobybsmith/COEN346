import java.util.List;
import java.util.ArrayList;

public class FairShare implements Algorithm {
    int gLocationInQueue = -1;
    // 10 per quant same as RR?
    int gTimeQuant = 10;
    int gUserCount = -1;
    List<Task> gTaskList;
    // Same Size!
    float[] gUserList;
    float[] gProcessesPerUser;
    float[] gTimePerUserProcesses;
    List<Integer> gTimeLeft;
    float gQuantSplit = 0.0f;

    public FairShare(List<Task> aTaskList) {
        gTaskList = aTaskList;
        gUserList = new float[11];
        gProcessesPerUser = new float[11];
        gTimePerUserProcesses = new float[11];
        gLocationInQueue = 0;
        for (Task t : gTaskList) {
            gTimeLeft.add(t.getBurst());
        }
    }

    public void schedule() {
        // A queue is first in first out, so we just iterate normally
        Task lCurrent = null;
        for (;;) {
            // Get task, run task, done task
            for (int i = 0; i < gTaskList.size(); i++) {
                // Get count of processes per user
                gUserList[gTaskList.get(i).getPriority()] += 1;
            }

            for (int i = 0; i < 11; i++) {
                if (gUserList[i] != 0) {
                    gUserCount += 1;
                }
            }

            // Split per user
            gQuantSplit = ((float) gTimeQuant) / ((float) gUserCount);

            // Get the count of each user's processes
            for (int i = 0; i < 11; i++) {
                // Each user's index will have the time per process for that user
                gTimePerUserProcesses[i] = gQuantSplit / gUserList[i];
            }
            lCurrent = pickNextTask();
            // Keep a count of each process's time left
            CPU.run(lCurrent, (int) gTimePerUserProcesses[lCurrent.getPriority()]);
            int lTimeLeft = gTimeLeft.get(gLocationInQueue);
            lTimeLeft -= gTimePerUserProcesses[lCurrent.getPriority()];
            if (lTimeLeft <= 0) {
                gTaskList.remove(lCurrent);
                System.out.println("Task " + lCurrent.getName() + " finished.");
            } else {
                gTimeLeft.set(gLocationInQueue,
                        (int) (gTimeLeft.get(gLocationInQueue) - gTimePerUserProcesses[lCurrent.getPriority()]));
            }
            System.out.println();
        }
    }

    public Task pickNextTask() {
        // Toby Smith writes worst code ever
        // Asked to leave COEN346 assignment
        return gTaskList.get(gLocationInQueue);
    }
}
