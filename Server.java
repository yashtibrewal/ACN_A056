package Experiment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Server {

    //creating a buffer
    ArrayList<Packet_> Server_Buffer;
    ServerSocket server_socket;
    Socket socket;
    ObjectInputStream objectReader;
    DataInputStream dis;
    ObjectOutputStream objectWriter;
    DataOutputStream dout;

    public Server() throws IOException {
        //server socket where the client can connect
        Server_Buffer = new ArrayList<>();
        server_socket = new ServerSocket(6666);
        socket = server_socket.accept();//establishes connection
        System.out.println("Server Connected");
        //Creating a input stream
        dis = new DataInputStream(socket.getInputStream());

        //Creating an output stream
        dout=new DataOutputStream(socket.getOutputStream());

        //creating a stream to send object using the ouput stream
        objectWriter=new ObjectOutputStream(dout);
        
        //creating a stream to accept objects using the input stream
        objectReader = new ObjectInputStream(dis);
        
        
    }

    Packet_ receivepacket() throws Exception {
        
        Packet_ packet_ = (Packet_)objectReader.readObject();
        return packet_;
    }

    void sendPacket(Packet_ packet)throws IOException{
        objectWriter.writeObject(packet);
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
    static Packet_ generateAck(Packet_ p) throws Exception {
        byte size = 0;//stores the size of the data received

        char ch[] = p.getData();

        //checking how ,uch data is present in the packet
        for (char c : ch) {
            if (c != Character.MIN_VALUE) {
                size++;
            }
        }
        //making the data field 0
        for (int i = 0; i < 20; i++) {
            ch[i] = Character.MIN_VALUE;
        }

        //adding the number of characters received
        p.setSeq(p.getSeq() + size);
        p.setAck((byte) 1);
        p.setData(ch);

        return p;
    }//tested and working correctly

    public void communicate() throws Exception {
        
        //read the object sent by the client
        while(true)
        {
            Packet_ packet_ = receivepacket();
            packet_.printPacketDetails();
            if(packet_.getData()[0]==Character.MAX_VALUE)
                break;
            packet_=generateAck(packet_);
            sendPacket(packet_);
        }
        
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, Exception {
        Server server = new Server();
        server.communicate();

        
        
        //Testing of acknowlegement generation
        /*Packet_ p=new Packet_();
          p.printPacketDetails();
          p.setData("Testing the size".toCharArray());
          p.setSeq(100);
          p.printPacketDetails();
          p=generateAck(p);
          p.printPacketDetails();*/
    }
}
