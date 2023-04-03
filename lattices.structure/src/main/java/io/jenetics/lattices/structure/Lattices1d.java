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
package io.jenetics.lattices.structure;

import static java.util.Objects.requireNonNull;

/**
 * This class provides structural access to a one-dimensional array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public final class Lattices1d {

    private final Structure1d structure;

    /**
     * Create a new object for accessing multidimensional data from an array.
     *
     * @param structure the defining access structure
     */
    public Lattices1d(Structure1d structure) {
        this.structure = requireNonNull(structure);
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param <T> the lattice element type
     * @return the element at the given {@code index}
     */
    public <T> T get(T[] array, Index1d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param <T> the lattice element type
     * @return the element at the given {@code index}
     */
    public <T> T get(T[] array, int index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     * @param <T> the lattice element type
     */
    public <T> void set(T[] array, Index1d index, T value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     * @param <T> the lattice element type
     */
    public <T> void set(T[] array, int index, T value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public int get(int[] array, Index1d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public int get(int[] array, int index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(int[] array, Index1d index, int value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(int[] array, int index, int value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public long get(long[] array, Index1d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public long get(long[] array, int index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(long[] array, Index1d index, long value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(long[] array, int index, long value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public double get(double[] array, Index1d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public double get(double[] array, int index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(double[] array, Index1d index, double value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(double[] array, int index, double value) {
        array[structure.layout().offset(index)] = value;
    }

}