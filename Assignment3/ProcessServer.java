 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessServer {

    private static OSSimulator simulator;
    public static void main(String[] args){
        //System.out.println(args[0]);
        if (args[0].equals("s"))
        {
            simulator = new OSSimulator(false);
        }
        else
        {
            simulator = new OSSimulator(true);
        }
        simulator.start();
        ServerSocket socket;
        try {
            socket = new ServerSocket(8000);

            while(true) {
                System.out.println("Server waiting for connections");
                Socket client = socket.accept();
                if (simulator.createProcess(client) == -1) {
                    client.sendUrgentData(-1);
                    client.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
