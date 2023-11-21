package io.jenetics.lattices.array;

public final class Utils {
    private Utils() {
    }

    static boolean contains(int value, int[] values) {
        for (var v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

    static boolean contains(long value, long[] values) {
        for (var v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

    static boolean contains(double value, double[] values) {
        for (var v : values) {
            if (v == value) {
                return true;
            }
        }
        return false;
    }

}
