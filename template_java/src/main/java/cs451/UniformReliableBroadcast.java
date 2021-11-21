package cs451;

import java.util.*;

public class UniformReliableBroadcast implements Broadcast, Observer {

    private BestEffortBroadcast beb;
    private Observer observer;
    private Host self;

    private List<Host> correct;
    private List<Message> delivered;
    private List<Forward> forward;
    private Hashtable<Message, Set<Integer>> ackMessage;

    public UniformReliableBroadcast(List<Host> hosts, Host self, Observer observer){
        this.self = self;
        this.observer = observer;

        this.correct = hosts;
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
    public void deliver(Message message) {

        // We add the ack for message msg and for the person that forwarded it.
        Set<Integer> ack_for_message = ackMessage.get(message);
        if(ack_for_message != null){
            ack_for_message.add(message.getForwardId());
        }else{
            ackMessage.put(message, new HashSet<>(message.getForwardId()));
        }
        System.out.println(ack_for_message);
        System.out.println(forward);
        // If ack[m] > N/2 we deliver the message
        if(canDeliver(message)){
            if(observer == null){
                OutputWriter.writeDeliver(message, true);
            } else {
                observer.deliver(message);
            }
        }

        // We check if we already forwarded the message, if not we send it again.
        Forward fwd = new Forward(message.getForwardId(), message);
        if (!forward.contains(fwd)){
            forward.add(fwd);
            beb.broadcast(message);
        }
    }


    private boolean canDeliver(Message message){
        return ackMessage.get(message).size() > (correct.size() / 2);
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
