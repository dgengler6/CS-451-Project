package cs451;

import java.util.ArrayList;

public class PerfectLinks implements Links, Observer {

    private StubbornLinks stb;
    private Observer observer;
    private ArrayList<Message> delivered;

    public PerfectLinks(Observer observer){
        this.stb = new StubbornLinks(this);
        this.observer = observer;
        this.delivered = new ArrayList<>();

    }

    @Override
    public void send(Message message){
        System.out.println(String.format("Sending message %d, on link %s",message.getSeqNbr(),"PL"));
        if(observer == null){
            OutputWriter.writeBroadcast(message, true);
        }
        stb.send(message);
    }

    @Override
    public void deliver(Message message){
        System.out.println(String.format("Delivering message %d, on link %s",message.getSeqNbr(),"PL"));
        if (!delivered.contains(message)){
            if(observer == null){
                OutputWriter.writeDeliver(message, true);
            } else {
                observer.deliver(message);
            }
            delivered.add(message);
        }
    }

    @Override
    public void handleAck(Ack ack) { }

}
