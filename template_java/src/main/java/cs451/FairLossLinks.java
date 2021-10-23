package cs451;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FairLossLinks implements Links{

    private String outputPath;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public FairLossLinks(String outputPath){
        this.outputPath = outputPath;
    }

    public void send(Message message){
        byte[] buf;
        OutputWriter.writeBroadcast(message, outputPath);
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

    public void deliver(Message message){
        OutputWriter.writeDeliver(message, outputPath);
    }
}
