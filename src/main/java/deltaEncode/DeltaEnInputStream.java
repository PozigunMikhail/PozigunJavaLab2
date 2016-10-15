package deltaEncode;

import java.io.IOException;
import java.io.InputStream;

public class DeltaEnInputStream extends InputStream {
    public static final int END_OF_STREAM = 256;
    private InputStream inStr;
    private int prev = 0;

    public DeltaEnInputStream(InputStream inputStream) {
        inStr = inputStream;
    }

    @Override
    public int read() throws IOException {
        int cur = inStr.read();
        if (cur == -1) {
            return END_OF_STREAM;
        }
        int retValue = cur - prev;
        prev = cur;
        return retValue;
    }

    @Override
    public int read(byte b[]) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        int c = read();
        if (c == END_OF_STREAM) {
            return -1;
        }
        b[off] = (byte) c;
        int i = 1;
        for (; i < len; i++) {
            c = read();
            if (c == END_OF_STREAM) {
                break;
            }
            b[off + i] = (byte) c;
        }
        return i;
    }

    @Override
    public void close() throws IOException {
        inStr.close();
    }
}
