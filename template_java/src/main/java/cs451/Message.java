package cs451;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int seqNbr;
    private int senderId;
    private String senderIp;
    private int destId;
    private String destIp;
    private int destPort;
    private String content;


    public Message(int seqNbr, int senderId, String senderIp, int destId, String destIp, int destPort, String content){
        this.seqNbr = seqNbr;
        this.senderId = senderId;
        this.senderIp = senderIp;
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
        System.out.println(String.format("Message %d from sender %d (IP %s) to dest %d (IP %s) on port %d. Content : %s", seqNbr, senderId, senderIp, destId, destIp, destPort, content));
    }
}
