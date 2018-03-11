/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiment7;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Dell-7560
 */
public class PracticeLists {

    static ArrayList<Packet_> mylist = new ArrayList<>();
    public static void main(String[] args) {
        Packet_ ob[] = new Packet_[3];
        int i = 1;
        for (Packet_ o : ob) {
            o = new Packet_();
            o.setData(("Sample " + i + " ").toCharArray());
            o.setSeq(i);
            mylist.add(o);
            i++;
        }
        Packet_ objePacket_=new Packet_();
        objePacket_.setSeq(2);
        objePacket_.setData(("Sample " + i + " ").toCharArray());
        mylist.remove(objePacket_);
        for (Packet_ o : mylist) {
//            System.out.println(o.getSeq());
            if(o.getSeq()==2)
                mylist.remove(o);
        }
        for (Packet_ o : mylist) {
            System.out.println(Arrays.toString(o.getData()));
            System.out.println(o.getSeq());
            
        }
    }
}
