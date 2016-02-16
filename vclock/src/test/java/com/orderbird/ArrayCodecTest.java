package com.orderbird;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ethan on 16/02/16.
 */
@RunWith(RobolectricTestRunner.class)
public class ArrayCodecTest {
    @Test
    public void encode_count() throws Exception {
        int raw = 15;
        String expected = "AAAP";
        ArrayCodec codec = new ArrayCodec();
        String encoded = codec.encode_count(raw);
        assertEquals(encoded, expected);
    }

    @Test
    public void encode_count2() throws Exception {
        int raw = 1234567;
        String expected = "EtaH";
        ArrayCodec codec = new ArrayCodec();
        String encoded = codec.encode_count(raw);
        assertEquals(encoded, expected);
    }

    @Test
    public void decode_count() throws Exception {
        String encoded = "A/+d";
        int expected = 262045;
        ArrayCodec codec = new ArrayCodec();
        int decoded = codec.decode_count(encoded);
        assertEquals(decoded, expected);
    }

    @Test
    public void encode_vector() throws Exception {
        ArrayList<Integer> vector = new ArrayList<Integer>();
        vector.add(1);
        vector.add(12345);
        vector.add(77);
        String expected = "AAABADA5AABN";

        ArrayCodec codec = new ArrayCodec();
        String encoded = codec.encode_vector(vector);
        assertEquals(encoded, expected);
    }

    @Test
    public void encode_decode_symetric() throws Exception {
        String initial = "AAAtAAAAAEAtAAEp";
        ArrayCodec codec = new ArrayCodec();
        List<Integer> decoded = codec.decode_vector(initial);
        assertEquals(decoded.size(), 4);
        assertEquals((int)decoded.get(1), 0);
        assertEquals((int)decoded.get(2), 16429);
        String encoded = codec.encode_vector(decoded);
        assertEquals(encoded, initial);
    }

}
