package Server;

import Client.model.HairDresserTermin;
import Client.model.HairDresserTerminString;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Operation;

/**
 * Klasa reprezentuje watek serwera.
 */
public class HandleRequestThread implements Runnable {
    private Socket socket;
    private InputStream Input_Stream;
    private OutputStream Output_Stream;
    private ObjectOutputStream Object_Output_Stream;
    ArrayList<HairDresserTermin> Reservations;



    public HandleRequestThread(Socket socket, ArrayList<HairDresserTermin> reservations) {
        this.socket = socket;
        Reservations = reservations;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            user = in.readLine();
            request = in.readLine();
        }
        catch(IOException e){e.printStackTrace();}
        System.out.println("Host connected: " + user +" with req: " + request);
        if(request.equals(Operation.GETTERMINS.toString())) {            try {
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

        }
        System.out.println("Koniec " + user + " " + request);
    }
}

