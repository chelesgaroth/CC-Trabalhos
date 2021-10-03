import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSChunkProtocol {


    public int startConn(DatagramSocket s, String hostS, int port) throws IOException {
        Packet pacote = new Packet((short) 0,1,0,0);
        InetAddress address = s.getLocalAddress();
        HostInfo sI = new HostInfo(s.getLocalPort(),address.getHostName());
        pacote.setData(sI.toBytes());
        send(s,hostS, port, pacote);
        // Definir um tempo máximo de espera do ACK
        s.setSoTimeout(60000);
        Packet ack = receive(s);
        if (ack.getMessageType() == 1 && ack.getAck() == 1) {
            pacote.setMessageType(0);
            pacote.setAck(1);
            send(s,hostS, port, pacote);
            System.out.println("Connection established!");
            return 0;
        }
        return -1;
    }

    public HostInfo listenConn(DatagramSocket s) throws IOException {
        Packet beg = receive(s);

        if (beg.getMessageType() == 1) {
            HostInfo sI = new HostInfo();
            sI.fromBytes(beg.getData());
            Packet begAck = new Packet((short) 0,1,1,0);

            send(s,sI.getAddress(),sI.getPort(),begAck);
            // Definir um tempo máximo de espera do ACK
            s.setSoTimeout(60000);
            Packet ack = receive(s);
            if (ack.getAck() == 1) {
                System.out.println("Connection established!");
                return sI;
            }
        }
        return null;
    }


    void endConn() {

    }

    public void send(DatagramSocket s, Packet p) throws IOException {
        // Meter a info num pacote
        byte[] packet = p.packetToBytes();
        s.send(new DatagramPacket(packet,packet.length));
    }
    public void send(DatagramSocket s, String hostS, int port, Packet p) throws IOException {
        InetAddress host = InetAddress.getByName(hostS);
        byte[] packet = p.packetToBytes();
        s.send(new DatagramPacket(packet,packet.length,host,port));
    }

    public Packet receive(DatagramSocket s) throws IOException {
        byte[] packetBytes = new byte[65536];
        DatagramPacket dp = new DatagramPacket(packetBytes,packetBytes.length);
        s.receive(dp);
        Packet pacote = new Packet();
        pacote.bytesToPacket(packetBytes);
        return pacote;
    }

    public void createNewFile(List<Packet> listP) {
        String name = listP.get(0).file.fileName;
        File outputFile = new File("outputFile.txt");

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            for(Packet p : listP) {
                //System.out.println(packet.file.fileSize);
                outputStream.write(p.getData(), 0, p.file.fileSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
