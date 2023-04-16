
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ProducerClient {

    public static void main(String[] args) {
        int id;
        int toSend = 0;
        int idx = 0;
        boolean isUnsafe = false;
        if (args[0].equals("u"))
        {
            isUnsafe = true;
        }
        Scanner sc = new Scanner(System.in);
        try (Socket socket = new Socket("localhost", 8000)) {
            System.out.println("Client connected");
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            // get Id
            String fromServer = reader.readLine();
            System.out.println("UUID From Server: " + fromServer);
            id = Integer.parseInt(fromServer);
            
            fromServer = reader.readLine();
            // wait for signal to start
            System.out.println("====================================");
            while (true) {
                if (fromServer.equals("RUN")) {
                    System.out.println("Select Operation:");
                    System.out.println("'0' to get a count of items in the buffer");
                    System.out.println("'1' followed by a number to add that number to the buffer");
                    System.out.println("'2' to terminate the process");
                    if (isUnsafe)
                    {
                        System.out.println("'3' to produce at an index in the buffer");
                    }
                    System.out.println("An invalid choice will be interpreted as 0");
                    System.out.print("> ");
                    int selection = -1;
                    while (true) {

                        try {
                            selection = sc.nextInt();
                        } catch (Exception e) {
                            System.out.println("Invalid Choice, interpreted as 0");
                            selection = 0;
                        }
                        if (!(selection == 0 || selection == 2
                                || selection == 1 || (selection == 3 && isUnsafe))) {
                            selection = 0;
                        }
                        // Worst code ever written by far
                        break;
                    }
                    if (selection == 1) {
                        System.out.println("Enter value to send: ");
                        System.out.print("> ");
                        idx = 0;
                        toSend = sc.nextInt();
                        // System.out.println("Sending: " + toSend);
                        String str = Integer.toString(idx) +
                                parseChoice(selection) +
                                Integer.toString(toSend);
                        writer.println(str);
                    }
                    String response = "null";
                    //System.out.println(parseChoice(selection));
                    if (selection != 2 && selection != 1 && selection != 3) {
                        writer.println(parseChoice(selection));
                    }
                    if (selection == 2) {
                        System.out.println("Terminating.");
                        writer.println(parseChoice(selection));
                        sc.close();
                        return;
                    }
                    if (selection == 3)
                    {
                        System.out.println("Enter an index: ");
                        System.out.print("> ");
                        int index = sc.nextInt();
                        System.out.println("Enter a value: ");
                        System.out.print("> ");
                        int value = sc.nextInt();
                        writer.println(Integer.toString(index) + "addToIndex" + Integer.toString(value));
                    }
                    response = reader.readLine();
                    if (selection == 0) {
                        System.out.println("Buffer Item Count: " + response);
                    }
                    // writer.println(Process.NEXT_ITEM_POS);
                    System.out.println("Sent request");
                    // int numItems = Integer.parseInt(reader.readLine());
                    // System.out.println("num:" + numItems);
                    // System.out.println("From Server: " + reader.readLine());
                    fromServer = reader.readLine();
                    System.out.println("====================================");
                    System.out.println();
                    System.out.println("====================================");
                    System.out.println("New Request");
                }
            }
        } catch (

        UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }

    }

    public static String parseChoice(int a) {
        switch (a) {
            case 0:
                return "getNumItems";
            case 1:
                return "addToBuffer";
            case 2:
                return "terminate";
            default:
                return "terminate";
        }
    }
}
