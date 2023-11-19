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

import java.util.HashMap;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import io.jenetics.lattices.function.IntDoubleConsumer;
import io.jenetics.lattices.function.IntDoubleToDoubleFunction;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public class IntDoubleMap extends IntPrimitiveMap {

    private static final double SENTINEL = 0;

    /**
     * The hash table values.
     */
    double[] values;

    /**
     * Constructs an empty map with the specified initial capacity and the
     * specified minimum and maximum load factor.
     *
     * @param initialCapacity the initial capacity.
     * @param minLoadFactor the minimum load factor.
     * @param maxLoadFactor the maximum load factor.
     * @throws IllegalArgumentException if {@code initialCapacity < 0 ||
     *         (minLoadFactor < 0.0 || minLoadFactor >= 1.0) ||
     *         (maxLoadFactor <= 0.0 || maxLoadFactor >= 1.0) ||
     *         (minLoadFactor >= maxLoadFactor)}
     */
    public IntDoubleMap(int initialCapacity, double minLoadFactor, double maxLoadFactor) {
        super(initialCapacity, minLoadFactor, maxLoadFactor);
        values = new double[table.length];
    }

    /**
     * Constructs an empty map with the specified initial capacity and default
     * load factors.
     *
     * @param initialCapacity the initial capacity of the map.
     * @throws IllegalArgumentException if the initial capacity is less than
     * zero.
     */
    public IntDoubleMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_MIN_LOAD_FACTOR, DEFAULT_MAX_LOAD_FACTOR);
    }

    /**
     * Constructs an empty map with default capacity and default load factors.
     */
    public IntDoubleMap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the specified
     * value. This operation will require linear time.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the
     *         specified value
     */
    public boolean containsValue(double value) {
        return indexOfValue(value) != -1;
    }

    /**
     * Return the index for the given value, or {@code -1} if {@code this} map
     * doesn't contain the given value.
     *
     * @param value the value to be searched in the receiver.
     * @return the index where the key is contained in the receiver, else
     *         returns -1.
     */
    int indexOfValue(double value) {
        final double[] val = values;
        final byte[] stat = state;

        for (int i = stat.length; --i >= 0; ) {
            if (stat[i] == FULL && Double.compare(val[i], value) == 0) {
                return i;
            }
        }

        return -1;
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
    public double get(int key) {
        return getOrDefault(key, SENTINEL);
    }

    public double getOrDefault(int key, double defaultValue) {
        int i = indexOfKey(key);
        if (i < 0) {
            return defaultValue;
        }

        return values[i];
    }

    /**
     * Associates the given key with the given value. Replaces any old
     * {@code (key, someOtherValue)} association, if existing.
     *
     * @param key the key the value shall be associated with.
     * @param value the value to be associated.
     * @return the old value if the receiver did not already contain such a key
     *         or 0 if the receiver did already contain such a key
     */
    public double put(int key, double value) {
        int i = indexOfInsertion(key);
        if (i < 0) {
            i = -i - 1;
            final double oldValue = values[i];
            values[i] = value;
            return oldValue;
        }

        if (this.distinct > this.highWaterMark) {
            final int newCapacity = chooseGrowCapacity(
                distinct + 1,
                minLoadFactor,
                maxLoadFactor
            );
            rehash(newCapacity);
            return put(key, value);
        }

        table[i] = key;
        values[i] = value;
        if (this.state[i] == FREE) {
            --freeEntries;
        }
        state[i] = FULL;
        ++distinct;

        if (this.freeEntries < 1) {
            final int newCapacity = chooseGrowCapacity(
                distinct + 1,
                minLoadFactor,
                maxLoadFactor
            );
            rehash(newCapacity);
        }

        return SENTINEL;
    }

    /**
     * Removes the given key with its associated element from the receiver, if
     * present.
     *
     * @param key the key to be removed from the receiver.
     * @return the old value if the receiver contained the specified key, zero
     *         otherwise.
     */
    public double remove(int key) {
        final int i = indexOfKey(key);
        if (i <= 0) {
            return SENTINEL;
        }

        state[i] = REMOVED;
        distinct--;

        if (distinct < lowWaterMark) {
            final int newCapacity = chooseShrinkCapacity(
                distinct,
                minLoadFactor,
                maxLoadFactor
            );
            rehash(newCapacity);
        }

        return values[i];
    }

    public void putAll(IntDoubleMap m) {
    }

    /**
     * Replaces each entry's value with the result of invoking the given
     * function on that entry until all entries have been processed, or the
     * function throws an exception.  Exceptions thrown by the function are
     * relayed to the caller.
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param function the function to apply to each entry
     */
    void replaceAll(IntDoubleToDoubleFunction function) {
        for (int i = 0; i < table.length; ++i) {
            if (state[i] == FULL) {
                values[i] = function.apply(table[i], values[i]);
            }
        }
    }

    /**
     * Applies a procedure to each (key,value) pair of the receivers, if any.
     * Iteration order is guaranteed to be <i>identical</i> to the order used by
     * method {@link #forEachKey(IntConsumer)}.
     *
     * @param consumer the procedure to be applied. Stops iteration if the
     *        procedure returns {@code false}, otherwise continues.
     */
    public void forEach(final IntDoubleConsumer consumer) {
        for (int i = table.length; i-- > 0; ) {
            if (state[i] == FULL) {
                consumer.accept(table[i], values[i]);
            }
        }
    }

    public void forEachValue(DoubleConsumer consumer) {
        for (int i = 0; i < table.length; ++i) {
            if (state[i] == FULL) {
                consumer.accept(values[i]);
            }
        }
    }

    public DoubleStream values() {
        return IntStream.range(0, table.length)
            .filter(i -> state[i] == FULL)
            .mapToDouble(i -> values[i]);
    }

    /**
     * Returns the first key the given value is associated with. It is often a
     * good idea to first check with {@link #containsValue(double)} whether
     * there exists an association from a key to this value. Search order is
     * guaranteed to be <i>identical</i> to the order used by method
     * {@link #forEachKey(IntConsumer)}.
     *
     * @param value the value to search for.
     * @return the first key for which holds {@code get(key) == value}; returns
     *         {@link Integer#MIN_VALUE} if no such key exists.
     */
    public int keyOf(double value) {
        int i = indexOfValue(value);
        if (i < 0) {
            return Integer.MIN_VALUE;
        } else {
            return table[i];
        }
    }


    /**
     * Rehashes the contents of the receiver into a new table with a smaller or
     * larger capacity. This method is called automatically when the number of
     * keys in the receiver exceeds the high watermark or falls below the low
     * watermark.
     */
    @Override
    void rehash(int newCapacity) {
        int oldCapacity = table.length;

        if (newCapacity <= this.distinct) {
            throw new AssertionError();
        }

        int[] oldTable = table;
        double[] oldValues = values;
        byte[] oldState = state;

        int[] newTable = new int[newCapacity];
        double[] newValues = new double[newCapacity];
        byte[] newState = new byte[newCapacity];

        lowWaterMark = chooseLowWaterMark(newCapacity, minLoadFactor);
        highWaterMark = chooseHighWaterMark(newCapacity, maxLoadFactor);

        table = newTable;
        values = newValues;
        state = newState;
        freeEntries = newCapacity - distinct;

        for (int i = oldCapacity; i-- > 0; ) {
            if (oldState[i] == FULL) {
                int element = oldTable[i];
                int index = indexOfInsertion(element);
                newTable[index] = element;
                newValues[index] = oldValues[i];
                newState[index] = FULL;
            }
        }
    }

}
