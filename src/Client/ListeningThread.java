package Client;


import Client.model.HairDresserTermin;
import Client.view.Controller;
import Server.HandleRequestThread;
import util.Operation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ListeningThread implements Runnable{
    public  int clientListeningPort;
    private ServerSocket serverSocket;
    Controller controller;
    String Client_name;
    ArrayList<HairDresserTermin> Reservations;
    Operation operationType;
    public ListeningThread(Controller controller,
                           String client_name,
                           ArrayList<HairDresserTermin> Reservations,
                           Operation operationType,
                           int clientListeningPort){
        try{
            System.out.println("client port = " + clientListeningPort);
            this.serverSocket= new ServerSocket(clientListeningPort);
        }
        catch(IOException e){e.printStackTrace();
        }
        this.controller = controller;
        this.Client_name = client_name;
        this.Reservations = Reservations;
        this.operationType = operationType;
        this.clientListeningPort = clientListeningPort;
    }


    @Override
    public void run() {
        try {
            System.out.println("Listening with port:"+ this.clientListeningPort);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Pobierz");
                try {
                    Thread GetTerminsThread;
                    ClientNetThread getTermins = new ClientNetThread(controller, Client_name, Reservations,
                            Operation.GETTERMINS,this.clientListeningPort);
                    GetTerminsThread = new Thread(getTermins);
                    GetTerminsThread.start();
                    GetTerminsThread.join();
                    controller.StartClientMain.setReservations(getTermins.getReservations());
                }
                catch(InterruptedException i){i.printStackTrace();}
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



}
