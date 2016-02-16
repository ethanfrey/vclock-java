package com.orderbird;
;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 16/02/16.
 */
public class ArrayCodec extends BaseCodec {
    final int COUNT_BYTES = 4;

    public String encode_vector(List<Integer> vector) {
        String result = "";
        for (int i=0; i<vector.size(); i++) {
            result += encode_count((int)vector.get(i));
        }
        return result;
   }

    public List<Integer> decode_vector(String line) {
        int len = line.length();
        assert len % COUNT_BYTES == 0;
        List<Integer> result = new ArrayList<Integer>();
        for (int i=0; i<len; i+=COUNT_BYTES) {
            String chunk = line.substring(i, i+COUNT_BYTES);
            result.add(decode_count(chunk));
        }
        return result;
    }
}
