package deltaEncode;

import java.io.*;

public class DeltaEncode {
    private static final int BLOCK_SIZE = 10;

    public static void deltaDecode(String enFilename) throws IOException {
        String decFilename = enFilename.replaceAll(".diff", "") + "1.txt";
        try (FileInputStream iStream = new FileInputStream(enFilename);
             DeltaEnOutputStream oStream = new DeltaEnOutputStream(new FileOutputStream(decFilename))) {
            byte[] buf = new byte[BLOCK_SIZE];
            int bufSize;
            while ((bufSize = iStream.read(buf, 0, buf.length)) != -1) {
                oStream.write(buf, 0, bufSize);
            }
        }
    }

    public static void deltaDecode() throws IOException {
        try (DeltaEnOutputStream oStream = new DeltaEnOutputStream(System.out)) {
            byte[] buf = new byte[BLOCK_SIZE];
            int bufSize;
            while ((bufSize = System.in.read(buf, 0, buf.length)) != -1) {
                oStream.write(buf, 0, bufSize);
            }
        }
    }

    public static void deltaEncode(String inFilename) throws IOException {
        String decFilename = inFilename.replaceAll(".txt", "") + ".diff";
        try (DeltaEnInputStream iStream = new DeltaEnInputStream(new FileInputStream(inFilename));
             FileOutputStream oStream = new FileOutputStream(decFilename)) {
            byte[] buf = new byte[BLOCK_SIZE];
            int bufSize;
            while ((bufSize = iStream.read(buf, 0, buf.length)) != -1) {
                oStream.write(buf, 0, bufSize);
            }
        }
    }

    public static void deltaEncode() throws IOException {
        try (DeltaEnInputStream iStream = new DeltaEnInputStream(System.in)) {
            byte[] buf = new byte[BLOCK_SIZE];
            int bufSize;
            while ((bufSize = iStream.read(buf, 0, buf.length)) != -1) {
                System.out.write(buf, 0, bufSize);
            }
        }
    }

    public static void main(String[] args) {
        CmdDeltaEnParser.parse(args);
    }
}
