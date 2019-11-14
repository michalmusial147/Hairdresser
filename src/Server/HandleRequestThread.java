package Server;

import Client.model.HairDresserTermin;
import Client.model.HairDresserTerminString;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Server.view.Controller;
import util.DateUtil;
import util.Operation;
import util.WeekTerminsGenerator;

/**
 * Klasa reprezentuje watek serwera.
 */
public class HandleRequestThread implements Runnable {
    private Socket socket;
    private InputStream Input_Stream;
    private OutputStream Output_Stream;
    private ObjectOutputStream Object_Output_Stream;
    private ArrayList<HairDresserTermin> Reservations;
    private Controller controller;
    private  BufferedReader in;

    public HandleRequestThread(Socket socket, ArrayList<HairDresserTermin> reservations, Controller controller) {
        this.socket = socket;
        this.Reservations = reservations;
        this.controller = controller;
    }

    @Override
    public void run() {
        String user="";
        String request="";
        byte[] buf =new byte[64];
        try {
            this.Input_Stream = socket.getInputStream();
            this.Output_Stream = socket.getOutputStream();
            this.Object_Output_Stream = new ObjectOutputStream(Output_Stream);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            user = in.readLine();
            request = in.readLine();
        }
        catch(IOException e){e.printStackTrace();}
        System.out.println("Host connected: " + user +" with req: " + request);
        if(request.equals(Operation.GETTERMINS.toString())) {
            try {
                List<HairDresserTerminString> bufer = new ArrayList<>();
                for ( HairDresserTermin termin : Reservations) {
                    bufer.add(new HairDresserTerminString(termin));
                }
                Object_Output_Stream.writeObject(bufer);
            }
            catch(IOException e ){
                e.printStackTrace();
            }
        }
        if(request.equals(Operation.REGISTER.toString())) {
            String reservation_time;
            try{
               reservation_time = in.readLine();
               for (HairDresserTermin t : Reservations) {
                   if(DateUtil.format(t.TerminTime()).equals(reservation_time)){
                       this.Output_Stream.write("RESERVATED".getBytes());
                       return;
                   }
               }
               Reservations.add(new HairDresserTermin(DateUtil.parse(reservation_time)));
               controller.init_columns();
           }
           catch(IOException  e){
                System.out.println(":(" + in.toString());
               e.printStackTrace();
           }
        }
        System.out.println("Koniec " + user + " " + request);
    }
}

