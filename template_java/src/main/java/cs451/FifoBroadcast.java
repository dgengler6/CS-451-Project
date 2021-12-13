package cs451;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FifoBroadcast implements Broadcast, Observer {

    private UniformReliableBroadcast urb;
    private Observer observer;
    private Host self;
    private Map<Integer, Integer> next;
    private List<Message> toBeDelivered;

    public FifoBroadcast(List<Host> hosts, Host self, Observer observer) {
        this.self = self;
        this.observer = observer;

        this.next = new HashMap<>();
        for (Host host : hosts) {
            next.put(host.getId(), 1);
        }
        this.toBeDelivered = new ArrayList<>();

        this.urb = new UniformReliableBroadcast(hosts, self, this);
    }

    @Override
    public void broadcast(Message message) {
        if (observer == null && message.getSenderId() == self.getId()) {
            OutputWriter.writeBroadcast(message, true);
        }
        urb.broadcast(message);
    }

    @Override
    public void deliver(Message message) {
        int sender = message.getSenderId();
        toBeDelivered.add(message);

        while (existsDeliverable(toBeDelivered, sender)) {
            next.replace(sender, next.get(sender) + 1);
        }
        /*
        int nb_delivered;
        do{
            // Check how much messages we can deliver and we deliver them.
            nb_delivered = existsDeliverableInt(toBeDelivered, sender);
        }while(nb_delivered > 0);*/
    }

    public boolean existsDeliverable(List<Message> l, int sender) {
        for (Message m : l) {
            if (checkDeliverable(m, sender)) {
                if (observer == null) {
                    OutputWriter.writeDeliver(m, true);
                } else {
                    observer.deliver(m);
                }
                toBeDelivered.remove(m);
                return true;
            }
        }
        return false;
    }

    public int existsDeliverableInt(List<Message> l, int sender) {
        int nb_delivered = 0;
        List<Message> delivered = new ArrayList<>();
        //Deliver all messages that can be delivered.
        for (Message m : l) {
            if (checkDeliverable(m, sender)) {
                if (observer == null) {
                    OutputWriter.writeDeliver(m, true);
                } else {
                    observer.deliver(m);
                }
                next.replace(sender, next.get(sender) + 1);
                delivered.add(m);
                nb_delivered += 1;
            }
        }
        //Remove all delivered messages from the list of messages to be delivered.
        for (Message m : delivered) {
            toBeDelivered.remove(m);
        }
        return nb_delivered;
    }

    public boolean checkDeliverable(Message m, int sender) {
        return m.getSenderId() == sender && m.getOrder() == next.get(sender);
    }
}
