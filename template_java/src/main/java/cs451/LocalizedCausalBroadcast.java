package cs451;

import java.util.List;
import java.util.ArrayList;

public class LocalizedCausalBroadcast implements Broadcast, Observer {

    private UniformReliableBroadcast urb;
    private Observer observer;
    private Host self;
    private List<Message> pending;
    private List<Message> tmpPending;
    private int[] vectorClock;
    private int delay = 100;

    LocalizedCausalBroadcast(List<Host> hosts, Host self, Observer observer, int[][] peersConfig) {
        this.self = self;
        this.observer = observer;

        //Initialize the vector clock for all other hosts.
        this.vectorClock = new int[hosts.size()];

        this.pending = new ArrayList<>();
        this.tmpPending = new ArrayList<>();

        this.urb = new UniformReliableBroadcast(hosts, self, this);

        System.out.println("Starting periodic execution of deliverPending");
        Thread deliverPendingThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        deliverPending();
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        System.out.println("Thread error in Stubborn link timer " + e);
                    }
                }
            }
        };
        deliverPendingThread.start();

    }

    @Override
    public void broadcast(Message message) {
        // Set message's vector clock.
        message.setVectorClock(this.vectorClock);

        // Broadcast message
        if (observer == null && message.getSenderId() == self.getId()) {
            OutputWriter.writeBroadcast(message, true);
        }
        urb.broadcast(message);

        // Update self VC
        vectorClock[self.getId() - 1] += 1;
    }

    @Override
    public void deliver(Message message) {
        message.printMesssageVectorClock();
        printVectorClock();
        // Here we add messages to a temporary array in order to avoid conflicts in deliverPending Thread.
        tmpPending.add(message);

    }

    /**
     * Check pending messages and deliver them if the our vector clock is greater than theirs.
     */
    public void deliverPending() {
        List<Message> delivered = new ArrayList<>();

        // Add all messages from tmpPending then clear the array.
        pending.addAll(tmpPending);
        tmpPending.clear();

        // Check if pending messages are deliverable and delivers them if so.
        for (Message message : pending) {
            int[] messageVectorClock = message.getVectorClock();
            if (checkVectorClockDeliverable(messageVectorClock)) {
                // Deliver the message
                if (observer == null) {
                    OutputWriter.writeDeliver(message, true);
                } else {
                    observer.deliver(message);
                }
                delivered.add(message);

                // Update vector clock, but only when delivering messages from other clients.
                if (message.getSenderId() != self.getId())
                    vectorClock[message.getSenderId() - 1] += 1;
            }
        }

        // Remove all delivered messages from the list of pending messages.
        for (Message message : delivered) {
            pending.remove(message);
        }
    }

    /**
     * Check if the message is deliverable.
     * That is the case if our vector clock is greater than the vector clock of a given message.
     *
     * @param messageVectorClock the other message's VC
     * @return true if the message is deliverable.
     */
    public boolean checkVectorClockDeliverable(int[] messageVectorClock) {
        //TODO: add the filter on the clients that are causaly dependent.
        for (int i = 0; i < messageVectorClock.length; i++) {
            if (this.vectorClock[i] < messageVectorClock[i]) {
                return false;
            }
        }
        return true;
    }

    public void printVectorClock() {
        System.out.println("VC for self ");
        System.out.print("[ ");
        for (int vc : vectorClock) {
            System.out.print(vc + " ");
        }
        System.out.println("]\n");
    }
}
