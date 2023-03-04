import java.util.List;
import java.util.ArrayList;

public class RR implements Algorithm {
    int gLocationInQueue = 0;
    List<Task> gTaskList;

    List<Integer> gTimeList;
    int gCompletionCounter = 0;
    boolean gTermFlag = false;
    int TurnAroundTime = 0;
    int TimeLeft = 0;
    int StartTime = 0;
    int WaitTime = 0;
    int WaitSum = 0;
    int TurnSum = 0;
    int avgWait = 0;
    int avgTurn = 0;
    List<Integer> TurnAroundTimeList;
    List<Integer> WaitList;

    public RR(List<Task> aTaskList) {
        gTaskList = aTaskList;
        gTimeList = new ArrayList<Integer>();
        for (int i = 0; i < gTaskList.size(); i++) {
            gTimeList.add(gTaskList.get(i).getBurst());
        }
    }

    public void schedule() {
        // A queue is first in first out, so we just iterate normally
        Task lCurrent = null;
        while (true) {
            lCurrent = pickNextTask();
            // Juicy lil trick to keep a number in a range with no conditional logic
            gLocationInQueue += 1;
            gLocationInQueue %= gTaskList.size();
            // Break once everything is done
            if (gCompletionCounter == gTaskList.size()) {
                break;
            }

            if (lCurrent == null) {
                continue;
            }
            // Run here
            CPU.run(lCurrent, 10);
            
            int runTime  = lCurrent.getBurstRemain() >= 10 ? 10 : lCurrent.getBurstRemain();
            lCurrent.decrementRemainBurst(runTime);
            if(lCurrent.isDone) {
                TurnAroundTime = lCurrent.getBurst() + lCurrent.waitTime;
                TurnSum += TurnAroundTime;
                WaitSum += lCurrent.waitTime;
            }

            for(int i = 0; i < gTaskList.size(); i++){
                Task newTask = gTaskList.get(i);
                if (!newTask.equals(lCurrent) && !newTask.isDone){
                    WaitTime += runTime;
                    newTask.waitTime += runTime;
                }
            }

            //TODO NEED TO FIND A WAY TO CALCULATE THE AVRG TURN AND WAIT TIME WHEN ALL TASK ARE DONE, MAYBE WITH GCOMPLETECOUNT??

            if (gTermFlag) {
                System.out.println("Task " + lCurrent.getName() + " finished.");
                System.out.println();
                gTaskList.remove(lCurrent);

            }
            gTermFlag = false;
        }
    }

    public Task pickNextTask() {
        // Check the current element's remaining time:
        int lTime = gTimeList.get(gLocationInQueue);
        if (lTime <= 0) {
            // task has already been completed, so we don't care
            // Why not just delete the task from the list? I'm a dummy
            gCompletionCounter += 1;
            return null;
        } else if (lTime <= 10 && lTime > 0) {
            // At most, one cycle left - we have to print "Task x has finished" POST
            // CPU.run() call, ick
            gTermFlag = true;
            gTimeList.set(gLocationInQueue, lTime - 10);
            gCompletionCounter = 0;
            return gTaskList.get(gLocationInQueue);
        } else {
            // There's more than one cycle left - so we don't care about printing "Task x
            // has finished"
            gTimeList.set(gLocationInQueue, lTime - 10);
            gCompletionCounter = 0;
            return gTaskList.get(gLocationInQueue);
        }
    }
}
