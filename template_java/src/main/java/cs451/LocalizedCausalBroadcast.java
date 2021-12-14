package cs451;

import java.util.List;
import java.util.ArrayList;

public class LocalizedCausalBroadcast implements Broadcast, Observer {

    private Observer observer;
    private Host self;

    private UniformReliableBroadcast urb;
    private List<Message> pending;
    private List<Message> tmpPending;
    private int broadcastVectorClockValue;
    private int[] vectorClock;
    private int[][] peersCausalLink;

    private int delay = 0;

    LocalizedCausalBroadcast(List<Host> hosts, Host self, Observer observer, int[][] peersCausalLink) {
        this.self = self;
        this.observer = observer;

        //Initialize the vector clock for all other hosts.
        this.broadcastVectorClockValue = 0;
        this.vectorClock = new int[hosts.size()];
        this.peersCausalLink = peersCausalLink;

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
        // When broadcasting we keep a temporary vector clock, so we keep message indices but are
        // still blank on recieving.
        int[] tmpVectorClock = vectorClock.clone();
        tmpVectorClock[self.getId() - 1] = broadcastVectorClockValue;
        broadcastVectorClockValue += 1;

        message.setVectorClock(tmpVectorClock);

        // Broadcast message
        if (observer == null && message.getSenderId() == self.getId()) {
            OutputWriter.writeBroadcast(message, true);
        }
        try {
            Thread.sleep(2000);
            urb.broadcast(message);
        } catch (InterruptedException e) {
            System.out.println("Thread error in Stubborn link timer " + e);
        }
        //urb.broadcast(message);

    }

    @Override
    public void deliver(Message message) {
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
            if (checkVectorClockDeliverable(message.getSenderId(), messageVectorClock, message)) {
                // Deliver the message
                if (observer == null) {
                    OutputWriter.writeDeliver(message, true);
                } else {
                    observer.deliver(message);
                }
                delivered.add(message);

                // Update vector clock, but only when delivering messages from other clients.
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
    public boolean checkVectorClockDeliverable(int peerId, int[] messageVectorClock, Message message) {
        // We get the list of indices that impact a given process.
        int[] peersCausallyImpactingMessage = peersCausalLink[peerId - 1];

        for (int i = 0; i < peersCausallyImpactingMessage.length; i++) {
            // Then we do the check for all of these indices ( and not all processes as before )
            int vcIndex = peersCausallyImpactingMessage[i] - 1;
            if (this.vectorClock[vcIndex] < messageVectorClock[vcIndex]) {
                return false;
            }
        }
        System.out.println("Able to deliver message bc (self, other)");
        printVectorClock();
        message.printMesssageVectorClock();

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
