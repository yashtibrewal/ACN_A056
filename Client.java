package Experiment7;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    //creating a buffer
    ArrayList<Packet_> Client_Buffer;
    double cwnd;//congesstion window
    double ssthresh;//declaring the threshhold
    Socket socket;
    
    public Client() throws IOException {
        socket=new Socket("localhost",6666);
        Client_Buffer = new ArrayList<>();
        cwnd = 1.0;
        ssthresh = 4.0;
    }

    public void addPacketToBuffer(Packet_ packet_) {
        Client_Buffer.add(packet_);
    }

    public Packet_ removePacketFromBuffer(int seq) {
        Packet_ ob = new Packet_();
        for (Packet_ packet_ : Client_Buffer) {
            if (packet_.getSeq() == seq) {
                ob = packet_;
                Client_Buffer.remove(packet_);//removing the packet with sequnce number mentioned in the parameter
            }

        }
        return ob;//returning a copy of packet
    }

    void sendPacket(Packet_ ob) {
    }

    Packet_ receivePacket() {
        Packet_ dummy = new Packet_();

        if (dummy.getAck() == 1) {
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

    //create a List
    //pushing a packet to the list
    //send packets from the list
    //remove the packet from the list if ack received.
    
    public void communicate() throws IOException
    {
        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            dout.writeUTF("Hello Server");

            //creating a stream for sending objects
            ObjectOutputStream objectWriter = new ObjectOutputStream(socket.getOutputStream());

            //creating a dummy packet to test the sending
            Packet_ packet = new Packet_();
            char ch[];
            String temp = "Text Packet";
            ch = temp.toCharArray();
//        ch=new char[]{'T','e','s','t',' ','P','a','c','k','e','t',' ',' ',' ',' ',' ',' ',' ',' ',' '};
            packet.setData(ch);
            packet.setSeq(1);
            packet.setAck((byte) 1);

            //sending the packet / writing the packet on the stream
            objectWriter.writeObject(packet);
            dout.close();
    }
    
    public static void main(String[] args) throws IOException {
    
            Client client=new Client();
            client.communicate();

        
    }
}
