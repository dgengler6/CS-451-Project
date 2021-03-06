package cs451;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

public class Main {

    private static void handleSignal() {
        //immediately stop network packet processing
        System.out.println("Immediately stopping network packet processing.");

        //write/flush output file if necessary
        System.out.println("Writing output.");
    }

    private static void initSignalHandlers() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                handleSignal();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        Parser parser = new Parser(args);
        parser.parse();

        initSignalHandlers();

        // example
        long pid = ProcessHandle.current().pid();
        System.out.println("My PID: " + pid + "\n");
        System.out.println("From a new terminal type `kill -SIGINT " + pid + "` or `kill -SIGTERM " + pid + "` to stop processing packets\n");

        System.out.println("My ID: " + parser.myId() + "\n");
        System.out.println("List of resolved hosts is:");
        System.out.println("==========================");
        for (Host host: parser.hosts()) {
            System.out.println(host.getId());
            System.out.println("Human-readable IP: " + host.getIp());
            System.out.println("Human-readable Port: " + host.getPort());
            System.out.println();
        }
        System.out.println();

        System.out.println("Path to output:");
        System.out.println("===============");
        System.out.println(parser.output() + "\n");

        System.out.println("Path to config:");
        System.out.println("===============");
        System.out.println(parser.config() + "\n");

        System.out.println("Doing some initialization\n");

        int myID = parser.myId();

        List<Host> hosts = parser.hosts();
        Host me = hosts.get(myID - 1);
        int nbMessages = parser.nbMessages();
        int[][] lcbInfos = parser.lcbInfos();

        System.out.println("Dependencies");
        for (int i = 0; i < lcbInfos.length; i++) {
            System.out.print(String.format("For user %s : ", i + 1));
            for (int j = 0; j < lcbInfos[i].length; j++) {
                System.out.print(String.format("%s, ", lcbInfos[i][j]));
            }
            System.out.print("\n");
        }
        OutputWriter.setOutputPath(parser.output());
        MessageListener.setListeningPort(me.getPort());

        System.out.println("Broadcasting and delivering messages...\n");

        /* Perfect link implementation

        int perfectLinkHostId = parser.perfectLinkHostId();
        System.out.println(String.format("m = %d , i = %d", nbMessages, perfectLinkHostId));
        Host destHost = hosts.get(perfectLinkHostId - 1);
        PerfectLinks pl = new PerfectLinks(null);


        if(! (myID == perfectLinkHostId)){
            System.out.println("Sending messages to "+ perfectLinkHostId);
            for(int i=1; i<=nbMessages;i++){
                Message m = new Message(i, myID, me.getIp(), me.getPort(), perfectLinkHostId, destHost.getIp(), destHost.getPort(), "");
                pl.send(m);
            }
        }
        */

        /* FIFO implementation
        FifoBroadcast fifo = new FifoBroadcast(hosts, me, null);

        for(int i=1; i<=nbMessages;i++){
            Message m = new Message(i, i, myID, me.getIp(), me.getPort(), hosts.size(), "");
            fifo.broadcast(m);
        }
        */

        //
        LocalizedCausalBroadcast lcb = new LocalizedCausalBroadcast(hosts, me, null, lcbInfos);
        /*
        if(myID % 2 == 0 ){
            try {
                Thread.sleep(myID * 1000);
            }catch (InterruptedException e){

            }
        }
        */
        for(int i=1; i<=nbMessages;i++){
            Message m = new Message(i, i, myID, me.getIp(), me.getPort(), hosts.size(), "");
            lcb.broadcast(m);
        }

        //UniformReliableBroadcast urb = new UniformReliableBroadcast(hosts, me, null);


        // After a process finishes broadcasting,
        // it waits forever for the delivery of messages.
        while (true) {
            // Sleep for 1 hour
            Thread.sleep(60 * 60 * 1000);
        }
    }
}
