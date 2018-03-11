package Experiment7;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Server {

    //creating a buffer
    ArrayList<Packet_> Server_Buffer;
    ServerSocket server_socket;
    Socket socket;
    public Server() throws IOException {
        //server socket where the client can connect
        server_socket = new ServerSocket(6666);
        socket=server_socket.accept();//establishes connection
    }

    Packet_ receivepacket() {
        Server_Buffer = new ArrayList<>();
        Packet_ dummy = new Packet_();
        return dummy;
    }

    void sendPacket(Packet_ p) {
    }

    //adds the packet to the buffer
    public void addPacketToBuffer(Packet_ packet_) {
        Server_Buffer.add(packet_);
    }

    //removes the packet from the buffer
    public Packet_ removePacketFromBuffer(int seq) {
        Packet_ ob = new Packet_();
        for (Packet_ packet_ : Server_Buffer) {
            if (packet_.getSeq() == seq) {
                ob = packet_;
                Server_Buffer.remove(packet_);//removing the packet with sequnce number mentioned in the parameter
            }

        }
        return ob;//returning a copy of packet
    }

    //converts the given packet's data to 0 and ack flag to 1
    void generateAck(Packet_ p) {
        char ch[] = new char[20];
        for (char c : ch) {
            c = Character.MIN_VALUE;
        }
        p.setAck((byte) 1);
        p.setData(ch);
    }
    
    public void communicate() throws IOException, ClassNotFoundException {
        
        
        //Creating a stream to accept simple data -UTF format
        DataInputStream dis = new DataInputStream(socket.getInputStream());

//            while(true)
//            {
        String str = (String) dis.readUTF();
        System.out.println("message= " + str);

        //creating a stream to accept objects
        ObjectInputStream objectReader = new ObjectInputStream(dis);

        //read the object sent by the client
        Packet_ dummy = (Packet_) objectReader.readObject();
        dummy.printPacketDetails();
//            }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Server server=new Server();
        server.communicate();

/*
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
         
         */
    }
}
