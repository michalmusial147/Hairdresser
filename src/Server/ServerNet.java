package Server;
import Client.model.HairDresserTermin;
import Server.view.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerNet implements Runnable {

    public static int PORT = 15456;
    private ServerSocket serverSocket;
    private ArrayList<HairDresserTermin> Reservations;
    private Controller controller;
    public ServerNet(ArrayList<HairDresserTermin> reservations, Controller controller) throws Exception {
        serverSocket = new ServerSocket(PORT);
        this.Reservations = reservations;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                HandleRequestThread handleThread = new HandleRequestThread(socket, Reservations, controller);
                new Thread(handleThread).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}