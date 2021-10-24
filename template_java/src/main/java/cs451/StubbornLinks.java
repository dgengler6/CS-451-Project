package cs451;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class StubbornLinks implements Links{

    private FairLossLinks fll;
    private ArrayList<Message> sent;
    private int delay = 10000;

    public StubbornLinks(String outputPath){
        this.fll = new FairLossLinks(outputPath);
        this.sent = new ArrayList<>();
        this.delay = delay;

        System.out.println("Starting periodic resend of all sent messages thread");
        Thread t = new Thread() {
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
        t.start();
    }

    public void send(Message message){
        fll.send(message);
        sent.add(message);

    }

    public void stubbornSend(){
        System.out.println("Periodic Resend of all messages");
        for(int i=0;i<sent.size();i++){
            fll.send(sent.get(i));
        }
    }

    public void deliver(Message message){
        fll.deliver(message);
    }

    // We might want to remove the messages at some point as well.
    
}
