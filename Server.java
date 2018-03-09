package Experiment7;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Server {

    double cwnd;
    double ssthresh;

    public Server() {
        cwnd = 1.0;
        ssthresh = 4.0;
    }

    void sendPacket(Packet_ ob) {
    }

    Packet_ receivePacket() {
        Packet_ dummy = new Packet_();

        if (dummy.ack == 1) {
            update_cwnd();
        }

        return dummy;
    }

    void update_cwnd() {
        if (cwnd < ssthresh) {
            cwnd += 1.0;
        } else {
            cwnd += 1 / cwnd;
        }
    }

    void timeout() {
        ssthresh = cwnd / 2;
        cwnd = 1;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (
             
            //server socket where the client can connect
            ServerSocket ss = new ServerSocket(6666)) {
            Socket s = ss.accept();//establishes connection
            
            
            //Creating a stream to accept simple data -UTF format
            DataInputStream dis = new DataInputStream(s.getInputStream());
            
            
//            while(true)
//            {
                String str = (String) dis.readUTF();
                System.out.println("message= " + str);
            
            //creating a stream to accept objects
            ObjectInputStream objectReader=new ObjectInputStream(dis);
            
            //read the object sent by the client
            Packet_ dummy=(Packet_)objectReader.readObject();
            dummy.printPacketDetails();
//            }
            

        
            
        } 
    }
}
