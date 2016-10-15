package deltaEncode;

import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class DeltaEnStreamTest {
    private void nextBytesPositive(byte[] bytes) {
        Random random = new Random();
        for (int i = 0; i < bytes.length; )
            for (int rnd = random.nextInt(), n = Math.min(bytes.length - i, 4);
                 n-- > 0; rnd >>= 8)
                bytes[i++] = (byte) Math.abs((byte) rnd);
    }

    private void oneDeltaEnStreamEncodeTest(byte[] inputArr, byte[] encodedArr) throws Exception {
        try (InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr))) {
            int elem, curIdx = 0;
            while ((elem = inStream.read()) != DeltaEnInputStream.END_OF_STREAM) {
                assertEquals(elem, encodedArr[curIdx]);
                curIdx++;
            }
        }
    }

    private void oneDeltaEnStreamBufferedEncodeTest(byte[] inputArr, byte[] encodedArr) throws Exception {
        try (InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr))) {
            for (int i = 1; i <= inputArr.length; i++) {
                byte[] buf = new byte[i];
                int bufSize, curIdx = 0;
                while ((bufSize = inStream.read(buf, 0, buf.length)) != -1) {
                    byte[] subArrayEncoded = new byte[bufSize],
                            subArrayBuf = new byte[bufSize];
                    System.arraycopy(encodedArr, curIdx, subArrayEncoded, 0, bufSize);
                    System.arraycopy(buf, 0, subArrayBuf, 0, bufSize);
                    assertEquals(Arrays.equals(subArrayEncoded, subArrayBuf), true);
                    curIdx += bufSize;
                }
            }
        }
    }

    private void oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(byte[] inputArr, byte[] encodedArr) throws Exception {
        try (InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr))) {
            for (int i = 1; i < inputArr.length; i++) {
                byte[] buf = new byte[inputArr.length];
                int bufSize, curIdx = 0;
                while ((bufSize = inStream.read(buf, i, inputArr.length - i)) != -1) {
                    byte[] subArrayEncoded = new byte[bufSize],
                            subArrayBuf = new byte[bufSize];
                    System.arraycopy(encodedArr, curIdx, subArrayEncoded, 0, bufSize);
                    System.arraycopy(buf, i, subArrayBuf, 0, bufSize);
                    assertEquals(Arrays.equals(subArrayEncoded, subArrayBuf), true);
                    curIdx += bufSize;
                }
            }
        }
    }

    private void oneDeltaEnStreamTest(byte[] inputArr) throws Exception {
        try (ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr));
             OutputStream outStream = new DeltaEnOutputStream(outStr)) {
            int elem;
            while ((elem = inStream.read()) != DeltaEnInputStream.END_OF_STREAM) {
                outStream.write(elem);
            }
            outStream.flush();
            byte[] outputArr = new byte[outStr.toByteArray().length];
            System.arraycopy(outStr.toByteArray(), 0, outputArr, 0, outStr.toByteArray().length);
            assertEquals(inputArr.length, outputArr.length);
            assertEquals(Arrays.equals(inputArr, outputArr), true);
        }
    }

    private void oneDeltaEnStreamBufferedTest(byte[] inputArr) throws Exception {
        try (ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr));
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
                assertEquals(inputArr.length, outputArr.length);
                assertEquals(Arrays.equals(inputArr, outputArr), true);
            }
        }
    }


    private void oneDeltaEnStreamBufferedWNZeroOffsetTest(byte[] inputArr) throws Exception {
        try (ByteArrayOutputStream outStr = new ByteArrayOutputStream();
             InputStream inStream = new DeltaEnInputStream(new ByteArrayInputStream(inputArr));
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
                assertEquals(inputArr.length, outputArr.length);
                assertEquals(Arrays.equals(inputArr, outputArr), true);
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
        oneDeltaEnStreamTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1});
        oneDeltaEnStreamEncodeTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1}, new byte[]{1, 0, 0, -1, 1, 1, 1, -2});
        oneDeltaEnStreamEncodeTest(new byte[]{1, 127, 0, 127, 0}, new byte[]{1, 126, -127, 127, -127});
        oneDeltaEnStreamEncodeTest(new byte[]{1, 1, 1, 1, 1, 1}, new byte[]{1, 0, 0, 0, 0, 0});
        oneDeltaEnStreamEncodeTest(new byte[]{0, 0, 0, 0, 0}, new byte[]{0, 0, 0, 0, 0});
    }

    @Test
    public void DeltaEnStreamAdditionalBufferedTests() throws Exception {
        oneDeltaEnStreamBufferedTest(new byte[]{0, 127, 0, 127, 0});
        oneDeltaEnStreamBufferedTest(new byte[]{1, 1, 1, 1, 1, 1});
        oneDeltaEnStreamBufferedTest(new byte[]{0, 0, 0, 0, 0});
        oneDeltaEnStreamBufferedTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1});
        oneDeltaEnStreamBufferedEncodeTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1}, new byte[]{1, 0, 0, -1, 1, 1, 1, -2});
        oneDeltaEnStreamBufferedEncodeTest(new byte[]{1, 127, 0, 127, 0}, new byte[]{1, 126, -127, 127, -127});
        oneDeltaEnStreamBufferedEncodeTest(new byte[]{1, 1, 1, 1, 1, 1}, new byte[]{1, 0, 0, 0, 0, 0});
        oneDeltaEnStreamBufferedEncodeTest(new byte[]{0, 0, 0, 0, 0}, new byte[]{0, 0, 0, 0, 0});
    }

    @Test
    public void DeltaEnStreamAdditionalBufferedWNZeroOffsetTests() throws Exception {
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{0, 127, 0, 127, 0});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{1, 1, 1, 1, 1, 1});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{0, 0, 0, 0, 0});
        oneDeltaEnStreamBufferedWNZeroOffsetTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1});
        oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(new byte[]{1, 1, 1, 0, 1, 2, 3, 1}, new byte[]{1, 0, 0, -1, 1, 1, 1, -2});
        oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(new byte[]{1, 127, 0, 127, 0}, new byte[]{1, 126, -127, 127, -127});
        oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(new byte[]{1, 1, 1, 1, 1, 1}, new byte[]{1, 0, 0, 0, 0, 0});
        oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(new byte[]{0, 0, 0, 0, 0}, new byte[]{0, 0, 0, 0, 0});
    }

    @Test
    public void DeltaEnStreamBigArrOfOnesTest() throws Exception {
        for (int i = 1; i <= 5; i++) {
            byte[] arr = new byte[i * 500];
            byte[] encodedArr = new byte[i * 500];
            Arrays.fill(arr, (byte) 1);
            Arrays.fill(encodedArr, (byte) 0);
            encodedArr[0] = 1;
            oneDeltaEnStreamTest(arr);
            oneDeltaEnStreamBufferedTest(arr);
            oneDeltaEnStreamBufferedWNZeroOffsetTest(arr);
            oneDeltaEnStreamEncodeTest(arr, encodedArr);
            oneDeltaEnStreamBufferedEncodeTest(arr, encodedArr);
            oneDeltaEnStreamBufferedWNZeroOffsetEncodeTest(arr, encodedArr);
        }
    }
}