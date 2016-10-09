package deltaEncode;

import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import static org.junit.Assert.*;

public class DeltaEnStreamTest {
    private void compByteArrays(byte[] arr1, byte[] arr2) throws Exception {
        assertEquals(arr1.length, arr2.length);
        for (int i = 0; i < arr1.length; i++) {
            assertEquals(arr1[i], arr2[i]);
        }
    }

    private void nextBytesPositive(byte[] bytes) {
        Random random = new Random();
        for (int i = 0; i < bytes.length; )
            for (int rnd = random.nextInt(), n = Math.min(bytes.length - i, 4);
                 n-- > 0; rnd >>= 8)
                bytes[i++] = (byte) Math.abs((byte) rnd);
    }

    private void oneDeltaEnStreamTest(byte[] inputArr) throws Exception {

        try (ByteArrayInputStream inStr = new ByteArrayInputStream(inputArr);
             ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(inStr);
             OutputStream outStream = new DeltaEnOutputStream(outStr)) {
            int elem;
            while ((elem = inStream.read()) != DeltaEnInputStream.EndOfStream) {
                outStream.write(elem);
            }
            outStream.flush();
            byte[] outputArr = new byte[outStr.toByteArray().length];
            System.arraycopy(outStr.toByteArray(), 0, outputArr, 0, outStr.toByteArray().length);
            compByteArrays(inputArr, outputArr);
        }
    }

    private void oneDeltaEnStreamBufferedTest(byte[] inputArr) throws Exception {
        try (ByteArrayInputStream inStr = new ByteArrayInputStream(inputArr);
             ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(inStr);
             OutputStream outStream = new DeltaEnOutputStream(outStr)) {
            for (int i = 1; i <= inputArr.length; i++) {
                byte[] buf = new byte[i];
                int bufSize;
                while ((bufSize = inStream.read(buf, 0, buf.length)) != -1) {
                    outStream.write(buf, 0, bufSize);
                }
                outStream.flush();
                byte[] outputArr = new byte[outStr.toByteArray().length];
                System.arraycopy(outStr.toByteArray(), 0, outputArr, 0, outStr.toByteArray().length);
                compByteArrays(inputArr, outputArr);
            }
        }
    }


    private void oneDeltaEnStreamBufferedWNZeroOffsetTest(byte[] inputArr) throws Exception {
        try (ByteArrayInputStream inStr = new ByteArrayInputStream(inputArr);
             ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(inStr);
             OutputStream outStream = new DeltaEnOutputStream(outStr)) {
            for (int i = 1; i < inputArr.length; i++) {
                byte[] buf = new byte[inputArr.length];
                int bufSize;
                while ((bufSize = inStream.read(buf, i, inputArr.length - i)) != -1) {
                    outStream.write(buf, i, bufSize);
                }
                outStream.flush();
                byte[] outputArr = new byte[outStr.toByteArray().length];
                System.arraycopy(outStr.toByteArray(), 0, outputArr, 0, outStr.toByteArray().length);
                compByteArrays(inputArr, outputArr);
            }
        }
    }

    @Test
    public void DeltaEnStreamRandomDataBufferedTest() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            byte[] inputArr = new byte[random.nextInt(100)];
            nextBytesPositive(inputArr);
            oneDeltaEnStreamBufferedTest(inputArr);
        }
    }

    @Test
    public void DeltaEnStreamRandomDataBufferedWNZeroOffsetTest() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            byte[] inputArr = new byte[random.nextInt(100)];
            nextBytesPositive(inputArr);
            oneDeltaEnStreamBufferedWNZeroOffsetTest(inputArr);
        }
    }

    @Test
    public void DeltaEnStreamRandomDataTest() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            byte[] inputArr = new byte[random.nextInt(100)];
            nextBytesPositive(inputArr);
            oneDeltaEnStreamTest(inputArr);
        }
    }

    @Test
    public void DeltaEnStreamEmptyDataTest() throws Exception {
        oneDeltaEnStreamTest(new byte[]{});
        oneDeltaEnStreamBufferedTest(new byte[]{});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{});
    }

    @Test
    public void DeltaEnStreamAdditionalTests() throws Exception {
        oneDeltaEnStreamTest(new byte[]{1, 127, 0, 127, 0});
        oneDeltaEnStreamTest(new byte[]{1, 1, 1, 1, 1, 1});
        oneDeltaEnStreamTest(new byte[]{0, 0, 0, 0, 0});
    }

    @Test
    public void DeltaEnStreamAdditionalBufferedTests() throws Exception {
        oneDeltaEnStreamBufferedTest(new byte[]{0, 127, 0, 127, 0});
        oneDeltaEnStreamBufferedTest(new byte[]{1, 1, 1, 1, 1, 1});
        oneDeltaEnStreamBufferedTest(new byte[]{0, 0, 0, 0, 0});
    }

    @Test
    public void DeltaEnStreamAdditionalBufferedWNZeroOffsetTests() throws Exception {
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{0, 127, 0, 127, 0});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{1, 1, 1, 1, 1, 1});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{0, 0, 0, 0, 0});
    }

}