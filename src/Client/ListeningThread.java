package Client;


import Server.HandleRequestThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListeningThread{
    public static int PORT = 15456;
    private ServerSocket serverSocket;

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
