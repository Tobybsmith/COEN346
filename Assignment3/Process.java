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
    
    private static Pattern P_BUF;
    private static Pattern G_BUF;

    
    private int processId;
    public static int uid = 0;
    
    public static int val = 0;
    public static int idx = 0;

    private Buffer buffer;
    private BufferedReader reader;
    private PrintWriter writer;

    public Process(int id, Socket socket, Buffer buffer) throws Exception{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.buffer = buffer;
        this.processId = id;
        //Writes the PID to the connected client - will be unique
        writer.println(uid);
        this.processId = uid;
        uid += 1;
        
        P_BUF = Pattern.compile("addToBuffer[0-9][0-9]+");
        G_BUF = Pattern.compile("addToBuffer[0-9]+");
    }

    public int run(int times){
        System.out.println("process running");

        for(int i = 0; i < times; i++) {
            try{
                writer.println("RUN");
                System.out.println("sent");
                String instruction = reader.readLine();
                System.out.println("Instruction: " + instruction);
                if(instruction.equals(NUM_ITEMS)){
                    getNumberofItems();
                }
                else if(instruction.equals(NEXT_ITEM_POS)){
                    getNextItemPosition();
                }
                else if(instruction.equals(TERMINATE)){
                    return -1;
                }
                if (Pattern.matches("getItemInPos[0-9]+", instruction))
                {
                    getItem(Integer.parseInt(instruction.split("getItemInPos")[1]));
                }
                if (Pattern.matches("[0-9]+addToBuffer[0-9]+", instruction))
                {
                    //Parse Buffer request
                    System.out.println("Adding to buffer");
                    parseBufferRequest(instruction);
                    System.out.println("Adding: " + val + ", To: " + idx);
                    buffer.insertItem(val, idx);
                }
            }catch(IOException e){
                return -1;
            }

        }
        return 0;
    }

    public void getNumberofItems() throws IOException {
        int numItems = buffer.readCount();
        writer.println(numItems);
    }

    public void getNextItemPosition(){
        System.out.println("I am stuff");
    }

    public void getItem(int position){
        int item = buffer.getNextItem(position);
        //send to client
        writer.println(item);
    }
    
    public void parseBufferRequest(String req)
    {
        //Will be of the forn "addToBuffer[index][numtoadd]"
        //Split the string into the first int component and the last int component
        boolean firstSeq = true;
        //Hmm today i will use a regex
        //clueless ^
        String[] arr = req.split("addToBuffer");
        
        val = Integer.parseInt(arr[1]);
        idx = Integer.parseInt(arr[0]);
    }
}
