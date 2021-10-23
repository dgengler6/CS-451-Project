package cs451;

import java.io.IOException;
import java.net.*;

public class StubbornLinks {

    private String outputPath;
    private FairLossLinks fll;

    public StubbornLinks(String outputPath){
        this.fll = new FairLossLinks(outputPath);
        this.outputPath = outputPath;
    }

    public void stubbornSend(int message, String ip, int port, int duratiion){
        byte[] buf;
        OutputWriter.writeBroadcast(message, outputPath);
        while(true) {
            try {
                DatagramSocket socket = new DatagramSocket();
                buf = (""+message).getBytes();
                DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName(ip), port);
                //= new DatagramPacket(buf, buf.length, ip, port);
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("Error in stubbornSend " + e);
            }
        }
    }

    public void stubbornDeliver(int message, int src){
        fll.fairLossDeliver(message, src);
    }
}
