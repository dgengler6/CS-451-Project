package cs451;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MessageListener implements Runnable {

    private DatagramSocket socket;
    private byte[] buf = new byte[256];
    private int duration = 10000;
    private Links link;

    /*
        UDP listener on port "port" for "duration" milliseconds, using "link" for connection
     */
    public MessageListener(int port, int duration, Links link) {
        try {
            this.duration = duration;
            this.link = link;
            socket = new DatagramSocket(port);
        } catch( SocketException e){
            System.out.println("Socket Error " + e);
        }
    }

    public void run() {

        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                try {
                    Message message = (Message) iStream.readObject();
                    link.deliver(message);
                    //message.printMessage();
                }catch(ClassNotFoundException e){
                    System.out.println("Error while deserializing "+e);
                }


                iStream.close();
            }catch(IOException e){
                System.out.println("I/O Error " + e);
            }

        }
    }
}
