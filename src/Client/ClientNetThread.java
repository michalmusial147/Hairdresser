package Client;
import java.io.*;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;

import Client.model.HairDresserTerminString;
import Client.model.HairDresserTermin;
import Client.view.Controller;
import util.DateUtil;
import util.Operation;

public class ClientNetThread implements Runnable {

    private Socket socket = new Socket();
    private String Client_name;
    private ArrayList<HairDresserTermin> Reservations;
    private Operation operationType;
    private Controller controller;
    private HairDresserTermin ReservationTime;
    private boolean registerOK;
    private static int serverListeningPort = 15456;
    private int clientListeningPort;


    public ClientNetThread(Controller controller, String client_name, ArrayList<HairDresserTermin> Reservations,
                           Operation operationType, int listeningPort) throws IOException {
        this.controller = controller;
        this.Client_name = client_name;
        this.Reservations = Reservations;
        this.operationType = operationType;
        this.clientListeningPort = listeningPort;
    }

    public ClientNetThread(Controller controller,
                           String client_name,
                           HairDresserTermin ReservationTime,
                           Operation operationType,int listeningPort
                          ) throws IOException {
        this.controller = controller;
        this.Client_name = client_name;
        this.ReservationTime = ReservationTime;
        this.operationType = operationType;
        this.clientListeningPort = listeningPort;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            // INIT CONNECTION
            socket.connect(new InetSocketAddress("localhost", serverListeningPort), 1000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            out.write((Client_name + "\n").getBytes());
            out.write((Integer.toString(clientListeningPort) + "\n").getBytes());
            out.write((operationType.toString()+"\n").getBytes());
        }
        catch(IOException e){ e.printStackTrace();
        }
        if (this.operationType == operationType.GETTERMINS) {
            GetTermins(in);
        }
        else if(this.operationType == operationType.REGISTER) {
            setRegisterOK(RegisterTermin(out,in));
        }
        try{
            out.close();
            in.close();
            socket.close();
        }
        catch(IOException e){e.printStackTrace();
        }
    }
    private boolean GetTermins(InputStream in){
        try {
            ObjectInputStream Object_input_stream = new ObjectInputStream(in);
            ArrayList<HairDresserTerminString> bufer = new ArrayList<>();
            bufer = (ArrayList<HairDresserTerminString>) Object_input_stream.readObject();
            ArrayList<HairDresserTermin> resultofwrap = new ArrayList<HairDresserTermin>();
            for (HairDresserTerminString termin : bufer) {
                resultofwrap.add(new HairDresserTermin(DateUtil.parse(termin.TerminTimeString())));
            }
            this.Reservations = resultofwrap;

        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private boolean RegisterTermin(OutputStream out, InputStream in){
        try{
            out.write((DateUtil.format(getReservationTime().TerminTime()) +"\n").getBytes());
            String response = new BufferedReader(new InputStreamReader(in, "UTF-8")).readLine();
            if(response.equals("RESERVATION_SUCCESS")){
                return true;
            }
            else if(response.equals("TERMIN_BUSY")){
                return false;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean isRegisterOK() {
        return registerOK;
    }

    public ArrayList<HairDresserTermin> getReservations() {
        return Reservations;
    }
    public HairDresserTermin getReservationTime() {
        return ReservationTime;
    }
    public void setRegisterOK(boolean registerOK) {
        this.registerOK = registerOK;
    }
}





