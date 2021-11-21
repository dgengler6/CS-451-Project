package cs451;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int seqNbr;
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

    /**
     * Builds a default message, without specifying forwarder or destination, this canbe updated later.
     * Use when you know you want to send a message but you don't know yet where.
     * @param seqNbr
     * @param senderId
     * @param senderIp
     * @param senderPort
     * @param content
     */
    public Message(int seqNbr, int senderId, String senderIp, int senderPort,  String content){
        this.seqNbr = seqNbr;
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
    }

    /**
     * Builds a message without specifying the forwarder, this assumes that this is the same as sender
     */
    public Message(int seqNbr, int senderId, String senderIp, int senderPort, int destId, String destIp, int destPort,  String content){
        this.seqNbr = seqNbr;
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
    }

    /**
     * Builds a message with all parameters
     */
    public Message(int seqNbr, int senderId, String senderIp, int senderPort, int destId, String destIp, int destPort, int forwardId, String forwardIp, int forwardPort, String content){
        this.seqNbr = seqNbr;
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
    }

    public int getSeqNbr() {
        return seqNbr;
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

    public int getForwardId() { return forwardId; }

    public String getForwardIp() { return forwardIp; }

    public int getForwardPort() { return forwardPort; }

    public String getContent() {
        return content;
    }

    public Message updateDestInfos(int destId, String destIp, int destPort){
        /*this.destId = destId;
        this.destIp = destIp;
        this.destPort = destPort;*/
        return new Message(this.seqNbr, this.senderId, this.senderIp, this.senderPort, destId, destIp, destPort, this.forwardId, this.forwardIp, this.forwardPort, this.content);
    }

    public Message updateForwardInfos(int forwardId, String forwardIp, int forwardPort){
        /*this.forwardId = forwardId;
        this.forwardIp = forwardIp;
        this.forwardPort = forwardPort;*/
        return new Message(this.seqNbr, this.senderId, this.senderIp, this.senderPort, this.destId, this.destIp, this.destPort, forwardId, forwardIp, forwardPort, this.content);
    }

    public void printMessage(){
        System.out.println(String.format("msg %d originally sent by %d to %d. port %d. Forwarded by %d. Content : %s.", seqNbr, senderId, destId, destPort, forwardId, content));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this )
            return true;

        if (!(obj instanceof Message))
            return false;

        return this.seqNbr == ((Message) obj).seqNbr && this.senderId == ((Message) obj).senderId &&
                this.senderIp.equals(((Message) obj).senderIp) && this.destId == ((Message) obj).destId &&
                this.destIp.equals(((Message) obj).destIp) && this.destPort == ((Message) obj).destPort && this.forwardId == ((Message) obj).forwardId &&
                this.forwardIp.equals(((Message) obj).forwardIp) && this.forwardPort == ((Message) obj).forwardPort && this.content.equals(((Message) obj).content);
    }
}
