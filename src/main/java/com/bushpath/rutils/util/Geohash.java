package com.bushpath.rutils.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Geohash {
    public final static byte BITS_PER_CHAR   = 5;
    public final static int  LATITUDE_RANGE  = 90;
    public final static int  LONGITUDE_RANGE = 180;

    public final static char[] charMap = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c',
        'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public final static HashMap<Character, Integer> charLookupTable =
        new HashMap<Character, Integer>();

    static {
        for (int i = 0; i < charMap.length; ++i) {
            charLookupTable.put(charMap[i], i);
        }
    }

    public static String encode(float latitude, float longitude,
                                int precision) {
        /* Set up 2-element arrays for longitude and latitude that we can flip
         * between while encoding */
        float[] high  = new float[2];
        float[] low   = new float[2];
        float[] value = new float[2];

        high[0] = LONGITUDE_RANGE;
        high[1] = LATITUDE_RANGE;
        low[0]  = - LONGITUDE_RANGE;
        low[1]  = - LATITUDE_RANGE;
        value[0] = longitude;
        value[1] = latitude;

        String hash = "";

        for (int p = 0; p < precision; ++p) {

            float middle = 0.0f;
            int charBits = 0;
            for (int b = 0; b < BITS_PER_CHAR; ++b) {
                int bit = (p * BITS_PER_CHAR) + b;

                charBits <<= 1;

                middle = (high[bit % 2] + low[bit % 2]) / 2;
                if (value[bit % 2] > middle) {
                    charBits |= 1;
                    low[bit % 2] = middle;
                } else {
                    high[bit % 2] = middle;
                }
            }

            hash += charMap[charBits];
        }

        return hash;
    }

    public static long hashToLong(String hash) {
        /* Long can fit 12 Geohash characters worth of precision. */
        int hashLen = hash.length();
        if (hashLen > 12) {
            hash = hash.substring(0, 12);
        }

        long longHash = 0;
        for (int i = hashLen - 1; i >= 0; --i) {
            char c = hash.charAt(i);
            longHash |= charLookupTable.get(c);

            if (i > 0) {
                longHash <<= BITS_PER_CHAR;
            }
        }

        return longHash;
    }
}
