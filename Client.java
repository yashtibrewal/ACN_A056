package Experiment7;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

        //creating an output stream
        dout = new DataOutputStream(socket.getOutputStream());

        //creating a stream to accept objects using the input stream
        objectReader = new ObjectInputStream(dis);

        //creating a stream for sending objects
        objectWriter = new ObjectOutputStream(socket.getOutputStream());

        //the order of creation of these objects matter,
        //the program enters a deadlock/infinite loop state if we place the initialization of objectReader between dis and dout
    }

    public void addPacketToBuffer(Packet_ packet_) throws Exception {
        if (Client_Buffer.size() + 1 > buffer_size) {
            throw new Exception("Buffer size exceded");
        } else {
            Client_Buffer.add(packet_);
        }
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

    //sending the packet using the objectWriter channel
    void sendPacket(Packet_ packet_) throws IOException {
        objectWriter.writeObject(packet_);
    }

    Packet_ receivePacket() throws IOException, ClassNotFoundException {
        Packet_ packet_ = (Packet_) objectReader.readObject();

        if (packet_.getAck() == 1) {
            update_cwnd();
        }

        return packet_;
    }

    void update_cwnd() {
        if (cwnd < ssthresh) {
            cwnd += 1.0;
            System.out.println("cwnd updated to " + cwnd);
        } else {
            cwnd += 1 / cwnd;
            System.out.println("cwnd updated to " + cwnd);
        }
    }

    void timeout() {
        System.out.println("Time out triggered");
        ssthresh = cwnd / 2;
        System.out.println("ssthread updated to " + ssthresh);
        cwnd = 1;
        System.out.println("cwnd updated to " + cwnd);
    }

    public void communicate() throws IOException, Exception {

        //creating dummy packets
        Packet_ p[] = new Packet_[10];
        for (int i = 0; i < 10; i++) {
            p[i] = new Packet_();
        }

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

        //sending and reciving the packets the packet on the stream
        for (int i = 0; i < 10; i++) {
            sendPacket(p[i]);
            Packet_ packet_;
            packet_ = receivePacket();
            
            packet_.printPacketDetails();
        }
        
        
        //creating a "closing" packet
        Packet_ packet_ = new Packet_();
        char ch[] = new char[20];
        ch[0] = Character.MAX_VALUE;
        packet_.setData(ch);
        sendPacket(packet_);//sending a packet to identify a close connection

        dout.close();
    }

    public static void main(String[] args) throws IOException, Exception {

        Client client = new Client();
        client.communicate();

    }
}
