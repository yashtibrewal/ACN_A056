package Experiment7;

import java.util.ArrayList;

/**
 *
 * @author Dell-7560
 */
public class Buffer {
    ArrayList<Packet_> buffer;//creating an array list for buffer
    int bufferSize;

    public Buffer(int size) {
        buffer=new ArrayList<>();
        bufferSize=size;
    }
    
    
    //returns true is the packet is added else returns a false
    boolean addPacket(Packet_ packet_){
        if(buffer.size()+1>bufferSize)
            return false;
        else {
            buffer.add(packet_);
            return true;
        }
        
    }
    
    //returns true if the packet is removed else false
    boolean removePacket(int seq){
        if(buffer.isEmpty())
            return false;
        else
        {
            for(Packet_ packet_:buffer){
                if(packet_.getSeq()==seq)
                    buffer.remove(packet_);
            }
            return true;
        }
    }
    //checks for a packet if present in buffer
    boolean checkPacket(Packet_ packet_){
        boolean flag=false;
        for(Packet_ packet_1:buffer){
            if( Packet_.samePacket(packet_1, packet_))
                flag=true;
        }
        return flag;
    }
    
}
