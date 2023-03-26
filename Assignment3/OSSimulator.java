
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class OSSimulator extends Thread {

    private final static int MAX_PROCESSES = 3;

    private Process[] clients = new Process[MAX_PROCESSES];
    private Queue<Process> procs;

    private Buffer buffer = new Buffer();
    private int numClients = 0;

    private int currClient = 0;

    private int LocationInQueue = 0;
    private Queue<Process> consumeQueue;
    private Queue<Process> produceQueue;

    public OSSimulator() {
        procs = new LinkedList<Process>();
        consumeQueue = new LinkedList<Process>();
        produceQueue = new LinkedList<Process>();
    }

    public int createProcess(Socket client) throws Exception {
        if (numClients == MAX_PROCESSES) {
            return -1;
        }
        // get id and assign to that position
        // Add to back of "queue"
        for (int i = 0; i < clients.length; i++) {
            if (clients[(LocationInQueue + i) % clients.length] == null) {
                // empty spot, place it here
                clients[(LocationInQueue + i) % clients.length] = new Process(numClients - 1, client, buffer);
                break;
                // add to queue;
            }
        }

        return 0;
    }

    public Process schedule() {
        // return the procesxs at the index
        Process cli = clients[LocationInQueue];
        return cli;
    }

    public void run() {
        while (true) {
            // Schedule
            System.out.println("Scheduling");
            Process client = schedule();

            System.out.println(client);
            if (client != null) {
                int result = client.run(1);
                if (result == -1) {
                    // delete process from queue and processes
                    clients[LocationInQueue] = null;
                } else if (result != -2 && result != -3) {
                    System.out.println("Result: " + result);
                    clients[LocationInQueue] = null;
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[(LocationInQueue + i) % clients.length] == null) {
                            // empty spot, place it here
                            clients[(LocationInQueue + i) % clients.length] = client;
                        }
                    }
                    LocationInQueue += 1;
                    LocationInQueue %= clients.length;
                } else if (result == -2) {
                    // producer
                    produceQueue.add(client);
                    clients[LocationInQueue] = null;
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[(LocationInQueue + i) % clients.length] == null) {
                            // empty spot, place it here
                            clients[(LocationInQueue + i) % clients.length] = client;
                        }
                    }
                    LocationInQueue += 1;
                    LocationInQueue %= clients.length;
                } else if (result == -3) {
                    consumeQueue.add(client);
                    clients[LocationInQueue] = null;
                    for (int i = 0; i < clients.length; i++) {
                        if (clients[(LocationInQueue + i) % clients.length] == null) {
                            // empty spot, place it here
                            clients[(LocationInQueue + i) % clients.length] = client;
                        }
                    }
                    LocationInQueue += 1;
                    LocationInQueue %= clients.length;
                    // consumer
                }
                // Now proc the two queued action buffers to either resolve one produce or one
                // consume

                if (!produceQueue.isEmpty()) {
                    // resolve the top element in the queue
                    Process temp = produceQueue.remove();
                    int q_result = 0;
                    q_result = temp.run(1);
                    if (q_result == 0) {
                        // success

                    } else if (q_result == -2) {
                        // re-attempt failed
                        produceQueue.add(temp);
                    }
                }

                if (!consumeQueue.isEmpty()) {
                    Process t = consumeQueue.remove();
                    int q_r = t.run(1);

                    if (q_r == 0) {

                    } else if (q_r == -3) {
                        consumeQueue.add(t);
                    }
                }

            }
        }
    }
}
