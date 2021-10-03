import java.io.*;

public class HostInfo {
    int port;
    String address;

    public HostInfo() {}

    public HostInfo(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void fromBytes(byte[] b) throws IOException {
        final ByteArrayInputStream byteIn = new ByteArrayInputStream(b);
        final DataInputStream dataIn = new DataInputStream(byteIn);
        port = dataIn.readInt();
        address = dataIn.readUTF();
        dataIn.close();
        byteIn.close();

    }
    public byte[] toBytes() throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeInt(port);
        dataOut.writeUTF(address);
        dataOut.close();
        byteOut.flush();
        return byteOut.toByteArray();
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "port=" + port +
                ", address='" + address + '\'' +
                '}';
    }
}
