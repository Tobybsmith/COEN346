
//I freakin love REGEX :)
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import java.io.*;
import java.net.Socket;

public class Process {

    public final static String NUM_ITEMS = "getNumItems";
    public final static String GET_ITEM = "getItemInPos";
    public final static String NEXT_ITEM_POS = "getNextItemPos";
    public final static String TERMINATE = "terminate";
    public final static String ADD_BUF = "addToBuffer";
    public final static String CONSUME = "consume";
    private String queued_intr = "";
    private boolean queued_intr_true = false;
    private static Pattern P_BUF;
    private static Pattern G_BUF;

    private int processId;
    public static int uid = 0;

    public static int val = 0;
    public static int idx = 0;

    private Buffer buffer;
    private BufferedReader reader;
    private PrintWriter writer;

    public Process(int id, Socket socket, Buffer buffer) throws Exception {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.buffer = buffer;
        this.processId = id;
        // Writes the PID to the connected client - will be unique
        writer.println(uid);
        this.processId = uid;
        uid += 1;

        P_BUF = Pattern.compile("addToBuffer[0-9][0-9]+");
        G_BUF = Pattern.compile("addToBuffer[0-9]+");
    }

    public int run(int times) {
        System.out.println("process running");

        for (int i = 0; i < times; i++) {
            try {
                if (!queued_intr_true)
                {
                    writer.println("RUN");
                }
                
                System.out.println("sent");
                if (queued_intr_true) {
                    // go attempt the queued instruction
                    if (Pattern.matches("[0-9]+addToBuffer[0-9]+", queued_intr)) {
                        // Parse Buffer request
                        System.out.println("Adding to buffer");
                        parseBufferRequest(queued_intr);
                        System.out.println("Adding: " + val + ", To: " + idx);
                        if (put(val) == -1) {
                            // put failed
                            return 0;
                        }
                    } else if (queued_intr.equals("consume")) {
                        if (getItem() == -1) {
                            // get item failed failed
                            return 0;
                        } 
                    }
                    return 0;
                }
                String instruction = reader.readLine();
                System.out.println("Instruction: " + instruction);
                if (instruction.equals(NUM_ITEMS)) {
                    getNumberofItems();
                } else if (instruction.equals(NEXT_ITEM_POS)) {
                    getNextItemPosition();
                } else if (instruction.equals(TERMINATE)) {
                    return -1;
                } else if (instruction.equals(CONSUME)) {
                    // Consume from buffer here
                    if (getItem() == -1) {
                        System.out.println("Failed to consume");
                        return 0;
                    }
                }
                if (Pattern.matches("getItemInPos[0-9]+", instruction)) {
                    getItem();
                }
                if (Pattern.matches("[0-9]+addToBuffer[0-9]+", instruction)) {
                    // Parse Buffer request
                    System.out.println("Adding to buffer");
                    parseBufferRequest(instruction);
                    System.out.println("Adding: " + val + ", To: " + idx);
                    if (put(val) == -1) {
                        // put failed
                        return 0;
                    }
                }
            } catch (IOException e) {
                return -1;
            }

        }
        return 0;
    }

    public void getNumberofItems() throws IOException {
        int numItems = buffer.getSize();
        writer.println(numItems);
    }

    public void getNextItemPosition() {
        writer.println(buffer.getRear());
    }

    public int getItem() {
        if (buffer.isEmpty()) {
            queued_intr = "consume";
            queued_intr_true = true;
            //writer.println("SKIP");
            return -1;
        } else {
            queued_intr = "";
            queued_intr_true = false;
        }
        int item = buffer.dequeue();
        // send to client
        writer.println(item);
        return 0;
    }

    public int put(int a) {
        if (buffer.isFull()) {
            // force the process to "skip" it's turn and go to the back of the queue
            queued_intr = "0addToBuffer" + Integer.toString(a);
            queued_intr_true = true;
            return -1;
        } else {
            queued_intr = "";
            queued_intr_true = false;
        }
        buffer.enqueue(a);
        writer.println(0);
        return 0;
    }

    public void parseBufferRequest(String req) {
        // Will be of the forn "addToBuffer[index][numtoadd]"
        // Split the string into the first int component and the last int component
        boolean firstSeq = true;
        // Hmm today i will use a regex
        // clueless ^
        String[] arr = req.split("addToBuffer");

        val = Integer.parseInt(arr[1]);
        idx = Integer.parseInt(arr[0]);
    }
}
