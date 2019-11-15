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
    private  Controller controller;

    public HairDresserTermin getReservationTime() {
        return ReservationTime;
    }

    private HairDresserTermin ReservationTime;

    private boolean registerOK;

    public ClientNetThread(Controller controller, String client_name, ArrayList<HairDresserTermin> Reservations, Operation operationType) throws IOException {
        this.controller = controller;
        this.Client_name = client_name;
        this.Reservations = Reservations;
        this.operationType = operationType;
    }

    public ClientNetThread(Controller controller, String client_name, HairDresserTermin ReservationTime, Operation operationType) throws IOException {
        this.controller = controller;
        this.Client_name = client_name;
        this.ReservationTime = ReservationTime;
        this.operationType = operationType;
    }


    @Override
    public void run() {
        InputStream in=null;
        OutputStream out=null;
        try {
            // INIT CONNECTION
            socket.connect(new InetSocketAddress("localhost", 15456), 1000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            out.write((Client_name + "\n").getBytes());
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
            Reservations = resultofwrap;

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
            String response = new BufferedReader(new InputStreamReader(in)).readLine();
            if(response.equals("RESERVATION_SUCCESS")){
                return true;
            }
            else if(response.equals("TERMIN_BUSY")){
                this.controller.showAlert("Termin zajety", "Wybrano zajety termin",
                        "Prosze wybrac inny termin wizyty");
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

    public void setRegisterOK(boolean registerOK) {
        this.registerOK = registerOK;
    }
}





