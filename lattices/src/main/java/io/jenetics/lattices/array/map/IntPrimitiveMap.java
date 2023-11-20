package io.jenetics.lattices.array.map;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

abstract class IntPrimitiveMap {

    static abstract class Sentinel {
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

    static final int CACHE_LINE_SIZE = 64;
    static final int KEY_SIZE = 4;
    static final int INITIAL_LINEAR_PROBE = CACHE_LINE_SIZE/KEY_SIZE/2;
    static final int DEFAULT_INITIAL_CAPACITY = 8;

    static final int EMPTY_KEY = 0;
    static final int REMOVED_KEY = 1;


    int[] keys;
    int occupiedWithData;
    int occupiedWithSentinels;

    IntPrimitiveMap() {
    }

    abstract Sentinel sentinel();

    void allocate(final int capacity) {
        keys = new int[capacity];
    }

    /**
     * Returns the number of key-value mappings in this map. If the map
     * contains more than {@link Integer#MAX_VALUE} elements, returns
     * {@link Integer#MAX_VALUE}.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return occupiedWithData + sentinel().size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Removes all mappings from this map (optional operation). The map will be
     * empty after this call returns.
     */
    public void clear() {
        sentinel().clear();
        occupiedWithData = 0;
        occupiedWithSentinels = 0;
        Arrays.fill(keys, EMPTY_KEY);
    }

    /**
     * Applies a procedure to each key of the receiver, if any. Note: Iterates
     * over the keys in no particular order. Subclasses can define a particular
     * order, for example, "sorted by key". All methods which <i>can</i> be
     * expressed in terms of this method (most methods can) <i>must
     * guarantee</i> to use the <i>same</i> order defined by this method, even
     * if it is no particular order. This is necessary so that, for example,
     * methods {@code keys} and {@code values} will yield association pairs,
     * not two uncorrelated lists.
     *
     * @param consumer the procedure to be applied. Stops iteration if the
     *        procedure returns {@code false}, otherwise continues.
     */
    public void forEachKey(IntConsumer consumer) {
        if (sentinel().hasEmptyKey) {
            consumer.accept(EMPTY_KEY);
        }
        if (sentinel().hasRemovedKey) {
            consumer.accept(REMOVED_KEY);
        }
        for (int key : keys) {
            if (key != EMPTY_KEY && key != REMOVED_KEY) {
                consumer.accept(key);
            }
        }
    }

    /**
     * Returns a stream containing all the keys in this map.
     *
     * @return a stream containing all the keys in this map
     */
    public IntStream keys() {
        final var builder = IntStream.builder();
        if (sentinel().hasEmptyKey) {
            builder.accept(EMPTY_KEY);
        }
        if (sentinel().hasRemovedKey) {
            builder.accept(REMOVED_KEY);
        }

        return IntStream.concat(
            builder.build(),
            IntStream.range(0, size())
                .filter(i -> keys[i] != EMPTY_KEY && keys[i] != REMOVED_KEY)
        );
    }

    static int smallestPowerOfTwoGreaterThan(int n) {
        return n > 1 ? Integer.highestOneBit(n - 1) << 1 : 1;
    }

}
