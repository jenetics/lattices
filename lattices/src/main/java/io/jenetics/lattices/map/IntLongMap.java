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

import static java.util.Objects.requireNonNull;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.LongConsumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import io.jenetics.lattices.function.IntLongConsumer;
import io.jenetics.lattices.function.IntLongToLongFunction;

/**
 * Maps {@code int} keys to {@code long} values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public final class IntLongMap extends IntPrimitiveMap {

    private static final class LongSentinel extends Sentinel {
        long emptyKeyValue;
        long removedKeyValue;

        void setEmptyKeyValue(long value) {
            hasEmptyKey = true;
            emptyKeyValue = value;
        }

        void setRemovedKeyValue(long value) {
            hasRemovedKey = true;
            removedKeyValue = value;
        }

        boolean contains(long value) {
            return
                (hasEmptyKey && emptyKeyValue == value) ||
                (hasRemovedKey && removedKeyValue == value);
        }
    }

    private static final long EMPTY_VALUE = 0;

    private final LongSentinel sentinel = new LongSentinel();

    private long[] values;

    /**
     * Create a new map with default capacity.
     */
    public IntLongMap() {
        allocate(DEFAULT_INITIAL_CAPACITY << 1);
    }

    /**
     * Create a new map with the desired {@code capacity}.
     *
     * @param capacity the desired capacity
     * @throws IllegalArgumentException if the given capacity is smaller then
     *         zero
     */
    public IntLongMap(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(
                "Initial capacity cannot be less than 0."
            );
        }

        allocate(alignCapacity(capacity << 1));
    }

    @Override
    LongSentinel sentinel() {
        return sentinel;
    }

    @Override
    void allocate(int capacity) {
        super.allocate(capacity);
        values = new long[capacity];
    }

    /**
     * Associates the given key with the given value. Replaces any old
     * {@code (key, someOtherValue)} association, if existing.
     *
     * @param key the key the value shall be associated with.
     * @param value the value to be associated.
     */
    public void put(int key, long value) {
        switch (key) {
            case Sentinel.EMPTY_KEY -> sentinel.setEmptyKeyValue(value);
            case Sentinel.REMOVED_KEY -> sentinel.setRemovedKeyValue(value);
            default -> {
                final int index = indexOf(key);
                final int keyAtIndex = keys[index];

                values[index] = value;
                if (keyAtIndex != key) {
                    addKeyAtIndex(key, index);
                }
            }
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or
     * {@code defaultValue} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @param defaultValue the default mapping of the key
     * @return the value to which the specified key is mapped, or
     *         {@code defaultValue} if this map contains no mapping for the key.
     */
    public long getOrDefault(int key, long defaultValue) {
        if (key == Sentinel.EMPTY_KEY && sentinel.hasEmptyKey) {
            return sentinel.emptyKeyValue;
        } else if (key == Sentinel.REMOVED_KEY && sentinel.hasRemovedKey) {
            return sentinel.removedKeyValue;
        } else if (occupiedWithSentinels == 0) {
            int index = mask(key);

            for (int i = 0; i < INITIAL_LINEAR_PROBE; ++i) {
                final int keyAtIndex = keys[index];

                if (keyAtIndex == key) {
                    return values[index];
                } else if (keyAtIndex == Sentinel.EMPTY_KEY) {
                    return defaultValue;
                } else {
                    index = (index + 1) & (keys.length - 1);
                }
            }

            index = indexOf_2(key, -1);
            return keys[index] == key ? values[index] : defaultValue;
        } else {
            final int index = indexOf(key);
            return keys[index] == key ? values[index] : defaultValue;
        }
    }

    /**
     * Returns the value associated with the specified key. It is often a good
     * idea to first check with {@link #containsKey(int)} whether the given key
     * has a value associated or not, i.e., whether there exists an association
     * for the given key or not.
     *
     * @param key the key to be searched for.
     * @return the value associated with the specified key; {@code 0} if no
     * such key is present.
     */
    public long get(int key) {
        return getOrDefault(key, EMPTY_VALUE);
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the specified
     * value. This operation will require linear time.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value
     */
    public boolean containsValue(long value) {
        if (sentinel.contains(value)) {
            return true;
        }
        for (int i = 0; i < values.length; ++i) {
            if (keys[i] != Sentinel.EMPTY_KEY &&
                keys[i] != Sentinel.REMOVED_KEY &&
                Double.compare(values[i], value) == 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Applies a procedure to each (key,value) pair of the receivers, if any.
     * Iteration order is guaranteed to be <i>identical</i> to the order used by
     * method {@link #forEachKey(IntConsumer)}.
     *
     * @param consumer the procedure to be applied
     */
    public void forEach(IntLongConsumer consumer) {
        requireNonNull(consumer);

        if (sentinel.hasEmptyKey) {
            consumer.accept(Sentinel.EMPTY_KEY, sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey) {
            consumer.accept(Sentinel.REMOVED_KEY, sentinel.removedKeyValue);
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != Sentinel.EMPTY_KEY && keys[i] != Sentinel.REMOVED_KEY) {
                consumer.accept(keys[i], values[i]);
            }
        }
    }

    /**
     * Applies a procedure to each value of the receivers, if any. Iteration
     * order is guaranteed to be <i>identical</i> to the order used by
     * method {@link #forEachKey(IntConsumer)}.
     *
     * @param consumer the procedure to be applied
     */
    public void forEachValue(LongConsumer consumer) {
        requireNonNull(consumer);

        if (sentinel.hasEmptyKey) {
            consumer.accept(sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey) {
            consumer.accept(sentinel.removedKeyValue);
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != Sentinel.EMPTY_KEY && keys[i] != Sentinel.REMOVED_KEY) {
                consumer.accept(values[i]);
            }
        }
    }

    /**
     * Update all map values using the given function {@code fn}.
     *
     * @param fn the update function
     */
    public void update(IntLongToLongFunction fn) {
        requireNonNull(fn);

        if (sentinel.hasEmptyKey) {
            final var value = fn.apply(Sentinel.EMPTY_KEY, sentinel.emptyKeyValue);
            if (value != EMPTY_VALUE) {
                sentinel.emptyKeyValue = value;
            } else {
                sentinel.hasEmptyKey = false;
            }
        }
        if (sentinel.hasRemovedKey) {
            final var value = fn.apply(Sentinel.REMOVED_KEY, sentinel.removedKeyValue);
            if (value != EMPTY_VALUE) {
                sentinel.removedKeyValue = value;
            } else {
                sentinel.hasRemovedKey = false;
            }
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != Sentinel.EMPTY_KEY && keys[i] != Sentinel.REMOVED_KEY) {
                final var value = fn.apply(keys[i], values[i]);
                if (value != EMPTY_VALUE) {
                    values[i] = value;
                } else {
                    remove(keys[i]);
                }
            }
        }
    }

    /**
     * Return all map values.
     *
     * @return all map values
     */
    public LongStream values() {
        return values(key -> true);
    }

    /**
     * Return all map values where its key matches the given {@code keyFilter}
     * predicate.
     *
     * @param keyFilter the value key filter
     * @return the filtered map values
     */
    public LongStream values(IntPredicate keyFilter) {
        requireNonNull(keyFilter);

        final var builder = LongStream.builder();
        if (sentinel.hasEmptyKey && keyFilter.test(Sentinel.EMPTY_KEY)) {
            builder.accept(sentinel.emptyKeyValue);
        }
        if (sentinel.hasRemovedKey && keyFilter.test(Sentinel.REMOVED_KEY)) {
            builder.accept(sentinel.removedKeyValue);
        }

        return LongStream.concat(
            builder.build(),
            IntStream.range(0, keys.length)
                .filter(i -> {
                    final var key = keys[i];
                    return key != Sentinel.EMPTY_KEY &&
                        key != Sentinel.REMOVED_KEY &&
                        keyFilter.test(key);
                })
                .mapToLong(i -> values[i])
        );
    }

    @Override
    void rehash(int newCapacity) {
        final var oldKeys = keys;
        final var oldValues = values;

        allocate(newCapacity);
        occupiedWithData = 0;
        occupiedWithSentinels = 0;

        for (int i = 0; i < oldKeys.length; ++i) {
            if (oldKeys[i] != Sentinel.EMPTY_KEY && oldKeys[i] != Sentinel.REMOVED_KEY) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }

}