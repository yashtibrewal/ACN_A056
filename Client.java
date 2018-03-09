package Experiment7;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    Packet_ receivepacket() {
        Packet_ dummy = new Packet_();
        return dummy;
    }

    void sendPacket(Packet_ p) {
    }

    void generateAck(Packet_ p) {
        p.ack = 1;
        p.data = "";
    }

    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("localhost", 6666)) {
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            dout.writeUTF("Hello Server");

        
        //creating a stream for sending objects
        ObjectOutputStream objectWriter=new ObjectOutputStream(socket.getOutputStream());
        
        //creating a dummy packet to test the sending
        Packet_ packet=new Packet_();
        packet.data="Testing the packet";
        packet.number=1;
        packet.ack=0;

        
        //sending the packet / writing the packet on the stream
        objectWriter.writeObject(packet);

            dout.flush();
            dout.close();
        }
    }
}
