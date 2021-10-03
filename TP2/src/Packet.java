import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;


public class Packet {
    // Cabeçalho do pacote
    short  packetSize;
    short clientId;
    private boolean[] messageType = new boolean[3];
    private boolean[] ack = new boolean[3];
    private boolean[] exp = new boolean[3];
    FileInfo file = null;
    // Dados/Payload do pacote
    byte[] data = {};

    public Packet(){}

    public Packet(short clientId, int messageType, int ack, int exp) {
        this.clientId = clientId;
        setMessageType(messageType);
        setAck(ack);
        setExp(exp);
        this.packetSize = (short) packetSize();
    }

    public Packet(short clientId, int messageType, int ack, int exp, byte[] newData) {
        this.clientId = clientId;
        setMessageType(messageType);
        setAck(ack);
        setExp(exp);
        this.data = newData;
        this.packetSize = (short) packetSize();
    }

    public Packet(short clientId, int messageType, int ack, int exp, FileInfo fileInfo) {
        this.clientId = clientId;
        setMessageType(messageType);
        setAck(ack);
        setExp(exp);
        this.file = fileInfo;
        this.packetSize = (short) packetSize();
    }

    public Packet(short clientId, int messageType, int ack, int exp, int filesize, String fileName, short packetN) {
        this.clientId = clientId;
        setMessageType(messageType);
        setAck(ack);
        setExp(exp);
        this.file = new FileInfo(filesize,fileName,packetN);
        this.packetSize = (short) packetSize();
    }

    public Packet(short clientId, int messageType, int ack, int exp, FileInfo f, byte[] dataF) {
        this.clientId = clientId;
        setMessageType(messageType);
        setAck(ack);
        setExp(exp);
        this.file = f;
        this.data = dataF;
        this.packetSize = (short) packetSize();
    }

    public byte[] packetToBytes() throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeShort(packetSize);
        dataOut.writeShort(clientId);
        for (int i = 0; i < 3; i++) {
            dataOut.writeBoolean(messageType[i]);
        }
        for (int i = 0; i < 3; i++) {
            dataOut.writeBoolean(ack[i]);
        }
        for (int i = 0; i < 3; i++) {
            dataOut.writeBoolean(exp[i]);
        }
        if (file != null) {
            dataOut.writeInt(file.fileSize);
            dataOut.writeUTF(file.fileName);
            dataOut.writeShort(file.packetNumber);

        }
        dataOut.write(data);
        dataOut.close();
        byteOut.flush();
        return byteOut.toByteArray();
    }

    public void bytesToPacket (byte[] headerBytes) throws IOException {
        final ByteArrayInputStream byteIn = new ByteArrayInputStream(headerBytes);
        final DataInputStream dataIn = new DataInputStream(byteIn);
        packetSize = dataIn.readShort();
        clientId = dataIn.readShort();
        for (int i = 0; i < 3; i++) {
            messageType[i] = dataIn.readBoolean();
        }
        for (int i = 0; i < 3; i++) {
            ack[i] = dataIn.readBoolean();
        }
        for (int i = 0; i < 3; i++) {
            exp[i] = dataIn.readBoolean();
        }
        if (booleansToInt(messageType) == 5) {
            file = new FileInfo();
            file.fileSize = dataIn.readInt();
            file.fileName = dataIn.readUTF();
            file.packetNumber = dataIn.readShort();
        }
        if(packetSize - headerSize() > 0)
            data = dataIn.readNBytes(packetSize - headerSize());
        dataIn.close();
        byteIn.close();
    }

    public int headerSize() {
        if (file == null) return 41;
        else {
            return 89 + file.fileName.length();
        }
    }

    public int packetSize() {
        if (file == null) return headerSize() + data.length;
        else {
            return headerSize() + file.fileSize;
        }
    }

    public boolean[] intToBooleans(int x, int n) {
        boolean[] b = new boolean[n];
        String s = Integer.toBinaryString(x);
        int j = s.length()-1;
        for (int i = n-1; i >= 0; i--) {
            if (j<0) b[i] = false;
            else {
                b[i] = s.charAt(j) == '1';
            }
            j--;
        }
        return b;
    }
    public int booleansToInt(boolean[] arr){
        int n = 0;
        for (boolean b : arr)
            n = (n << 1) | (b ? 1 : 0);
        return n;
    }


    public void setData(byte[] data) {
        this.data = data;
        this.packetSize = (short) packetSize();
    }

    public byte[] getData() {
        return data;
    }

    public void setMessageType(int messageType) {
        this.messageType = intToBooleans(messageType,3);
    }

    public int getMessageType() {
         return booleansToInt(this.messageType);
    }

    public int getAck() {
        return booleansToInt(this.ack);
    }

    public int getExp() {
        return booleansToInt(this.exp);
    }

    public void setAck(int ack) {
        this.ack = intToBooleans(ack,3);
    }

    public void setExp(int exp) {
        this.exp = intToBooleans(exp,3);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "packetSize=" + packetSize +
                ", clientId=" + clientId +
                ", messageType=" + booleansToInt(messageType)  +
                ", ack=" + booleansToInt(ack) +
                ", exp=" + booleansToInt(exp) +
                ", file=" + file.toString() +
                '}';
    }
}
