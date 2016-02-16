package com.orderbird;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ethan on 16/02/16.
 */
public class DictCodec extends BaseCodec {
    final int PREFIX_BYTES = 2;
    final int COUNT_BYTES = 6;

    public String encode_vector(Map<String, Integer> vector) {
        String result = "";

        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String o1, String o2) {
                // do reverse ordering
                return - o1.compareTo(o2);
            }
        };

        // sorted from largest key to lowest
        List sortedKeys = new ArrayList<String>(vector.keySet());
        Collections.sort(sortedKeys, comparator);

        for (Object key: sortedKeys) {
            int value = vector.get((String)key);
            String count = encode_count(value);
            result += (String)key + count;
        }
        return result;
    }

    public Map<String, Integer> decode_vector(String line) {
        int len = line.length();
        assert len % COUNT_BYTES == 0;
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (int i=0; i<len; i+=COUNT_BYTES) {
            String key = line.substring(i, i+PREFIX_BYTES);
            String chunk = line.substring(i+PREFIX_BYTES, i+COUNT_BYTES);
            result.put(key, decode_count(chunk));
        }
        return result;
    }

}
