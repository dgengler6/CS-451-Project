package cs451;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BestEffortBroadcast implements Broadcast, Observer {

    private PerfectLinks pl;
    private Observer observer;
    private Host self;
    private Set<Host> hosts;

    public BestEffortBroadcast(Set<Host> hosts, Host self, Observer observer){
        this.pl = new PerfectLinks(this);
        this.self = self;
        this.observer = observer;
        this.hosts = hosts;
    }

    // TODO: Potentially use the iterator of the Set.

    @Override
    public void broadcast(Message message){
        // Says that the forwarder is itself.
        Message message_fwd = message.updateForwardInfos(self.getId(), self.getIp(), self.getPort());

        // Get the hosts as a list and send the message to all of them.
        Host[] hostList = hosts.toArray(new Host[0]);
        System.out.println(hostList);
        if(observer == null){
            OutputWriter.writeBroadcast(message, true);
        }
        for(int i = 0; i < hostList.length; i++){
            Host dest = hostList[i];
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
