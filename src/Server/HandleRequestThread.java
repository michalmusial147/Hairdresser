package Server;

import Client.model.HairDresserTermin;
import Client.model.HairDresserTerminString;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import Server.view.Controller;
import util.DateUtil;
import util.Operation;

/**
 * Klasa reprezentuje watek serwera.
 */
public class HandleRequestThread implements Runnable {
    private Socket socket;
    private Set<Integer> ports;
    private ArrayList<HairDresserTermin> Reservations;
    private Controller controller;
    private ReentrantLock lock;
    public HandleRequestThread(Socket socket, Set<Integer> ports,
                               ArrayList<HairDresserTermin> reservations, Controller controller) {
        this.socket = socket;
        this.ports = ports;
        this.Reservations = reservations;
        this.controller = controller;
        lock = new ReentrantLock();
    }

    @Override
    public void run() {
        lock.lock();
        String user="";
        String request="";
        String newport="";
        InputStream inputStream=null;
        OutputStream outputStream=null;
        ObjectOutputStream Object_Output_Stream=null;
        BufferedReader in = null;
        byte[] buf = new byte[64];
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            user = in.readLine();
            newport = in.readLine();
            request = in.readLine();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Host connected: " + user +" with req: " + request);
        Integer portin = new Integer(newport);

        ports.add(portin);
        if(request.equals(Operation.GETTERMINS.toString())) {
            try {
                List<HairDresserTerminString> bufer = new ArrayList<>();
                for ( HairDresserTermin termin : Reservations) {
                    bufer.add(new HairDresserTerminString(termin));
                }
                Object_Output_Stream = new ObjectOutputStream(outputStream);
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
               sendEcho(portin);
            }
           catch(IOException e){
                System.out.println(":(" + in.toString());
                e.printStackTrace();
           }
       }
        else if(request.equals(Operation.CANCEL.toString())) {
            String reservation_time;
            try{
                reservation_time = in.readLine();
                Reservations.removeIf(t->(DateUtil.format(t.TerminTime()).equals(reservation_time)));
                sendEcho(portin);
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
       lock.unlock();
       System.out.println("Koniec " + user + " " + request);
    }

    private void sendEcho(Integer portin) throws IOException {
        for(Integer i : ports){
            System.out.println("Echo:" + i.toString());
            if(!portin.equals(i)){
               new Socket().connect(new InetSocketAddress("localhost", i), 1000);
            }
        }
        if(controller != null){
            controller.init_columns();
        }
    }

    public ArrayList<HairDresserTermin> getReservations() {
        return Reservations;
    }

}

