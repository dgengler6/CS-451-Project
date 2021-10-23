package cs451;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessageListener {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private int duration = 10000;

    /*
        UDP listener on port "port" for "duration" milliseconds
     */
    public MessageListener(int port, int duration) {
        try {
            this.duration = duration;
            socket = new DatagramSocket(port);
        } catch( SocketException e){
            System.out.println("Socket Error " + e);
        }
    }

    public void run() {
        running = true;

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                System.out.println(packet);
                String received
                        = new String(packet.getData(), 0, packet.getLength());

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(received.getBytes()));
                try {
                    Message message = (Message) iStream.readObject();
                    System.out.println(message.getSeqNbr()+ " "+ message.getSenderId() + " " + message.getDestId());
                }catch(ClassNotFoundException e){
                    System.out.println("Error while deserializing "+e);
                }
                iStream.close();
            }catch(IOException e){
                System.out.println("I/O Error " + e);
            }

        }
        socket.close();
    }
}
