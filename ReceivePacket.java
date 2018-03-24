package Experiment7;

import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceivePacket implements Runnable{
    
    ObjectInputStream objectReader;
    Packet_ packet_;
    Packet_ receivepacket() throws Exception {
        //reading the packet from the stream
        Packet_ packet = (Packet_) objectReader.readObject();
        System.out.println("------Packet Received");
        packet.printPacketDetails();
        return packet;//returning the read packet
    }

    @Override
    public void run() {
        try {
            packet_=receivepacket();
        } catch (Exception ex) {
            System.err.println(ex);
            Logger.getLogger(ReceivePacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
