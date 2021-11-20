package cs451;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FairLossLinks implements Links{

    private Links observer;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public FairLossLinks(Links observer){
        this.observer = observer;
        MessageListener ml = new MessageListener(this);
        new Thread(ml).start();
    }

    @Override
    public void send(Message message){
        System.out.println(String.format("Sending message %d, on link %s",message.getSeqNbr(),"FLL"));
        byte[] buf;
        try {
            socket = new DatagramSocket();

            // Serialize our Message to send it over network
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(message);
            oo.close();

            buf = bStream.toByteArray();
            packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName(message.getDestIp()), message.getDestPort());
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Error in fairLossSend " + e);
        }

    }

    @Override
    public void deliver(Message message){
        System.out.println(String.format("Delivering message %d, on link %s",message.getSeqNbr(),"FLL"));
        if(observer == null){
            OutputWriter.writeDeliver(message, true);
        } else {
            observer.deliver(message);
        }

    }

    @Override
    public void handleAck(Ack ack) {
        if(this.observer != null) {
            observer.handleAck(ack);
        }

    }
}
