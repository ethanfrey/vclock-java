package com.orderbird;

import android.util.Base64;

/**
 * Created by ethan on 16/02/16.
 */
public class BaseCodec {

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
}
