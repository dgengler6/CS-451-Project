package cs451;

import java.util.*;

public class UniformReliableBroadcast implements Broadcast, Observer {

    private BestEffortBroadcast beb;
    private Observer observer;
    private Host self;

    private Set<Host> correct;
    private List<Message> delivered;
    private List<Forward> forward;
    private Hashtable<Message, Set<Integer>> ackMessage;

    public UniformReliableBroadcast(List<Host> hosts, Host self, Observer observer){
        this.self = self;
        this.observer = observer;

        this.correct = new HashSet<Host>(hosts);
        this.delivered = new ArrayList<>();
        this.forward = new ArrayList<>();
        this.ackMessage = new Hashtable<>();

        this.beb = new BestEffortBroadcast(this.correct, self, this);
    }

    @Override
    public void broadcast(Message message) {
        forward.add(new Forward(this.self.getId(), message));
        beb.broadcast(message);
    }

    @Override
    public void deliver(Message msg) {
        // We add the ack for message msg and for the person that forwarded it.
        Set<Integer> acked = ackMessage.get(msg);
        if(acked != null){
            acked.add(msg.getForwardId());
        }else{
            ackMessage.put(msg, new HashSet<>(msg.getForwardId()));
        }

        // We check if we already forwarded the message, if not we send it again.
        Forward fwd = new Forward(msg.getForwardId(), msg);
        if (!forward.contains(fwd)){
            forward.add(fwd);
            beb.broadcast(msg);
        }
    }

    private void checkDeliverable(){

    }
}

class Forward {

    public int sender;
    public Message message;

    public Forward(int sender, Message message){
        this.sender = sender;
        this.message = message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this )
            return true;

        if (!(obj instanceof Forward))
            return false;

        return this.sender == ((Forward) obj).sender && this.message.equals(((Forward) obj).message);
    }
}
