
package Experiment7;

import java.io.Serializable;


public class Packet_ implements Serializable{
    String data;
    byte ack;
    int number;
    void printPacketDetails()
    {
        System.out.println("data "+data);
        System.out.println("ack "+ack);
        System.out.println("number "+number);
    }
}
