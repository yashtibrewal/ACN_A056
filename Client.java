package Experiment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    //creating a buffer
    ArrayList<Packet_> Client_Buffer;
    int buffer_size;//buffer size 
    double cwnd;//congesstion window
    double ssthresh;//declaring the threshhold
    Socket socket;
    ObjectOutputStream objectWriter;
    DataOutputStream dout;
    ObjectInputStream objectReader;
    DataInputStream dis;

    public Client() throws IOException {
        //Connecting to the host with port 666
        socket = new Socket("localhost", 6666);
        Client_Buffer = new ArrayList<>();
        cwnd = 1.0;
        ssthresh = 4.0;
        buffer_size = 10;
        
        //Creating a input stream
        dis = new DataInputStream(socket.getInputStream());

        //creating a stream to accept objects using the input stream
        objectReader = new ObjectInputStream(dis);
        
        
        //creating an output stream
        dout = new DataOutputStream(socket.getOutputStream());

        //creating a stream for sending objects
        objectWriter = new ObjectOutputStream(socket.getOutputStream());
        
    }

    public void addPacketToBuffer(Packet_ packet_) {
        Client_Buffer.add(packet_);
    }

    //create packets 
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

    
     public void communicate() throws IOException, Exception
    {
            
            //creating dummy packets
        Packet_ p[]=new Packet_[10];
        for (int i=0;i<10;i++)
            p[i]=new Packet_();
            
        p[0].setData("First Packet".toCharArray());
        p[0].setSeq(16);
        p[1].setData("Second Packet".toCharArray());
        p[1].setSeq(33);
        p[2].setData("Third Packet".toCharArray());
        p[2].setSeq(49);
        p[3].setData("Fourth Packet".toCharArray());
        p[3].setSeq(66);
        p[4].setData("Fifth Packet".toCharArray());
        p[4].setSeq(78);
        p[5].setData("Sixth Packet".toCharArray());
        p[5].setSeq(90);
        p[6].setData("Seventh Packet".toCharArray());
        p[6].setSeq(104);
        p[7].setData("Eighth Packet".toCharArray());
        p[7].setSeq(117);
        p[8].setData("Ninth Packet".toCharArray());
        p[8].setSeq(119);
        p[9].setData("Tenth Packet".toCharArray());
        p[9].setSeq(131);
        
        
        
        
        //sending the packet / writing the packet on the stream
        for(int i=0;i<10;i++)
            objectWriter.writeObject(p[i]);
        
            
            //sending the packet / writing the packet on the stream
        for(int i=0;i<10;i++)
            objectWriter.writeObject(p[i]);
            
            
        dout.close();
    }

    public static void main(String[] args) throws IOException, Exception {

        Client client = new Client();
        client.communicate();

    }
}
