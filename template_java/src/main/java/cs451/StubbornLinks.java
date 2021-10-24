package cs451;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class StubbornLinks implements Links{

    private FairLossLinks fll;
    private ArrayList<Message> sent;
    private int delay = 5000;

    public StubbornLinks(String outputPath){
        this.fll = new FairLossLinks(outputPath);
        this.sent = new ArrayList<>();

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

    }

    public void send(Message message){
        fll.send(message);
        sent.add(message);

    }

    public void stubbornSend(){
        System.out.println(String.format("Periodic Resend of %d messages", sent.size()));
        for(int i=0;i<sent.size();i++){
            fll.send(sent.get(i));
        }
    }

    public void deliver(Message message){
        fll.deliver(message);
        fll.send(new Ack(message));
    }

    public void handleAck(Ack ack){
        ack.printMessage();
        sent.remove(ack.getMessage());
    }


}
