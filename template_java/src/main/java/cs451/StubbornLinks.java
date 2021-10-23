package cs451;

import java.io.IOException;
import java.net.*;

public class StubbornLinks implements Links{

    private String outputPath;
    private FairLossLinks fll;

    public StubbornLinks(String outputPath){
        this.fll = new FairLossLinks(outputPath);
        this.outputPath = outputPath;
    }

    public void send(Message message){
        byte[] buf;
        while(true) {

        }
    }

    public void deliver(Message message){
        fll.deliver(message);
    }
}
