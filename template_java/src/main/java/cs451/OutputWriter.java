package cs451;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {

    private static String path;

    public static void writeToFile(String data){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(OutputWriter.path, true));
            writer.append(data + "\n");

            writer.close();

        }catch(IOException e){
            System.out.println("Error while writing to file");
        }
    }

    // Write to output the broadcasting of a message
    public static void writeBroadcast(Message message){
        writeBroadcast(message, false);
    }

    public static void writeBroadcast(Message message, Boolean print){
        String output = String.format("b %d", message.getSeqNbr());
        if (print)
            System.out.println(output);
        writeToFile(output);
    }

    // Write to output the delivering of a message
    public static void writeDeliver(Message message){
        writeDeliver(message, false);
    }

    public static void writeDeliver(Message message, Boolean print){
        String output = String.format("d %d %d", message.getSenderId(), message.getSeqNbr());
        if (print)
            System.out.println(output);
        writeToFile(output);
    }

    public static void setOutputPath(String path) { OutputWriter.path = path;}

}
