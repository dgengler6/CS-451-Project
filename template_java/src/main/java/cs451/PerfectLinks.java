package cs451;

import java.util.ArrayList;

public class PerfectLinks implements Links {

    private StubbornLinks stb;
    private ArrayList<Message> delivered;

    public PerfectLinks(String outputPath){

        this.stb = new StubbornLinks(outputPath);
        this.delivered = new ArrayList<>();

    }

    public void send(Message message){
        stb.send(message);
    }

    public void deliver(Message message){
        if (!delivered.contains(message)){
            stb.deliver(message);
            delivered.add(message);
        }
    }
}
