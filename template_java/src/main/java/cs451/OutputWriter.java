package cs451;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {

    public static void writeToFile(String data, String path){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.append(data + "\n");

            writer.close();

        }catch(IOException e){
            System.out.println("Error while writing to file");
        }
    }

    // Write to output the broadcasting of a message
    public static void writeBroadcast(Message message, String path){
        writeToFile(String.format("b %d", message.getSeqNbr()), path);
    }

    // Write to output the delivering of a message
    public static void writeDeliver(Message message, String path){
        writeToFile(String.format("d %d %d", message.getSenderId(), message.getSeqNbr()), path);
    }

}
