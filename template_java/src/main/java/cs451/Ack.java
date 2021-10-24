package cs451;

public class Ack extends Message {

    private Message message;
    public Ack( Message message){
        super( message.getSeqNbr(),  message.getDestId(),  message.getDestIp(), message.getDestPort(),  message.getSenderId(),  message.getSenderIp(),  message.getSenderPort(),  "");
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void printMessage(){
        System.out.println(String.format("recieved ack for msg %d from %d", message.getSeqNbr(), message.getDestId()));
    }
}
