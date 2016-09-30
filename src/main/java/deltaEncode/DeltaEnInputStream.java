package deltaEncode;

import java.io.IOException;
import java.io.InputStream;

public class DeltaEnInputStream extends InputStream {
    private InputStream inStr;

    DeltaEnInputStream(InputStream inputStream) {
        inStr = inputStream;
    }

    @Override
    public int read() throws IOException {
        return inStr.read();
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
        int length;
        length = inStr.read(b, off, len);
        byte prev = b[0];
        for (int i = 1; i < length; i++) {
            byte cur = b[i];
            b[i] = (byte) (b[i] - prev);
            prev = cur;
        }
        return length;
    }

    @Override
    public void close() throws IOException {
        inStr.close();
    }
}
