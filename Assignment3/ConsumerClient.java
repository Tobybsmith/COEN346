
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConsumerClient {

    public static void main(String[] args) {
        int id = 0;
        int toSend = 0;
        int idx = 0;
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

            // wait for signal to start
            fromServer = reader.readLine();
            // System.out.println("Instruction From Server: " + fromServer);
            // writer.println() to send client -> server
            // reader.readline() to read from server

            while (true) {
                if (fromServer.equals("RUN")) {
                    System.out.println("Select Operation:");
                    System.out.println("'0' to get a count of items in the buffer");
                    System.out.println("'1' to consume the current item");
                    System.out.println("'2' to terminate");
                    System.out.println("An invalid choice will be interpreted as 0");
                    System.out.print("> ");
                    int selection = -1;
                    while (true) {

                        try {
                            selection = sc.nextInt();
                        } catch (Exception e) {
                            selection = 0;
                        }
                        if (!(selection == 0 || selection == 1 || selection == 2)) {
                            selection = 2; // will terminate if invalide input request
                        }
                        // Worst code ever written by far
                        break;
                    }
                    writer.println(parseChoice(selection));
                    String response = "null";
                    // RESPONSE STUFF
                    // Should wait until there is something to consume, how?
                    // If a consume call is sent to the server, then the process gets put back into
                    // the queue
                    // and the consume call "queued" until there is something to consume and it
                    // reaches the front of the queue
                    // Buffer count will always be valid, and terminate will always be valid
                    // Basically, it skips it's turn if there's nothing to consume, a -1 return code
                    // maybe?
                    response = reader.readLine();
                    if (selection == 0) {
                        System.out.println("Buffer Item Count: " + response);
                    } else if (selection == 1) {
                        System.out.println("Consumed Item: " + response);
                    } else if (selection == 2) {
                        System.out.println("Terminating");
                        break;
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

        } catch (UnknownHostException ex) {

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
                return "consume";
            case 2:
                return "terminate";
            default:
                return "terminate";
        }
    }
}
