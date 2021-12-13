package cs451;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int seqNbr;
    private int order;
    private int senderId;
    private String senderIp;
    private int senderPort;
    private int destId;
    private String destIp;
    private int destPort;
    private int forwardId;
    private String forwardIp;
    private int forwardPort;
    private String content;
    private int vectorClockSize;
    private int[] vectorClock;

    /**
     * Builds a default message, without specifying forwarder or destination, this canbe updated later.
     * Use when you know you want to send a message but you don't know yet where.
     *
     * @param seqNbr
     * @param senderId
     * @param senderIp
     * @param senderPort
     * @param content
     */
    public Message(int seqNbr, int order, int senderId, String senderIp, int senderPort, int vectorClockSize, String content) {
        this.seqNbr = seqNbr;
        this.order = order;
        this.senderId = senderId;
        this.senderIp = senderIp;
        this.senderPort = senderPort;
        this.destId = senderId;
        this.destIp = senderIp;
        this.destPort = senderPort;
        this.forwardId = senderId;
        this.forwardIp = senderIp;
        this.forwardPort = senderPort;
        this.content = content;
        this.vectorClockSize = vectorClockSize;
        this.vectorClock = new int[vectorClockSize];
    }

    /**
     * Builds a message without specifying the forwarder, this assumes that this is the same as sender
     */
    public Message(int seqNbr, int order, int senderId, String senderIp, int senderPort, int destId, String destIp, int destPort, int vectorClockSize, String content) {
        this.seqNbr = seqNbr;
        this.order = order;
        this.senderId = senderId;
        this.senderIp = senderIp;
        this.senderPort = senderPort;
        this.destId = destId;
        this.destIp = destIp;
        this.destPort = destPort;
        this.forwardId = senderId;
        this.forwardIp = senderIp;
        this.forwardPort = senderPort;
        this.content = content;
        this.vectorClockSize = vectorClockSize;
        this.vectorClock = new int[vectorClockSize];
    }

    /**
     * Builds a message with all parameters
     */
    public Message(int seqNbr, int order, int senderId, String senderIp, int senderPort, int destId, String destIp, int destPort, int forwardId, String forwardIp, int forwardPort, int vectorClockSize, String content) {
        this.seqNbr = seqNbr;
        this.order = order;
        this.senderId = senderId;
        this.senderIp = senderIp;
        this.senderPort = senderPort;
        this.destId = destId;
        this.destIp = destIp;
        this.destPort = destPort;
        this.forwardId = forwardId;
        this.forwardIp = forwardIp;
        this.forwardPort = forwardPort;
        this.content = content;
        this.vectorClockSize = vectorClockSize;
        this.vectorClock = new int[vectorClockSize];
    }

    public int getSeqNbr() {
        return seqNbr;
    }

    public int getOrder() {
        return order;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public int getDestId() {
        return destId;
    }

    public String getDestIp() {
        return destIp;
    }

    public int getDestPort() {
        return destPort;
    }

    public int getForwardId() {
        return forwardId;
    }

    public String getForwardIp() {
        return forwardIp;
    }

    public int getForwardPort() {
        return forwardPort;
    }

    public int[] getVectorClock() {
        return vectorClock;
    }

    public String getContent() {
        return content;
    }

    /**
     * Set the vectorClock of the message to a given vector clock.
     *
     * @param vectorClock
     */
    public void setVectorClock(int[] vectorClock) {
        for (int i = 0; i < this.vectorClockSize; i++)
            this.vectorClock[i] = vectorClock[i];
    }

    /**
     * Update the destination information of the message
     *
     * @param destId   new destination id
     * @param destIp   new destination ip
     * @param destPort new destination port
     * @return the new Message with update destination informations.
     */
    public Message updateDestInfos(int destId, String destIp, int destPort) {
        Message newMessage = new Message(this.seqNbr, this.order, this.senderId, this.senderIp, this.senderPort, destId, destIp, destPort, this.forwardId, this.forwardIp, this.forwardPort, this.vectorClockSize, this.content);
        newMessage.setVectorClock(this.vectorClock);
        return newMessage;
    }

    /**
     * Update the forwarder inforamtion of a the message
     *
     * @param forwardId   new forwarder id
     * @param forwardIp   new forwarder ip
     * @param forwardPort new forward port
     * @return the new Message with the updated forward informations.
     */
    public Message updateForwardInfos(int forwardId, String forwardIp, int forwardPort) {
        Message newMessage = new Message(this.seqNbr, this.order, this.senderId, this.senderIp, this.senderPort, this.destId, this.destIp, this.destPort, forwardId, forwardIp, forwardPort, this.vectorClockSize, this.content);
        newMessage.setVectorClock(this.vectorClock);
        return newMessage;
    }

    public void printMessage() {
        System.out.println(String.format("msg %d originally sent by %d to %d. port %d. Forwarded by %d. Content : %s.", seqNbr, senderId, destId, destPort, forwardId, content));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Message))
            return false;

        Message other = (Message) obj;
        return this.seqNbr == other.seqNbr &&
                this.order == other.order &&
                this.senderId == other.senderId &&
                this.senderIp.equals(other.senderIp) &&
                this.destId == other.destId &&
                this.destIp.equals(other.destIp) &&
                this.destPort == other.destPort &&
                this.forwardId == other.forwardId &&
                this.forwardIp.equals(other.forwardIp) &&
                this.forwardPort == other.forwardPort &&
                Arrays.equals(this.vectorClock, other.vectorClock) &&
                this.content.equals(other.content);
    }
}
