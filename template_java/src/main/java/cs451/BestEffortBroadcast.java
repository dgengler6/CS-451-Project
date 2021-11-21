package cs451;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public void broadcast(Message message){
        // Says that the forwarder is itself.
        Message message_fwd = message.updateForwardInfos(self.getId(), self.getIp(), self.getPort());

        if(observer == null){
            OutputWriter.writeBroadcast(message, true);
        }
        for(int i = 0; i < hosts.size(); i++){
            Host dest = hosts.get(i);
            Message message_dest = message_fwd.updateDestInfos(dest.getId(), dest.getIp(), dest.getPort());
            pl.send(message_dest);
        }
    }

    @Override
    public void deliver(Message message){
        if(observer == null){
            OutputWriter.writeDeliver(message, true);
        } else {
            observer.deliver(message);
        }
    }
}
