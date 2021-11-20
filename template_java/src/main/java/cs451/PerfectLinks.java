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

    public void send(Message message){
        OutputWriter.writeBroadcast(message, true);
        stb.send(message);
    }

    public void deliver(Message message){
        if (!delivered.contains(message)){
            if(observer == null){
                OutputWriter.writeDeliver(message, true);
            } else {
                observer.deliver(message);
            }
            delivered.add(message);
        }
    }

    public void handleAck(Ack ack) { }

}
