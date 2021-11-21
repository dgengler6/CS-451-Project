package cs451;

import java.util.*;

public class UniformReliableBroadcast implements Broadcast, Observer {

    private BestEffortBroadcast beb;
    private Observer observer;
    private Host self;

    private List<Host> correct;
    private List<Message> delivered;
    private List<Forward> forward;
    private Hashtable<AckMessage, Set<Integer>> ackMessage;

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
        AckMessage am = new AckMessage(message.getSenderId(), message.getSeqNbr());
        forward.add(new Forward(message.getSenderId(), am));
        beb.broadcast(message);
    }

    @Override
    public void deliver(Message message) {

        // We add the ack for message msg and for the person that forwarded it.
        AckMessage am = new AckMessage(message.getSenderId(), message.getSeqNbr());
        am.printAck();
        Set<Integer> ack_for_message = ackMessage.get(am);
        if(ack_for_message != null){
            ackMessage.get(am).add(message.getForwardId());
        }else{
            ackMessage.put(am, new HashSet<>(Arrays.asList(message.getForwardId())));
        }
        System.out.println(ack_for_message);
        System.out.println(ackMessage.get(am));
        // If ack[m] > N/2 we deliver the message
        if(canDeliver(message)){
            if(observer == null){
                OutputWriter.writeDeliver(message, true);
            } else {
                observer.deliver(message);
            }
        }

        // We check if we already forwarded the message, if not we send it again.
        Forward fwd = new Forward(message.getSenderId(), am);
        if (!forward.contains(fwd)){
            forward.add(fwd);
            beb.broadcast(message);
        }
    }


    private boolean canDeliver(Message message){
        Set<Integer> acks = ackMessage.get(new AckMessage( message.getSenderId(), message.getSeqNbr()));
        return acks != null && acks.size() > (correct.size() / 2);
    }
}

class AckMessage {
    //private int forwarder;
    private int originalSender;
    private int message;

    public AckMessage(int originalSender, int message){
        this.originalSender = originalSender;
        this.message = message;
    }

    public void printAck(){
        System.out.println(String.format("Ack for message %d originally sent by %d", message, originalSender));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this )
            return true;

        if (!(obj instanceof AckMessage))
            return false;

        return this.originalSender == ((AckMessage) obj).originalSender && this.message == ((AckMessage) obj).message;
    }
}

class Forward {

    public int sender;
    public AckMessage message;

    public Forward(int sender, AckMessage message){
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
