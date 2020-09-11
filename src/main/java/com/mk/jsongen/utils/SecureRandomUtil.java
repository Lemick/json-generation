package com.mk.jsongen.utils;


import java.security.SecureRandom;
import java.util.List;

public class SecureRandomUtil {

    public static SecureRandom secureRandom = new SecureRandom();

    public static <T> T randomElem(List<T> c) {
        return c.get(randomInt(c.size() - 1));
    }

    public static int randomInt(int min, int max) {
        return secureRandom.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }

    public static long randomLong(long min, long max) {
        return secureRandom.longs(min, (max + 1)).limit(1).findFirst().getAsLong();
    }

    public static int randomInt(int max) {
        return randomInt(0, max);
    }

    public static boolean randomBoolean() {
        return randomInt(0, 1) == 1;
    }
}
