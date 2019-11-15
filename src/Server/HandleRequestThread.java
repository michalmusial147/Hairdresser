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
    private ArrayList<HairDresserTermin> Reservations;
    private Controller controller;


    public HandleRequestThread(Socket socket, ArrayList<HairDresserTermin> reservations, Controller controller) {
        this.socket = socket;
        this.Reservations = reservations;
        this.controller = controller;
    }

    @Override
    public void run() {
        String user="";
        String request="";
        InputStream inputStream=null;
        OutputStream outputStream=null;
        ObjectOutputStream Object_Output_Stream=null;
        BufferedReader in = null;
        byte[] buf =new byte[64];
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Object_Output_Stream = new ObjectOutputStream(outputStream);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            user = in.readLine();
            request = in.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Host connected: " + user +" with req: " + request);
        if(request.equals(Operation.GETTERMINS.toString())) {
            try {
                List<HairDresserTerminString> bufer = new ArrayList<>();
                for ( HairDresserTermin termin : Reservations) {
                    bufer.add(new HairDresserTerminString(termin));
                }
                Object_Output_Stream.writeObject(bufer);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
       else if(request.equals(Operation.REGISTER.toString())) {
            String reservation_time;
            try{
               reservation_time = in.readLine();
               outputStream.flush();
               for (HairDresserTermin t : Reservations) {
                   if(DateUtil.format(t.TerminTime()).equals(reservation_time)){
                       outputStream.write(("TERMIN_BUSY" + "\n").getBytes());
                       outputStream.flush();
                       return;
                   }
               }

               outputStream.write(("RESERVATION_SUCCESS" + "\n").getBytes());
               outputStream.flush();
               Reservations.add(new HairDresserTermin(DateUtil.parse(reservation_time)));
               if(controller != null){
                   controller.init_columns();

               }
           }
           catch(IOException e){
                System.out.println(":(" + in.toString());
                e.printStackTrace();
           }
       }
       try {
           in.close();
           inputStream.close();
           outputStream.close();
           socket.close();
       }
       catch(IOException e){
           e.printStackTrace();
       }
       System.out.println("Koniec " + user + " " + request);
    }
}

