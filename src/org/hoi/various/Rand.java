package org.hoi.various;

import java.util.Random;

public class Rand {
    final private static Random rng = new Random();

    final private static float FLOAT_DELTA = -(float) Integer.MIN_VALUE;
    final private static float FLOAT_MAX = 2 * (float) Integer.MAX_VALUE + 1f;

    final private static double DOUBLE_DELTA = -(double) Long.MIN_VALUE;
    final private static double DOUBLE_MAX = 2 * (double) Long.MAX_VALUE + 1d;

    public static boolean nextBool () {
        return (nextByte() & 1) == 1;
    }

    public static byte[] nextBytes (int len) {
        byte[] r = new byte[len];
        rng.nextBytes(r);

        return r;
    }

    public static byte nextByte () {
        return nextBytes(1)[0];
    }

    public static short nextShort () {
        byte[] b = nextBytes(2);
        return (short) (b[0] | (b[1] << 8));
    }

    public static char nextChar () {
        byte[] b = nextBytes(2);
        return (char) (b[0] | (b[1] << 8));
    }

    public static int nextInt () {
        return rng.nextInt();
    }

    public static int nextInt (int from, int to) {
        return rng.nextInt(from + to) - from;
    }

    public static long nextLong () {
        return rng.nextLong();
    }

    public static float nextFloat () {
        return rng.nextFloat();
    }

    public static float nextFloat (float from, float to) {
        float i = (float) nextInt() + FLOAT_DELTA;
        return (to - from) * (i / FLOAT_MAX) + from;
    }

    public static double nextDouble () {
        return rng.nextDouble();
    }

    public static double nextDouble (double from, double to) {
        double i = (float) nextLong() + DOUBLE_DELTA;
        return (to - from) * (i / DOUBLE_MAX) + from;
    }
}
