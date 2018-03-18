package Experiment7;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {

    //creating a buffer
    ArrayList<Packet_> Server_Buffer;
    int buffer_size;
    ServerSocket server_socket;
    Socket socket;
    ObjectInputStream objectReader;
    DataInputStream dis;
    ObjectOutputStream objectWriter;
    DataOutputStream dout;

    public Server() throws IOException {
        buffer_size = 10;
        //server socket where the client can connect
        Server_Buffer = new ArrayList<>();
        server_socket = new ServerSocket(6666);
        socket = server_socket.accept();//establishes connection
        System.out.println("Server Connected");
        //Creating a input stream
        dis = new DataInputStream(socket.getInputStream());

        //Creating an output stream
        dout = new DataOutputStream(socket.getOutputStream());

        //creating a stream to send object using the ouput stream
        objectWriter = new ObjectOutputStream(dout);

        //creating a stream to accept objects using the input stream
        objectReader = new ObjectInputStream(dis);

    }

    Packet_ receivepacket() throws Exception {
        //reading the packet from the stream
        Packet_ packet_ = (Packet_) objectReader.readObject();
        System.out.println("------Packet Received");
        packet_.printPacketDetails();
        return packet_;//returning the read packet
    }
    
    //function to write the object to the stream
    void sendPacket(Packet_ packet) throws IOException {
        objectWriter.writeObject(packet);
        System.out.println("------Packet Sent");
        packet.printPacketDetails();
    }

    //adds the packet to the buffer
    public void addPacketToBuffer(Packet_ packet_) throws Exception {
        if (Server_Buffer.size() + 1 > buffer_size) {
            throw new Exception("Buffer size exceded");
        } else {
            Server_Buffer.add(packet_);
        }
    }

    //removes the packet from the buffer
    public Packet_ removePacketFromBuffer(int seq) {
        Packet_ ob = new Packet_();
        //traversing the buffer
        for (Packet_ packet_ : Server_Buffer) {
            if (packet_.getSeq() == seq) {
                //finding the packet with the same seq
                ob = packet_;
                Server_Buffer.remove(packet_);//removing the packet 
            }

        }
        return ob;//returning a copy of packet
    }

    //converts the given packet's data to 0 and ack flag to 1
    //increasing the sequence number by the bytes of data received
    static Packet_ generateAck(Packet_ packet_) throws Exception {
        byte size = 0;//stores the size of the data received
        packet_.setAckFlag(true);
        char ch[] = packet_.getData();

        //checking how much data is present in the packet
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
        packet_.setSeq(packet_.getSeq() + size+1);
        packet_.setAck((packet_.getSeq()));
        packet_.setData(ch);

        return packet_;
    }//tested and working correctly

    String charArrayToString(char characterArray[])
    {
        String line="";
        for(char ch:characterArray)
            line+=ch;
        return line;
    }
    
    public void communicate() throws Exception {
        DataInputStream in = new DataInputStream(System.in);
        //converting the server state from listening to active
        while (true) {
        //read the object sent by the client
            Packet_ packet_ = receivepacket();
            //condition to change the the server state from active to closed
            if (packet_.getData()[0] == Character.MAX_VALUE) {
                break;
            }
            
            
            //writing the received data to file
            BufferedWriter bw=new BufferedWriter(new FileWriter(new File("D:\\Users\\Dell-7560\\Documents\\NetBeansProjects\\Advanced Computer Networking\\src\\Experiment7\\Output2.txt"),true));
            bw.write(charArrayToString(packet_.getData()));
            
            
            
            packet_ = generateAck(packet_);
            System.out.println("Send ack ? press 1 for yes and 2 for no");
            
            boolean flag=true;//for handling the user input
            while (flag) {
                String input = in.readLine();
                switch (input) {
                    case "1":
                        sendPacket(packet_);
                        System.out.println("------ack send for the above packet");
                        flag=false;
                        break;
                    case "2":
                        packet_.setnAckFlag(true);
                        packet_.setAckFlag(false);
                        sendPacket(packet_);
                        System.out.println("------NAck not send for the above packet");
                        flag=false;
                        break;
                    default:
                        System.out.println("------Wrong input");
                        break;
                }
            }
            bw.close();
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
