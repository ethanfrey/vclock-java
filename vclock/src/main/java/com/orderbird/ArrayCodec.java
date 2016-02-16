package com.orderbird;

import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 16/02/16.
 */
public class ArrayCodec {
    int COUNT_BYTES = 4;

    protected String encode_count(int count) {
        final byte[] binary = new byte[] {
                (byte)(count >>> 16),
                (byte)(count >>> 8),
                (byte)count
        };
        return Base64.encodeToString(binary, Base64.DEFAULT).trim();
    }

    protected int decode_count(String encoded) {
        byte[] bytes = Base64.decode(encoded, Base64.DEFAULT);
        int result = (bytes[0] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF);
        return result;
    }

    public String encode_vector(List<Integer> vector) {
        String result = "";
        for (int i=0; i<vector.size(); i++) {
            result += encode_count((int)vector.get(i));
        }
        return result;
   }

    public List<Integer> decode_vector(String line) {
        int len = line.length();
        assert len % 4 == 0;
        List<Integer> result = new ArrayList<Integer>();
        for (int i=0; i<len; i+=4) {
            String chunk = line.substring(i, i+4);
            result.add(decode_count(chunk));
        }
        return result;
    }
}
