package cs451;

import java.util.ArrayList;

public class PerfectLinks implements Links {

    private String outputPath;
    private StubbornLinks stb;
    private ArrayList<Message> delivered;

    public PerfectLinks(String outputPath){
        this.outputPath = outputPath;
        this.stb = new StubbornLinks(outputPath);
        this.delivered = new ArrayList<>();

    }

    public void send(Message message){
        OutputWriter.writeBroadcast(message, outputPath);
        stb.send(message);
    }

    public void deliver(Message message){
        if (!delivered.contains(message)){
            stb.deliver(message);
            delivered.add(message);
        }
    }
}
