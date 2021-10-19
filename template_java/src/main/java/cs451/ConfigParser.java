package cs451;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class ConfigParser {

    private static final String SPACES_REGEX = "\\s+";

    private String path;

    private int nbMessages;
    private int perfectLinkHostId;
    // We'll use that latter, might as well add it now
    private int[][] localizedCausalBroadcastProcesses;

    public boolean populate(String value) {
        File file = new File(value);
        path = file.getPath();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            int lineNum = 1;
            String firstLine = br.readLine();

            // Read the first line as it is defining which config we use.
            // m is used everywhere, if size is 2 the we test perfect links config
            String[] splits = firstLine.split(SPACES_REGEX);

            if (splits.length == 1) {
                System.out.println("FIFO Config");
                nbMessages = Integer.parseInt(splits[0]);
            }else if (splits.length == 2) {
                System.out.println("Perfect Links Config");
                nbMessages = Integer.parseInt(splits[0]);
                perfectLinkHostId = Integer.parseInt(splits[1]);
            }else{
                System.err.println("Problem with the line " + lineNum + " in the config file!");
                return false;
            }

            lineNum += 1;

            // If the file has more lines we handle it, only used for lcausal config.
            for(String line; (line = br.readLine()) != null; lineNum++) {
                System.out.println("Lcausal Config");
                if (line.isBlank()) {
                    continue;
                }

                splits = line.split(SPACES_REGEX);
            }
        } catch (IOException e) {
            System.err.println("Problem with the config file!");
            return false;
        }

        return true;
    }

    public String getPath() {
        return path;
    }

    public int getNbMessages() { return nbMessages; }

    public int getPerfectLinkHostId() { return perfectLinkHostId; }

}
