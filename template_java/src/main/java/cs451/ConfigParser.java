package cs451;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

public class ConfigParser {

    private static final String SPACES_REGEX = "\\s+";

    private String path;

    private int nbMessages = 0;
    private int perfectLinkHostId = 0;
    private int[][] localizedCausalBroadcastProcesses;

    public boolean populate(String value) {
        File file = new File(value);
        path = file.getPath();
        int nbLines = 0;

        // Count Line number
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.readLine() != null) nbLines++;
        } catch (IOException e) {
            System.err.println("Problem with the config file!");
            return false;
        }

        // Do Initialization.
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int lineNum = 1;
            String firstLine = br.readLine();

            // Read the first line as it is defining which config we use.
            // m is used everywhere, if size is 2 the we test perfect links config
            String[] splits = firstLine.split(SPACES_REGEX);

            if (splits.length == 1) {
                if (nbLines == 1)
                    System.out.println("FIFO Config");
                nbMessages = Integer.parseInt(splits[0]);
            } else if (splits.length == 2) {
                System.out.println("Perfect Links Config");
                nbMessages = Integer.parseInt(splits[0]);
                perfectLinkHostId = Integer.parseInt(splits[1]);
            } else {
                System.err.println("Problem with the line " + lineNum + " in the config file!");
                return false;
            }

            if (nbLines > 1) {
                System.out.println("Localized Causal Config");
                localizedCausalBroadcastProcesses = new int[nbLines - 1][];
                for (String line; (line = br.readLine()) != null; lineNum++) {

                    if (line.isBlank()) {
                        continue;
                    } else {
                        splits = line.split(SPACES_REGEX);
                        int[] lineInt = new int[splits.length];
                        for (int i = 0; i < splits.length; i++) {
                            lineInt[i] = Integer.parseInt(splits[i]);
                            System.out.println(lineInt[i]);
                        }
                        localizedCausalBroadcastProcesses[lineInt[0] - 1] = lineInt;
                    }
                }
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

    public int getNbMessages() {
        return nbMessages;
    }

    public int getPerfectLinkHostId() {
        return perfectLinkHostId;
    }

    public int[][] getLocalizedCausalBroadcastProcesses() {
        return localizedCausalBroadcastProcesses;
    }
}
