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
import static io.jenetics.lattices.grid.Structures.checkSameExtent;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import io.jenetics.lattices.grid.array.Array;
import io.jenetics.lattices.structure.Extent1d;

/**
 * This interface <em>structures</em> the elements into a 1-dimensional lattice.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface ObjectLattice1d<T>
    extends Lattice1d<Array<T>>, Structure1dOps
{

    /**
     * Returns the matrix cell value at coordinate {@code index}.
     *
     * @param index the index of the cell
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    default T get(int index) {
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
    default void set(int index, T value) {
        array().set(structure().offset(index), value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param source the source lattice to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
     */
    default void assign(ObjectLattice1d<? extends T> source) {
        requireNonNull(source);
        if (source == this) {
            return;
        }
        checkSameExtent(extent(), source.extent());
        forEach(i -> set(i, source.get(i)));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param values the values to be filled into the cells
     */
    default void assign(T[] values) {
        checkSameExtent(extent(), new Extent1d(values.length));
        forEach(i -> set(i, values[i]));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    default void assign(T value) {
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
    default void assign(UnaryOperator<T> f) {
        requireNonNull(f);
        forEach(i -> set(i, f.apply(get(i))));
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
    default void assign(ObjectLattice1d<T> a, BinaryOperator<T> f) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.apply(get(i), a.get(i))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i] = f(a[i])
     * }</pre>
     * <pre>{@code
     * final ObjectGrid1d<Integer> ints = ObjectGrid1d
     *     .<Integer>dense()
     *     .create(40);
     *
     * final ObjectGrid1d<String> strings = ObjectGrid1d
     *     .<String>dense()
     *     .create(40);
     *
     * ints.forEach((i) -> ints.set(i, i));
     * strings.assign(ints, Object::toString);
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the mapping function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    default  <A> void assign(
        ObjectLattice1d<? extends A> a,
        Function<? super A, ? extends T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.apply(a.get(i))));
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code size() != other.size()}.
     */
    default void swap(final ObjectLattice1d<T> other) {
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
    default Optional<T> reduce(BinaryOperator<T> reducer, UnaryOperator<T> f) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (extent().elements() == 0) {
            return Optional.empty();
        }

        T a = f.apply(get(extent().elements() - 1));
        for (int i = extent().elements() - 1; --i >= 0;) {
            a = reducer.apply(a, f.apply(get(i)));
        }

        return Optional.ofNullable(a);
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    default boolean equals(ObjectLattice1d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch(i -> Objects.equals(get(i), other.get(i)));
    }

}
