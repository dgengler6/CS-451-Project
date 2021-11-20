package cs451;

import java.util.List;

public class UniformReliableBroadcast implements Broadcast, Observer {

    private BestEffortBroadcast beb;
    private Observer observer;

    public List<Host> correct;
    private List<Message> delivered;
    private List<Message> forward;

    public UniformReliableBroadcast(List<Host> hosts, Host self, Observer observer){
        this.correct = hosts;
        //this.beb = new BestEffortBroadcast(this.correct, self, this);
        this.observer = observer;
    }

    @Override
    public void broadcast(int msg) {

    }

    @Override
    public void deliver(Message msg) {

    }
}
