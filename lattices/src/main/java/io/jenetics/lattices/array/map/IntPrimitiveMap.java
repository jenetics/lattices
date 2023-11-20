package io.jenetics.lattices.array.map;

import java.util.stream.IntStream;

abstract class IntPrimitiveMap {

    abstract class Sentinel {
        boolean hasEmptyKey;
        boolean hasRemovedKey;

        int size() {
            return (hasEmptyKey ? 1 : 0) + (hasRemovedKey ? 1 : 0);
        }

        void clear() {
            hasEmptyKey = false;
            hasRemovedKey = false;
        }
    }

    static final int EMPTY_KEY = 0;
    static final int REMOVED_KEY = 1;

    int[] keys;

    void allocate(final int capacity) {
        keys = new int[capacity];
    }

    abstract int size();

    public IntStream keys() {
        return IntStream.range(0, size())
            .filter(i -> keys[i] != EMPTY_KEY && keys[i] != REMOVED_KEY);
    }

}
