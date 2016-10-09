package deltaEncode;

import java.io.IOException;
import java.io.OutputStream;

public class DeltaEnOutputStream extends OutputStream {
    private OutputStream outStr;
    private int prev = 0;

    public DeltaEnOutputStream(OutputStream outputStream) {
        outStr = outputStream;
    }

    @Override
    public void write(int b) throws IOException {
        outStr.write(prev + b);
        prev += b;
    }

    @Override
    public void write(byte b[]) throws IOException {
        this.write(b, 0, b.length);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        for (int i = 0; i < len; i++) {
            write(b[off + i]);
        }
    }

    @Override
    public void close() throws IOException {
        outStr.close();
    }

    @Override
    public void flush() throws IOException {
        outStr.flush();
    }
}
