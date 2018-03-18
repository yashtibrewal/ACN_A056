package Experiment7;

import java.io.Serializable;
import java.util.Arrays;

public class Packet_ implements Serializable {

    //using Packet_ becuase Packet already exixsts in jnetpcap library
    private char data[] = new char[20];//data field
    //assume the size is fixed to 20 characters

    private int ack;//acknowledgement number
    private int seq;//sequence number
    private boolean ackFlag;
    private boolean nAckFlag;

    public void setnAckFlag(boolean nAckFlag) {
        this.nAckFlag = nAckFlag;
    }

    public boolean isnAckFlag() {
        return nAckFlag;
    }

    public void setAckFlag(boolean ackFlag) {
        this.ackFlag = ackFlag;
    }

    public boolean isAckFlag() {
        return ackFlag;
    }

    Packet_() {
        ackFlag = false;
        ack = 0;
        //making the characters in the packet 0
        for (char d : data) {
            d = Character.MIN_VALUE;
        }
    }

    public void printPacketDetails() {
        if (nAckFlag) {
            System.out.println("No acknowlegement received");
            return;
        }
        System.out.println("----- Packet Details : ----");
        System.out.println("data " + Arrays.toString(data));
        if (isAckFlag()) {
            System.out.println("ack " + ack);
        }
        if (!isAckFlag()) {
            System.out.println("sequence number " + seq);
        }
    }

    public void setData(char[] data) throws Exception {
        if (data.length > 20) {
            System.out.println("Cannot have data more than 20 characters in a packet");
            throw new Exception("Cannot have data more than 20 characters in a packet");
        }
        int i = 0;
        for (; i < data.length; i++) {
            this.data[i] = data[i];
        }
        for (; i < 20; i++) {
            this.data[i] = Character.MIN_VALUE;
        }
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public char[] getData() {
        return data;
    }

    public int getAck() {
        return ack;
    }

    public int getSeq() {
        return seq;
    }
}
