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
package io.jenetics.lattices.map;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Abstract implementation of {@code Int[double|int|long]Map} classes. It
 * contains mostly the key hashing code.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public abstract class IntPrimitiveMap {

    /**
     * Some keys are reserved values. In such cases, the mapped values are stored
     * in this class as <em>side</em> car.
     */
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

    int indexOf(int key) {
        int index = mask(key);
        int keyAtIndex = keys[index];

        if (keyAtIndex == key || keyAtIndex == EMPTY_KEY) {
            return index;
        }

        int removedIndex = keyAtIndex == REMOVED_KEY ? index : -1;

        for (int i = 1; i < INITIAL_LINEAR_PROBE; i++) {
            int nextIndex = (index + i) & (keys.length - 1);
            keyAtIndex = keys[nextIndex];

            if (keyAtIndex == key) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        return indexOf_2(key, removedIndex);
    }

    int indexOf_2(int key, int removedIndex) {
        int index = mask(mix2(key));

        for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
            int nextIndex = (index + i) & (keys.length - 1);
            int keyAtIndex = keys[nextIndex];

            if (keyAtIndex == key) {
                return nextIndex;
            }
            if (keyAtIndex == EMPTY_KEY) {
                return removedIndex == -1 ? nextIndex : removedIndex;
            }
            if (keyAtIndex == REMOVED_KEY && removedIndex == -1) {
                removedIndex = nextIndex;
            }
        }

        int nextIndex = mix1(key);
        int spread2 = Integer.reverse(mix2(key)) | 1;

        while (true) {
            nextIndex = mask(nextIndex + spread2);
            int keyAtIndex = keys[nextIndex];

            if (keyAtIndex == key) {
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

    int mask(int spread) {
        return spread & (keys.length - 1);
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
     * Returns {@code true} if this map contains a mapping for the specified
     * key.
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     *         key
     */
    public boolean containsKey(int key) {
        if (key == EMPTY_KEY) {
            return sentinel().hasEmptyKey;
        } else if (key == REMOVED_KEY) {
            return sentinel().hasRemovedKey;
        } else {
            return keys[indexOf(key)] == key;
        }
    }

    /**
     * Removes the given key with its associated element from the receiver, if
     * present.
     *
     * @param key the key to be removed from the receiver.
     */
    public void remove(int key) {
        if (key == EMPTY_KEY) {
            sentinel().hasEmptyKey = false;
        } else if (key == REMOVED_KEY) {
            sentinel().hasRemovedKey = false;
        } else {
            int index = indexOf(key);
            if (keys[index] == key) {
                removeKeyAtIndex(index);
            }
        }
    }

    private void removeKeyAtIndex(int index) {
        keys[index] = REMOVED_KEY;
        --occupiedWithData;
        ++occupiedWithSentinels;
    }

    void addKeyAtIndex(int key, int index) {
        if (keys[index] == REMOVED_KEY) {
            --occupiedWithSentinels;
        }

        keys[index] = key;
        ++occupiedWithData;
        if (occupiedWithData + occupiedWithSentinels > maxOccupiedWithData()) {
            rehashAndGrow();
        }
    }

    /**
     * Trims the map to optimize for the new size.
     *
     * @return {@code true} if the map has been trimmed and rehashed,
     *         {@code false} otherwise
     */
    public boolean trim() {
        final int newCapacity = alignCapacity(size());
        if (keys.length > newCapacity) {
            rehash(newCapacity);
            return true;
        }
        return false;
    }

    private void rehashAndGrow() {
        int max = maxOccupiedWithData();
        int newCapacity = Math.max(max, alignCapacity((occupiedWithData + 1) << 1));
        if (occupiedWithSentinels > 0 && (max >> 1) + (max >> 2) < occupiedWithData) {
            newCapacity <<= 1;
        }
        rehash(newCapacity);
    }

    abstract void rehash(int newCapacity);

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
     * Applies a procedure to each key of the receiver.
     *
     * @param consumer the procedure to be applied
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
            Arrays.stream(keys)
                .filter(key -> key != EMPTY_KEY && key != REMOVED_KEY)
        );
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

    int maxOccupiedWithData() {
        return keys.length >> 1;
    }

}
