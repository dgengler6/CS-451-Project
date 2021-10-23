package cs451;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private int seqNbr;
    private int senderId;
    private int destId;
    private String destIp;
    private int destPort;
    private String content;


    public Message(int seqNbr, int senderId, int destId, String destIp, int destPort, String content){
        this.seqNbr = seqNbr;
        this.senderId = senderId;
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
}
