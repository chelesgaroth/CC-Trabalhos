import java.io.*;
import java.net.*;

public class HttpGw {
    public static void main(String args[]) {
        DatagramSocket sock = null;
        FSChunkProtocol protocol = new FSChunkProtocol();
        try {
            sock = new DatagramSocket(8888);
            // Wait for an incoming data
            echo("Server socket created. Waiting for incoming data...");
            HostInfo sI = protocol.listenConn(sock);

            //communication loop
            while(true) {
                Packet packet = protocol.receive(sock);

                byte[] data = packet.getData();
                File outputFile = new File("outputFile.txt");
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    System.out.println(packet.file.fileSize);
                    outputStream.write(data, 0, packet.file.fileSize);
                    echo("Ficheiro guardado!");
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }

    //simple function to echo data to terminal
    public static void echo(String msg) {
        System.out.println(msg);
    }


}
