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

import static io.jenetics.lattices.array.Primes.nextPrime;

import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * Extents the base-array with <em>copy</em> and <em>creation</em> capabilities.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
abstract class IntPrimitiveMap {

    static final int DEFAULT_CAPACITY = 277;
    static final double DEFAULT_MIN_LOAD_FACTOR = 0.2;
    static final double DEFAULT_MAX_LOAD_FACTOR = 0.5;

    static final byte FREE = 0;
    static final byte FULL = 1;
    static final byte REMOVED = 2;

    /**
     * The number of distinct associations in the map.
     */
    int distinct;

    /**
     * The table capacity c=table.length always satisfies the invariant
     * {@code c * minLoadFactor <= s <= c * maxLoadFactor}, where
     * {@code s=size()} is the number of associations currently contained.
     * The term "c * minLoadFactor" is called the "lowWaterMark", "c *
     * maxLoadFactor" is called the "highWaterMark". In other words, the table
     * capacity (and proportionally the memory used by this class) oscillates
     * within these constraints. The terms are precomputed and cached to avoid
     * recalculating them each time put(..) or removeKey(...) is called.
     */
    int lowWaterMark;

    int highWaterMark;

    /**
     * The minimum load factor for the hashtable.
     */
    double minLoadFactor;

    /**
     * The maximum load factor for the hashtable.
     */
    double maxLoadFactor;

    /**
     * The hash table keys.
     */
    int[] table;

    /**
     * The state of each hash table entry (FREE, FULL, REMOVED).
     */
    byte[] state;

    /**
     * The number of table entries in state==FREE.
     */
    int freeEntries;

    /**
     * Initializes the receiver. You will almost certainly need to override this
     * method in subclasses to initialize the hash table.
     *
     * @param initialCapacity the initial capacity of the receiver.
     * @param minLoadFactor the minLoadFactor of the receiver.
     * @param maxLoadFactor the maxLoadFactor of the receiver.
     * @throws IllegalArgumentException if {@code initialCapacity < 0 ||
     *         (minLoadFactor < 0.0 || minLoadFactor >= 1.0) ||
     *         (maxLoadFactor <= 0.0 || maxLoadFactor >= 1.0) ||
     *         (minLoadFactor >= maxLoadFactor)}
     */
    IntPrimitiveMap(int initialCapacity, double minLoadFactor, double maxLoadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                "Initial Capacity must not be less than zero: " + initialCapacity
            );
        }
        if (minLoadFactor < 0.0 || minLoadFactor >= 1.0) {
            throw new IllegalArgumentException(
                "Illegal minLoadFactor: " + minLoadFactor
            );
        }
        if (maxLoadFactor <= 0.0 || maxLoadFactor >= 1.0) {
            throw new IllegalArgumentException(
                "Illegal maxLoadFactor: " + maxLoadFactor
            );
        }
        if (minLoadFactor >= maxLoadFactor) {
            throw new IllegalArgumentException(
                "Illegal minLoadFactor: " + minLoadFactor +
                    " and maxLoadFactor: " + maxLoadFactor
            );
        }

        int capacity = initialCapacity;
        capacity = nextPrime(capacity);
        if (capacity == 0) {
            capacity = 1;
        }

        table = new int[capacity];
        state = new byte[capacity];

        // memory will be exhausted long before this pathological case happens, anyway.
        this.minLoadFactor = minLoadFactor;
        if (capacity == Primes.LARGEST_PRIME) {
            this.maxLoadFactor = 1.0;
        } else {
            this.maxLoadFactor = maxLoadFactor;
        }

        this.distinct = 0;
        this.freeEntries = capacity;
        this.lowWaterMark = 0;
        this.highWaterMark = chooseHighWaterMark(capacity, this.maxLoadFactor);
    }

    /**
     * Returns the number of key-value mappings in this map.  If the map
     * contains more than {@link Integer#MAX_VALUE} elements, returns
     * {@link Integer#MAX_VALUE}.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return distinct;
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
        return indexOfKey(key) != -1;
    }

    /**
     * Return the index for the given key, or {@code -1} if {@code this} map
     * doesn't contain the given key.
     *
     * @param key the key to be searched in the receiver.
     * @return the index where the key is contained in the receiver, else
     *         returns -1.
     */
    int indexOfKey(int key) {
        final int[] tab = table;
        final byte[] stat = state;
        final int length = tab.length;

        final int hash = Integer.hashCode(key) & 0x7FFFFFFF;

        int decrement = hash % (length - 2);
        if (decrement == 0) {
            decrement = 1;
        }

        // stop if we find a free slot, or if we find the key itself.
        // do skip over removed slots (yes, open addressing is like that...)
        // assertion: there is at least one FREE slot.
        int i = hash % length;
        while (stat[i] != FREE && (stat[i] == REMOVED || tab[i] != key)) {
            i -= decrement;
            if (i < 0) {
                i += length;
            }
        }

        if (stat[i] == FREE) {
            return -1;
        }

        return i;
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     */
    public void clear() {
        distinct = 0;
        freeEntries = table.length;
        trimToSize();
    }

    void trimToSize() {
        final int newCapacity = nextPrime((int)(1 + 1.2 * size()));
        if (table.length > newCapacity) {
            rehash(newCapacity);
        }
    }

    abstract void rehash(int newCapacity);

    /**
     * Ensures that the receiver can hold at least the specified number of
     * associations without needing to allocate new internal memory. If
     * necessary, allocates new internal memory and increases the capacity of
     * the receiver.
     * <p>
     * This method never needs to be called; it is for performance tuning only.
     * Calling this method before {@code put()}ing a large number of
     * associations boosts performance, because the receiver will grow only once
     * instead of potentially many times and hash collisions get less probable.
     *
     * @param minCapacity the desired minimum capacity.
     */
    void ensureCapacity(int minCapacity) {
        if (table.length < minCapacity) {
            int newCapacity = nextPrime(minCapacity);
            rehash(newCapacity);
        }
    }

    int chooseGrowCapacity(int size, double minLoad, double maxLoad) {
        return nextPrime(Math.max(size + 1, (int) ((4 * size / (3 * minLoad + maxLoad)))));
    }

    int chooseHighWaterMark(int capacity, double maxLoad) {
        return Math.min(capacity - 2, (int) (capacity * maxLoad));
    }

    int chooseLowWaterMark(int capacity, double minLoad) {
        return (int) (capacity * minLoad);
    }

    int chooseShrinkCapacity(int size, double minLoad, double maxLoad) {
        return nextPrime(Math.max(size + 1, (int) ((4 * size / (minLoad + 3 * maxLoad)))));
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
        for (int i = 0; i < table.length; ++i) {
            if (state[i] == FULL) {
                consumer.accept(table[i]);
            }
        }
    }

    public IntStream keys() {
        return IntStream.range(0, table.length)
            .filter(i -> state[i] == FULL);
    }

    /**
     * @param key the key to be added to the receiver.
     * @return the index where the key would need to be inserted, if it is not
     * already contained. Returns -index-1 if the key is already contained at
     * slot index. Therefore, if the returned index < 0, then it is already
     * contained at slot -index-1. If the returned index >= 0, then it is NOT
     * yet contained and should be inserted at slot index.
     */
    int indexOfInsertion(int key) {
        final var table = this.table;
        final var state = this.state;
        final var length = table.length;

        final int hash = Integer.hashCode(key) & 0x7FFFFFFF;
        int i = hash % length;

        // double hashing, see http://www.eece.unm.edu/faculty/heileman/hash/node4.html
        int decrement = hash % (length - 2);
        //int decrement = (hash / length) % length;
        if (decrement == 0) {
            decrement = 1;
        }

        // stop if we find a removed or free slot, or if we find the key itself
        // do NOT skip over removed slots (yes, open addressing is like that...)
        while (state[i] == FULL && table[i] != key) {
            i -= decrement;
            //hashCollisions++;
            if (i < 0) {
                i += length;
            }
        }

        if (state[i] == REMOVED) {
            // stop if we find a free slot, or if we find the key itself.
            // do skip over removed slots (yes, open addressing is like that...)
            // assertion: there is at least one FREE slot.
            int j = i;
            while (state[i] != FREE && (state[i] == REMOVED || table[i] != key)) {
                i -= decrement;
                //hashCollisions++;
                if (i < 0) {
                    i += length;
                }
            }
            if (state[i] == FREE) {
                i = j;
            }
        }

        if (state[i] == FULL) {
            // key already contained at slot i.
            // return a negative number identifying the slot.
            return -i - 1;
        }

        // not already contained, should be inserted at slot i.
        // return a number >= 0 identifying the slot.
        return i;
    }

}
