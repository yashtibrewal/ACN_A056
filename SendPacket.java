package Experiment7;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendPacket implements Runnable{
    ObjectOutputStream objectWriter;
    Packet_ packet_;
    //function to write the object to the stream
    void sendPacket(Packet_ packet) throws IOException {
        objectWriter.writeObject(packet);
        System.out.println("------Packet Sent");
        packet.printPacketDetails();
    }

    @Override
    public void run() {
        try {
            sendPacket(packet_);
        } catch (IOException ex) {
            System.err.println(ex);
            Logger.getLogger(SendPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
