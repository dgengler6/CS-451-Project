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

}
