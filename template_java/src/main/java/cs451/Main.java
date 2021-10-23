package cs451;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
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

        int perfectLinkHostId = parser.perfectLinkHostId();
        System.out.println(String.format("m = %d , i = %d", nbMessages, perfectLinkHostId));
        Host destHost = hosts.get(perfectLinkHostId - 1);

        if( perfectLinkHostId == myID){
            System.out.println("Recieving messages");
           MessageListener ml = new MessageListener(me.getPort(), 0, new PerfectLinks(parser.output()));
           new Thread(ml).start();
        }else{
            System.out.println("Sending messages to "+ perfectLinkHostId);
            //FairLossLinks fll = new FairLossLinks(parser.output());
            //StubbornLinks stb = new StubbornLinks(parser.output());
            PerfectLinks pl = new PerfectLinks(parser.output());
            for(int i=0; i<nbMessages;i++){
                Message m = new Message(i, myID, me.getIp(), perfectLinkHostId, destHost.getIp(), destHost.getPort(), "");
                pl.send(m);
            }
        }
        System.out.println("Broadcasting and delivering messages...\n");


        // The idea here would be to create a thread for each msg and send it repeat to all hosts.




        // After a process finishes broadcasting,
        // it waits forever for the delivery of messages.
        while (true) {
            // Sleep for 1 hour
            Thread.sleep(60 * 60 * 1000);
        }
    }
}
