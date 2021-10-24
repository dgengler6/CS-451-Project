package cs451;

import sun.nio.ch.DatagramSocketAdaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class StubbornLinks implements Links{

    private FairLossLinks fll;
    private ArrayList<Message> sent;
    private int delay = 5000;
    private int port;

    public StubbornLinks(String outputPath, int port){
        this.fll = new FairLossLinks(outputPath);
        this.sent = new ArrayList<>();
        this.port = port;

        System.out.println("Starting periodic resend of all sent messages thread");
        Thread resendThread = new Thread() {
            public void run(){
                while (true) {
                    try{
                        stubbornSend();
                        Thread.sleep(delay);
                    }catch(InterruptedException e){
                        System.out.println("Thread error in Stubborn link timer "+ e);
                    }

                }
            }
        };
        resendThread.start();
        Thread ackThread = new Thread() {
            public void run(){
                recieveAcks();
            }
        };
        ackThread.start();
    }

    public void send(Message message){
        fll.send(message);
        sent.add(message);

    }

    public void stubbornSend(){
        System.out.println("Periodic Resend of messages");
        for(int i=0;i<sent.size();i++){
            fll.send(sent.get(i));
        }
    }

    public void deliver(Message message){
        fll.deliver(message);
        fll.send(new Ack(message));
    }

    private void recieveAcks(){
        DatagramSocket socket;
        byte[] buf = new byte[256];
        try {
            socket = new DatagramSocket(port);
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
                        if(message instanceof Ack){
                            message.printMessage();
                            sent.remove(((Ack) message).getMessage());
                        }

                    }catch(ClassNotFoundException e){
                        System.out.println("Error while deserializing "+e);
                    }


                    iStream.close();
                }catch(IOException e){
                    System.out.println("I/O Error " + e);
                }

            }
        }catch(SocketException e){
            System.out.println("Socket error "+ e);
        }

    }

}
