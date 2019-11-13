package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Client.model.HairDresserTerminString;
import Client.model.HairDresserTermin;
import Client.util.DateUtil;



import javafx.collections.*;
import static java.lang.System.out;

public class ClientNetThread implements Runnable{

    private Socket socket = new Socket();
    private String Client_name;
    private ObjectInputStream Object_input_stream;
    private ArrayList<HairDresserTermin> Reservations;
    private operationType OperationType;

    public ClientNetThread(String client_name, ArrayList<HairDresserTermin> Reservations,OperationType operationType) throws IOException {
        this.Client_name = client_name;
        this.Reservations = Reservations;
        this.OperationType = operationType;
    }

    enum operationType {
        REGISTER,CANCEL,GETTERMINS,LISTEN
    }


    @Override
        public void run(){
            try {
                socket.connect(new InetSocketAddress("localhost", 15456), 1000);
                OutputStream out = socket.getOutputStream();
                InputStream in = socket.getInputStream();
                if(this.OperationType ==  operationType.GETTERMINS)
                out.write((Client_name+"\n").getBytes());
                out.write("get_termins\n".getBytes());

                Object_input_stream = new ObjectInputStream(in);
                try {
                    ArrayList<HairDresserTerminString> bufer = new ArrayList<>();
                     bufer = (ArrayList<HairDresserTerminString>)Object_input_stream.readObject();
                    ArrayList<HairDresserTermin> resultofwrap = new ArrayList<HairDresserTermin>();
                    for ( HairDresserTerminString termin : bufer ) {
                        resultofwrap.add(new HairDresserTermin(DateUtil.parse(termin.TerminTimeString())));
                    }
                    Reservations = resultofwrap;
                }
                catch(IOException | ClassNotFoundException e ){
                    e.printStackTrace();
                }
                out.close();
                in.close();
                socket.close();

            }
            catch(IOException e ) {
                e.printStackTrace();
            }
        }
        public boolean RegisterTermin(HairDresserTermin termin){
            boolean success = false;


            return success;
        }


    }





