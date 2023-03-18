
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ProducerClient {

    public static void main(String[] args){
        int id;
        int toSend = 0;
        int idx = 0;
        Scanner sc = new Scanner(System.in);
        try(Socket socket = new Socket("localhost", 8000)){
            System.out.println("Client connected");
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            //get Id
            String fromServer = reader.readLine();
            System.out.println("UUID From Server: " + fromServer);
            id = Integer.parseInt(fromServer);

            //wait for signal to start
            fromServer = reader.readLine();
            System.out.println("Instruction From Server: " + fromServer);
            int i = 0;
            //writer.println() to send client -> server
            //reader.readline() to read from server

            while(true) {
                if (fromServer.equals("RUN")) {
                    System.out.println("Select Operation:");
                    System.out.println("'0' to get a count of items in the buffer");
                    System.out.println("'1' to get the item in the current position");
                    System.out.println("'2' to get the current buffer position");
                    System.out.println("'3' to terminate the process");
                    System.out.println("'4' followed by a number to add that number to the buffer");
                    System.out.println("An invalid choice will be interpreted as 0");
                    System.out.print("> ");
                    int selection = -1;
                    while (true)
                    {

                        try
                        {
                            selection = sc.nextInt();
                        }
                        catch (Exception e)
                        {
                            selection = 0;
                        }
                        if (!(selection == 0 || selection == 2 
                        || selection == 3 || selection == 1 || selection == 4))
                        {
                            selection = 0;
                        }
                        //Worst code ever written by far
                        break;
                    }
                    if (selection == 3)
                    {
                        break;
                    }
                    if (selection == 1)
                    {
                        System.out.println("Enter index to view: ");
                        System.out.print("> ");
                        int val = sc.nextInt();
                        writer.println(parseChoice(selection) + Integer.toString(val));
                    }
                    if (selection == 4)
                    {
                        System.out.println("Enter index and value to send: ");
                        System.out.print("> ");
                        idx = sc.nextInt();
                        toSend = sc.nextInt();
                        System.out.println("Sending: " + idx + " " + toSend);
                        String str = Integer.toString(idx) + 
                            parseChoice(selection) + 
                            Integer.toString(toSend);
                        writer.println(str);
                    }
                    String response = "null";
                    if (!(selection == 4))
                    {
                        writer.println(parseChoice(selection));
                        response = reader.readLine();
                    }
                    if (selection == 0)
                    {
                        System.out.println("Buffer Count: " + response);
                    }
                    else if (selection == 1)
                    {
                        System.out.println("Value Of Buffer Item: " + response);
                    }
                    else if (selection == 2)
                    {
                        System.out.println("Pos Of Next Item: " + response);
                    }
                    else if (selection == 3)
                    {
                        System.out.println("Terminating.");
                    }
                    //writer.println(Process.NEXT_ITEM_POS);
                    System.out.println("Sent request");
                    //int numItems = Integer.parseInt(reader.readLine());
                    //System.out.println("num:" + numItems);
                    //System.out.println("From Server: " + reader.readLine());
                    fromServer = reader.readLine();
                    System.out.println("====================================");
                    System.out.println();
                    System.out.println("====================================");
                    System.out.println("New Request");
                }
            }
            if (fromServer.equals("RUN")) {
                writer.println(Process.TERMINATE);
            }

        }catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

    }

    public static String parseChoice(int a)
    {
        switch(a)
        {
            case 0:
                return "getNumItems";
            case 1:
                return "getItemInPos";
            case 2:
                return "getNextItemPos";
            case 3:
                return "terminate";
             case 4:
                 return "addToBuffer";
            default:
                return "terminate";
        }
    }
}
