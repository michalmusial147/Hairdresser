package Server;
import Client.model.HairDresserTermin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerNet implements Runnable {

    public static int PORT = 15456;
    private ServerSocket serverSocket = new ServerSocket();
    private StartServer servermain;
    private ArrayList<HairDresserTermin> Reservations;
    public ServerNet(ArrayList<HairDresserTermin> reservations) throws Exception {
        serverSocket = new ServerSocket(PORT);
        this.Reservations = reservations;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new HandleRequestThread(socket, Reservations)).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}