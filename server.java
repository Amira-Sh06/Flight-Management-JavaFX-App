
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.util.Random;

public class server {
    private static int depReschedules = 0;
    private static int landReschedules = 0;


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server started. Waiting for connections...");
        boolean test = false;
        try {
            while (!test) {
                Socket clientSocket = serverSocket.accept();
                InputStream fromClient = clientSocket.getInputStream();
                String clientMsg;
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pr = new PrintWriter(clientSocket.getOutputStream(), true);
                System.out.println("Client connected. ");
                clientMsg = br.readLine();
                if(clientMsg.equals("Dep reschedule")) {
                    depReschedules++;
                    pr.println("Departure reschedules: " + depReschedules);
                    System.out.println("Departure schedule updated");
                }
                else if(clientMsg.equals("Land schedule")) {
                    landReschedules++;
                    pr.println("Landing reschedules: " + landReschedules);
                    System.out.println("Landing schedule updated");
                } else if (clientMsg.equals("Count Request")) {
                pr.println("Departure reschedules: " + depReschedules + ", Landing reschedules: " + landReschedules);
                }
                else {
                    System.out.println("No late flights");
                    pr.println(clientMsg);
                }
                pr.flush();
                br.close();
                pr.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Server error.");
        }
    }
}