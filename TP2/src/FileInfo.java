import java.io.*;

public class FileInfo {
    int fileSize;
    String fileName;
    short packetNumber;

    public FileInfo() {}

    public FileInfo(int filesize, String fileName, short packetN) {
        this.fileSize = filesize;
        this.fileName = fileName;
        this.packetNumber = packetN;
    }
    public byte[] readFile() throws IOException {
        File f = new File(fileName);
        fileSize = (int) f.length();
        // work only for 2GB file, because array index can only up to Integer.MAX
        /*
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final DataOutputStream dataOut = new DataOutputStream(byteOut);
        dataOut.writeInt(fileSize);
        dataOut.close(); // or dataOut.flush()
        final byte[] bytesFileSize = byteOut.toByteArray();
        */
        byte[] buffer = new byte[(int)f.length()];
        FileInputStream is = new FileInputStream(fileName);
        is.read(buffer);
        is.close();
        return buffer;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileSize=" + fileSize +
                ", fileName='" + fileName + '\'' +
                ", packetNumber=" + packetNumber +
                '}';
    }
}
