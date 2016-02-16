package com.orderbird;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by ethan on 16/02/16.
 */
@RunWith(RobolectricTestRunner.class)
public class DictCodecTest {

    @Test
    public void encode_vector() throws Exception {
        Map<String, Integer> vector = new HashMap<String, Integer>();
        vector.put("9d", 123);
        vector.put("Bf", 18);
        vector.put("+7", 76543);
        // make sure elements are sorted by key in descending order
        String expected = "BfAAAS9dAAB7+7ASr/";

        DictCodec codec = new DictCodec();
        String encoded = codec.encode_vector(vector);
        assertEquals(encoded, expected);
    }

    @Test
    public void encode_decode_symetric() throws Exception {
        Map<String, Integer> initial = new HashMap<String, Integer>();
        initial.put("AA", 3);
        initial.put("6d", 9635);
        initial.put("93", 473);

        DictCodec codec = new DictCodec();
        String encoded = codec.encode_vector(initial);
        assertEquals(encoded.length(), 6 * 3);
        // the first element should be the AA key
        assertEquals(encoded.substring(0, 2), "AA");
        Map<String, Integer> decoded = codec.decode_vector(encoded);
        assertEquals(decoded, initial);
    }

}
