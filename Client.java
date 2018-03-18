
    /**assumption:
    *the packet for the the acknowledgment is not received
    is a packet received with nAckFlag as true - for simulation purpose
    * generally there is a timer for no acknowledgment which in this case
    is the user input on the server side
    *retransmitting the packet for which the acknowledged packet is not received
    is not included in the scope of simulation*/


    package Experiment7;

    import java.io.BufferedReader;
    import java.io.DataInputStream;
    import java.io.DataOutputStream;
    import java.io.File;
    import java.io.FileReader;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.net.Socket;
    import java.util.ArrayList;

    public class Client {

        //creating a buffer
        ArrayList<Packet_> clientBuffer;
        int bufferSize;//buffer size 
        double cwnd;//congesstion window
        double ssthresh;//declaring the threshold
        Socket socket;
        ObjectOutputStream objectWriter;
        DataOutputStream dataOut;
        ObjectInputStream objectReader;
        DataInputStream dataIn;
        int index;//too keep a track of file data read
        boolean endOfFile;

        public Client() throws IOException {

            index = 0;
            endOfFile = true;
            //Connecting to the host with port 666
            socket = new Socket("localhost", 6666);
            clientBuffer = new ArrayList<>();
            cwnd = 1.0;
            ssthresh = 4.0;
            bufferSize = 5;

            //Creating a input stream
            dataIn = new DataInputStream(socket.getInputStream());

            //creating an output stream
            dataOut = new DataOutputStream(socket.getOutputStream());

            //creating a stream to accept objects using the input stream
            objectReader = new ObjectInputStream(dataIn);

            //creating a stream for sending objects
            objectWriter = new ObjectOutputStream(socket.getOutputStream());

            //the order of creation of these objects matter,
            //the program enters a deadlock/infinite loop state if we place
            //the initialization of objectReader between dis and dout initialization
        }

        public void addPacketToBuffer(Packet_ packet_) throws Exception {
            if (clientBuffer.size() + 1 > bufferSize) {
                throw new Exception("Buffer size exceded");
            } else {
                clientBuffer.add(packet_);
            }
        }

        //create packets 
        public Packet_ removePacketFromBuffer(int seq) {
            Packet_ ob = new Packet_();
            for (Packet_ packet_ : clientBuffer) {
                if (packet_.getSeq() == seq) {
                    ob = packet_;
                    //removing the packet with the mentioned sequnce number
                    clientBuffer.remove(packet_);
                }

            }
            return ob;//returning a copy of packet
        }

        //sending the packet using the objectWriter channel
        void sendPacket(Packet_ packet_) throws IOException {
            System.out.println("------Packet Sent");
            packet_.printPacketDetails();
            objectWriter.writeObject(packet_);
        }

        //function to read the packet and perform other operations
        Packet_ receivePacket() throws IOException, ClassNotFoundException {
            Packet_ packet_ = (Packet_) objectReader.readObject();
            System.out.println("------Packet Received");
            packet_.printPacketDetails();
            //if the ack flag is ture the congesstion windows updates
            if (packet_.isAckFlag()) {
                update_cwnd();
            }

            return packet_;
        }

        //function used to upadte the congestion window according to sender threshold
        void update_cwnd() {
            if (cwnd < ssthresh) {
                cwnd += 1.0;
                System.out.println("--------cwnd updated to " + cwnd);
            } else {
                cwnd += 1 / cwnd;
                System.out.println("--------cwnd updated to " + cwnd);
            }
        }

        //function called during the event of timeout
        void timeout() {
            System.out.println("-------Time out triggered");
            ssthresh = cwnd / 2;//threshold is havled
            System.out.println("-------ssthread updated to " + ssthresh);
            cwnd = 1;//congession windows is changes back to 1
            System.out.println("-------cwnd updated to " + cwnd);
        }

        //reads a packet from an index and returns it
        boolean readPacketFromFile(File ob, int seq) throws Exception {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ob));

            //create a packet object
            Packet_ packet_ = new Packet_();
            packet_.setSeq(seq);

            //fill the buffer with packets
            //check whether there is a place for 1 packet to be added
            if (clientBuffer.size() + 1 < bufferSize) {

                //moves the present cursor to the index position
                for (int i = 0; i < index; i++) {
                    bufferedReader.read();//traverse the data till index position
                }
                index++;//index position
                //reads the first character after traversing till index position
                char ch = (char) bufferedReader.read();
                char characterArray[] = new char[20];
                if (ch == '\uffff')//if there is no character exit the execution
                {
                    return false;
                }
                characterArray[0] = ch;
                for (int i = 1; i < 20 && ch != -1; i++) {
                    ch = (char) bufferedReader.read();
                    if (ch == '\uffff') {
                        break;//if end of file reached while reading the 20 characters
                    }
                    index++;
                    characterArray[i] = ch;
                }
                packet_.setData(characterArray);
                //add to buffer
                clientBuffer.add(packet_);
            }
            return true;
        }//tested working correctly

        public void communicate() throws IOException, Exception {

            //creating dummy packets
            /*Packet_ p[] = new Packet_[10];
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
            p[9].setSeq(131);*/
            File file = new File("D:\\Users\\Dell-7560\\Documents\\NetBeansProjects\\Advanced Computer Networking\\src\\Experiment7\\Data2.txt");

            //sending and reciving the packets the packet on the stream
            int seq = 4000;
            while (true) {
                Packet_ packet_ = new Packet_();
                //sending the packets
                if (!readPacketFromFile(file, seq)) {
                    System.err.println("End of file");
                    break;
                }
                sendPacket(clientBuffer.get(0));//sending the first element from
                //buffer
                clientBuffer.remove(0);//removing the first element from buffer
                //receiving the packets
                packet_ = receivePacket();
                if (packet_.isAckFlag())//if acknowledge comes
                {
                    seq = packet_.getAck();//set the new sequence to its ack number
                }
                if (packet_.isnAckFlag()) {
                    timeout();
                }
            }

            //creating a "closing" packet
            Packet_ packet_ = new Packet_();
            char ch[] = new char[20];
            ch[0] = Character.MAX_VALUE;
            packet_.setData(ch);
            //sending a packet to identify a close connection
            sendPacket(packet_);

            dataOut.close();
        }

        public static void main(String[] args) throws IOException, Exception {

            Client client = new Client();
            client.communicate();

            //Testing the read packets from a file funtion
            /*Packet_ packet_;
            packet_=client.readPacketFromFile(new File("D:\\Users\\Dell-7560\\Documents\\NetBeansProjects\\Advanced Computer Networking\\src\\Experiment7\\Data.txt"), 4000);
            packet_.printPacketDetails();*/
        }
    }