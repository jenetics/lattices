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

/**
 * Implementation of a <em>sparse</em> array of {@code double} values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public class SparseDoubleArray implements Array.OfDouble {

    private static final int DEFAULT_CAPACITY = 277;
    private static final double DEFAULT_MIN_LOAD_FACTOR = 0.2;
    private static final double DEFAULT_MAX_LOAD_FACTOR = 0.5;

    private static final byte FREE = 0;
    private static final byte FULL = 1;
    private static final byte REMOVED = 2;

    private int[] table;
    private double[] values;
    private byte[] state;
    private int freeEntries;

    private final int length;

    public SparseDoubleArray(final int length) {
        this.length = length;

        this.table = new int[DEFAULT_CAPACITY];
        this.values = new double[DEFAULT_CAPACITY];
        this.state = new byte[DEFAULT_CAPACITY];
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public double get(final int index) {
        return 0;
    }

    @Override
    public void set(final int index, final double value) {

        var foo = new HashMap<String, String>();

    }

    private int indexOfInsertion(final int key) {
        final int[] tab = table;
        final byte[] stat = state;
        final int length = tab.length;

        final int hash = Integer.hashCode(key) & 0x7FFFFFFF;
        int i = hash % length;
        int decrement = hash % (length - 2);
        if (decrement == 0) {
            decrement = 1;
        }

        while (stat[i] == FULL && tab[i] != key) {
            i -= decrement;
            if (i < 0) {
                i += length;
            }
        }

        if (stat[i] == REMOVED) {
            int j = i;
            while (stat[i] != FREE && (stat[i] == REMOVED || tab[i] != key)) {
                i -= decrement;
                if (i < 0) {
                    i += length;
                }
            }
            if (stat[i] == FREE) {
                i = j;
            }
        }

        if (stat[i] == FULL) {
            return -i - 1;
        }
        return i;
    }

    @Override
    public SparseDoubleArray copy(final int start, final int length) {
        return null;
    }

    @Override
    public SparseDoubleArray like(final int length) {
        return null;
    }

}
