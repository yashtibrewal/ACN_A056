
package Experiment7;

import java.io.Serializable;
import java.util.Arrays;


public class Packet_ implements Serializable{
    private char data[]=new char[20];
    //assume the size is fixed to 20 characters
    private byte ack;
    private int seq;
    Packet_()
    {
    }
    
    public void printPacketDetails()
    {
        System.out.println("data "+Arrays.toString(data));
        System.out.println("ack "+ack);
        System.out.println("number "+seq);
    }

    public void setData(char[] data) {
        this.data = data;
    }

    public void setAck  (byte ack) {
        this.ack = ack;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public char[] getData() {
        return data;
    }

    public byte getAck() {
        return ack;
    }

    public int getSeq() {
        return seq;
    }
}
