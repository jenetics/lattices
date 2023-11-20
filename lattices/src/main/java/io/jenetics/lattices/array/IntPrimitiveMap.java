/*
 * Java Lattice Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.lattices.array;

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
    int size() {
        return occupiedWithData + sentinel().size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key
     */
    boolean containsKey(int key) {
        if (key == EMPTY_KEY) {
            return sentinel().hasEmptyKey;
        } else if (key == REMOVED_KEY) {
            return sentinel().hasRemovedKey;
        } else {
            return keys[probe(key)] == key;
        }
    }

    /**
     * Removes all mappings from this map (optional operation). The map will be
     * empty after this call returns.
     */
    void clear() {
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
    void forEachKey(IntConsumer consumer) {
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
    IntStream keys() {
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

    void removeKeyAtIndex(int index) {
        keys[index] = REMOVED_KEY;
        --occupiedWithData;
        ++occupiedWithSentinels;
    }

    int probe(int element) {
        int index = mask(element);
        int keyAtIndex = keys[index];

        if (keyAtIndex == element || keyAtIndex == EMPTY_KEY) {
            return index;
        }

        int removedIndex = keyAtIndex == REMOVED_KEY ? index : -1;
        for (int i = 1; i < INITIAL_LINEAR_PROBE; i++) {
            int nextIndex = (index + i) & (keys.length - 1);
            keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        return probeTwo(element, removedIndex);
    }

    int probeTwo(int element, int removedIndex) {
        int index = spreadTwoAndMask(element);
        for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
            int nextIndex = (index + i) & (keys.length - 1);
            int keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        return probeThree(element, removedIndex);
    }

    private int probeThree(int element, int removedIndex) {
        int nextIndex = mix1(element);
        int spreadTwo = Integer.reverse(mix2(element)) | 1;

        while (true) {
            nextIndex = mask(nextIndex + spreadTwo);
            int keyAtIndex = keys[nextIndex];
            if (keyAtIndex == element) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }
    }

    private int spreadTwoAndMask(int element) {
        return mask(mix2(element));
    }

    int mask(int spread) {
        return spread & (keys.length - 1);
    }

    static int alignCapacity(int capacity) {
        return capacity > 1 ? Integer.highestOneBit(capacity - 1) << 1 : 1;
    }

    private static int mix1(int code) {
        int code1 = code;
        code1 ^= code1 >>> 15;
        code1 *= 0xACAB2A4D;
        code1 ^= code1 >>> 15;
        code1 *= 0x5CC7DF53;
        code1 ^= code1 >>> 12;
        return code1;
    }

    private static int mix2(int code) {
        int code1 = code;
        code1 ^= code1 >>> 14;
        code1 *= 0xBA1CCD33;
        code1 ^= code1 >>> 13;
        code1 *= 0x9B6296CB;
        code1 ^= code1 >>> 12;
        return code1;
    }

}
