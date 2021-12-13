package cs451;

public class Ack extends Message {

    private Message message;
    public Ack( Message message){
        super( message.getSeqNbr(), message.getOrder(),  message.getDestId(),  message.getDestIp(), message.getDestPort(),  message.getSenderId(),  message.getSenderIp(),  message.getSenderPort(), message.getForwardId(), message.getForwardIp(), message.getForwardPort(), message.getVectorClockSize(),  "");
        this.message = message;
    }

    public Ack( Message ack,  Message message){
        super( ack.getSeqNbr(), ack.getOrder(),  ack.getDestId(),  ack.getDestIp(), ack.getDestPort(),  ack.getForwardId(),  ack.getForwardIp(),  ack.getForwardPort(), ack.getForwardId(), ack.getForwardIp(), ack.getForwardPort(), ack.getVectorClockSize(),  "");
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void printMessage(){
        System.out.println(String.format("recieved ack for msg %d originally from %d and forwarded by %d", message.getSeqNbr(), message.getDestId(), message.getForwardId()));
    }
}
