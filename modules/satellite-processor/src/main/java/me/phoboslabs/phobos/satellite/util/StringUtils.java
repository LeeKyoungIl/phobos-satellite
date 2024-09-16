package me.phoboslabs.phobos.satellite.util;

public class StringUtils {

    private StringUtils() {
    }

    public static String sequenceGenerator() {
        return new ObjectId().toHexString();
    }
}
