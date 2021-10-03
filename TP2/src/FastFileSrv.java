import java.io.*;
import java.net.*;


public class FastFileSrv {
    static final HostInfo gwInfo = new HostInfo(8888,"localhost");
    public static void main(String args[]) {
        DatagramSocket sock;
        FSChunkProtocol protocol = new FSChunkProtocol();
        String s;


        try {
            sock = new DatagramSocket();
            protocol.startConn(sock, gwInfo.getAddress(),gwInfo.getPort());

            while(true) {
                FileInfo f = new FileInfo( 0, "ola.txt",(short) 0);
                byte[] b = f.readFile();
                Packet packet = new Packet((short) 1, 5,0,0,f,b);
                System.out.println(packet.toString());
                protocol.send(sock, gwInfo.getAddress(),gwInfo.getPort(), packet);
                echo("Ficheiro enviado!");
                Thread.sleep(1000000);

            }

        }

        catch(IOException | InterruptedException e) {
            System.err.println("IOException " + e);
        }
    }

    //simple function to echo data to terminal
    public static void echo(String msg) {
        System.out.println(msg);
    }


}
