package cs451;

import java.util.ArrayList;

public class BestEffortBroadcast implements Broadcast, Observer {

    private PerfectLinks pl;
    private Observer observer;
    private Host self;
    private ArrayList<Host> hosts;

    public BestEffortBroadcast(ArrayList<Host> hosts, Host self){
        this.pl = new PerfectLinks(this);
        this.self = self;
        this.hosts = hosts;
    }

    public void broadcast(int message){
        for(int i = 0; i < hosts.size(); i++){
            Host dest = hosts.get(i);
            Message m = new Message(message, self.getId(), self.getIp(), self.getPort(), dest.getId(), dest.getIp(), dest.getPort(), "");
            pl.send(m);
        }

    }

    public void deliver(Message message){
        if(observer == null){
            OutputWriter.writeDeliver(message, true);
        } else {
            observer.deliver(message);
        }
    }
}
