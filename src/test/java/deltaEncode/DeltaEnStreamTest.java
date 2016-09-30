package deltaEncode;

import org.junit.Test;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import static org.junit.Assert.*;

public class DeltaEnStreamTest {
    private void oneDeltaEnStreamTest(int arrSize) throws Exception {
        byte[] inputArr = new byte[arrSize];
        Random random = new Random();
        random.nextBytes(inputArr);

        ByteArrayInputStream inStr = new ByteArrayInputStream(inputArr);
        ByteArrayOutputStream outStr = new ByteArrayOutputStream();

        DeltaEnInputStream inStream = new DeltaEnInputStream(inStr);
        DeltaEnOutputStream outStream = new DeltaEnOutputStream(outStr);

        byte[] buf = new byte[inputArr.length];
        inStream.read(buf, 0, buf.length);
        outStream.write(buf, 0, buf.length);

        byte[] arr = new byte[outStr.toByteArray().length];
        System.arraycopy(outStr.toByteArray(), 0, arr, 0, outStr.toByteArray().length);
        assertEquals(inputArr.length, arr.length);
        for (int i = 0; i < inputArr.length; i++) {
            assertEquals(arr[i], inputArr[i]);
        }
    }

    @Test
    public void DeltaEnStreamNotEmptyDataTest() throws Exception {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            oneDeltaEnStreamTest(random.nextInt(100));
        }
    }

    @Test
    public void DeltaEnStreamEmptyDataTest() throws Exception {
        oneDeltaEnStreamTest(0);
    }
}