package cs451;

import java.util.List;

public class BestEffortBroadcast implements Broadcast, Observer {

    private PerfectLinks pl;
    private Observer observer;
    private Host self;
    private List<Host> hosts;

    public BestEffortBroadcast(List<Host> hosts, Host self, Observer observer){
        this.pl = new PerfectLinks(this);
        this.self = self;
        this.observer = observer;
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
