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
    private String content;


    public Message(int seqNbr, int senderId, String senderIp, int senderPort, int destId, String destIp, int destPort, String content){
        this.seqNbr = seqNbr;
        this.senderId = senderId;
        this.senderIp = senderIp;
        this.senderPort = senderPort;
        this.destId = destId;
        this.destIp = destIp;
        this.destPort = destPort;
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

    public String getContent() {
        return content;
    }

    public void printMessage(){
        System.out.println(String.format("msg %d from %d to %d. port %d. Content : %s.", seqNbr, senderId, destId, destPort, content));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this )
            return true;

        if (!(obj instanceof Message))
            return false;

        return this.seqNbr == ((Message) obj).seqNbr && this.senderId == ((Message) obj).senderId &&
                this.senderIp.equals(((Message) obj).senderIp) && this.destId == ((Message) obj).destId &&
                this.destIp.equals(((Message) obj).destIp) && this.destPort == ((Message) obj).destPort && this.content.equals(((Message) obj).content);
    }
}
