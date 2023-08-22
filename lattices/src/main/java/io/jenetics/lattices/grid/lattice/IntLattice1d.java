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
package io.jenetics.lattices.grid.lattice;

import static java.util.Objects.requireNonNull;
import static io.jenetics.lattices.structure.Structures.checkSameExtent;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import io.jenetics.lattices.grid.array.IntArray;
import io.jenetics.lattices.structure.Extent1d;

/**
 * This interface <em>structures</em> the elements into a 1-dimensional lattice.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface IntLattice1d extends Lattice1d<IntArray>, Structure1dOps {

    /**
     * Returns the matrix cell value at coordinate {@code index}.
     *
     * @param index the index of the cell
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    default int get(int index) {
        return array().get(structure().offset(index));
    }

    /**
     * Sets the matrix cell at coordinate {@code index} to the specified
     * {@code value}.
     *
     * @param index the index of the cell
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    default void set(int index, int value) {
        array().set(structure().offset(index), value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    default void assign(IntLattice1d other) {
        if (other == this) {
            return;
        }
        checkSameExtent(extent(), other.extent());

        forEach(i -> set(i, other.get(i)));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param values the values to be filled into the cells
     */
    default void assign(int[] values) {
        checkSameExtent(extent(), new Extent1d(values.length));
        forEach(i -> set(i, values[i]));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    default void assign(int value) {
        forEach(i -> set(i, value));
    }

    /**
     * Assigns the result of a function to each cell.
     * <pre>{@code
     * this[i] = f(this[i])
     * }</pre>
     *
     * @param f a function object taking as argument the current cell's value.
     */
    default void assign(IntUnaryOperator f) {
        requireNonNull(f);
        forEach(i -> set(i, f.applyAsInt(get(i))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i] = f(this[i], a[i])
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the combiner function
     */
    default void assign(IntLattice1d a, IntBinaryOperator f) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.applyAsInt(get(i), a.get(i))));
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code size() != other.size()}.
     */
    default void swap(final IntLattice1d other) {
        checkSameExtent(extent(), other.extent());
        forEach(i -> {
            final var tmp = get(i);
            set(i, other.get(i));
            other.set(i, tmp);
        });
    }

    /**
     * Applies a function to each cell and aggregates the results.
     * Returns a value {@code v} such that {@code v == a(size())} where
     * {@code a(i) == reducer( a(i - 1), f(get(i)) )} and terminators are
     * {@code a(1) == f(get(0))}.
     *
     * @param reducer an aggregation function taking as first argument the
     *        current aggregation and as second argument the transformed current
     *        cell value
     * @param f a function transforming the current cell value
     * @return the aggregated measure or {@link OptionalDouble#empty()} if
     *         {@code size() == 0}
     */
    default OptionalInt reduce(IntBinaryOperator reducer, IntUnaryOperator f) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (extent().nelements() == 0) {
            return OptionalInt.empty();
        }

        int a = f.applyAsInt(get(extent().nelements() - 1));
        for (int i = extent().nelements() - 1; --i >= 0;) {
            a = reducer.applyAsInt(a, f.applyAsInt(get(i)));
        }

        return OptionalInt.of(a);
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    default boolean equals(IntLattice1d other) {
        return extent().equals(other.extent()) &&
            allMatch(i -> get(i) == other.get(i));
    }

}

