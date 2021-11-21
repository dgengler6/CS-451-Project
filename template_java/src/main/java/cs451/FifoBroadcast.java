package cs451;

import java.util.ArrayList;
import java.util.List;

public class FifoBroadcast implements Broadcast, Observer {

    private UniformReliableBroadcast urb;
    private Observer observer;
    private Host self;

    public FifoBroadcast(List<Host> hosts, Host self, Observer observer){
        this.self = self;
        this.observer = observer;


        this.urb = new UniformReliableBroadcast(hosts, self, this);
    }

    @Override
    public void broadcast(Message message) {
        urb.broadcast(message);
    }

    @Override
    public void deliver(Message message) {

    }
}
