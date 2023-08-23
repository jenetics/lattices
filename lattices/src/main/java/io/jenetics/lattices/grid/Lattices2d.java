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
package io.jenetics.lattices.grid;

import static java.util.Objects.requireNonNull;

import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.OffsetMapper2d;

/**
 * This class provides structural access to a one-dimensional array.
 *
 * <pre>{@code
 * final var values = new double[structure.extent().size()]
 * final var lattices = Lattices2d.of(structure);
 *
 * lattice.set(values, index, 0.5);
 * final var value = lattices.get(values, index);
 * assert value == 0.5;
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public final class Lattices2d {

    private final OffsetMapper2d mapper;

    /**
     * Create a new object for accessing multidimensional data from an array.
     *
     * @param mapper the defining offset mapper
     */
    public Lattices2d(OffsetMapper2d mapper) {
        this.mapper = requireNonNull(mapper);
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
    public <T> T get(T[] array, Index2d index) {
        return array[mapper.offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @param <T> the lattice element type
     * @return the element at the given {@code index}
     */
    public <T> T get(T[] array, int row, int col) {
        return array[mapper.offset(row, col)];
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
    public <T> void set(T[] array, Index2d index, T value) {
        array[mapper.offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @param value the new lattice value at the given {@code index}
     * @param <T> the lattice element type
     */
    public <T> void set(T[] array, int row, int col, T value) {
        array[mapper.offset(row, col)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public int get(int[] array, Index2d index) {
        return array[mapper.offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @return the element at the given {@code index}
     */
    public int get(int[] array, int row, int col) {
        return array[mapper.offset(row, col)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(int[] array, Index2d index, int value) {
        array[mapper.offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @param value the new lattice value at the given {@code index}
     */
    public void set(int[] array, int row, int col, int value) {
        array[mapper.offset(row, col)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public long get(long[] array, Index2d index) {
        return array[mapper.offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @return the element at the given {@code index}
     */
    public long get(long[] array, int row, int col) {
        return array[mapper.offset(row, col)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(long[] array, Index2d index, long value) {
        array[mapper.offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @param value the new lattice value at the given {@code index}
     */
    public void set(long[] array, int row, int col, long value) {
        array[mapper.offset(row, col)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public double get(double[] array, Index2d index) {
        return array[mapper.offset(index)];
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @return the element at the given {@code index}
     */
    public double get(double[] array, int row, int col) {
        return array[mapper.offset(row, col)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(double[] array, Index2d index, double value) {
        array[mapper.offset(index)] = value;
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param row the row index
     * @param col the column index
     * @param value the new lattice value at the given {@code index}
     */
    public void set(double[] array, int row, int col, double value) {
        array[mapper.offset(row, col)] = value;
    }

}