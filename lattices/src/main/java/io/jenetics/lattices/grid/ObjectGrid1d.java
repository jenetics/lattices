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
import static io.jenetics.lattices.grid.Grids.checkArraySize;
import static io.jenetics.lattices.grid.Grids.checkSameExtent;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import io.jenetics.lattices.array.DenseObjectArray;
import io.jenetics.lattices.array.ObjectArray;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Object 1-d grid implementation.
 *
 * @param <T> the grid element type
 * @param structure The structure, which defines the <em>extent</em> of the grid
 *        and the <em>order</em> which determines the index mapping {@code N -> N}.
 * @param array The underlying {@code double[]} array.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public record ObjectGrid1d<T>(Structure1d structure, ObjectArray<T> array)
    implements Grid1d<ObjectArray<T>, ObjectGrid1d<T>>
{

    /**
     * Create a new 1-d grid with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @throws IllegalArgumentException if the size of the given {@code array}
     * is not able to hold the required number of elements. It is still possible
     * that an {@link IndexOutOfBoundsException} is thrown when the defined
     * order of the grid tries to access an array index, which is not within the
     * bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    public ObjectGrid1d {
        checkArraySize(structure, array.length());
    }

    @Override
    public ObjectGrid1d<T> create(
        final Structure1d structure,
        final ObjectArray<T> array
    ) {
        return new ObjectGrid1d<>(structure, array);
    }

    /**
     * Returns the matrix cell value at coordinate {@code index}.
     *
     * @param index the index of the cell
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     * bounds
     */
    public T get(final int index) {
        return array.get(layout().offset(index));
    }

    /**
     * Sets the matrix cell at coordinate {@code index} to the specified
     * {@code value}.
     *
     * @param index the index of the cell
     * @param value the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     * bounds
     */
    public void set(final int index, final T value) {
        array.set(layout().offset(index), value);
    }

    /**
     * Return an iterator of the grid elements.
     *
     * @return a new grid element iterator
     */
    public Iterator<T> iterator() {
        return new ObjectGrid1dIterator<>(this);
    }

    /**
     * Return a new stream of the grid elements.
     *
     * @return a new grid element stream
     */
    public Stream<T> stream() {
        return StreamSupport.stream(
            ((Iterable<T>) this::iterator).spliterator(),
            false
        );
    }

    @Override
    public void assign(final ObjectGrid1d<T> other) {
        checkSameExtent(extent(), other.extent());
        forEach(i -> set(i, other.get(i)));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param values the values to be filled into the cells
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public final void assign(final T... values) {
        checkSameExtent(extent(), new Extent1d(values.length));
        forEach(i -> set(i, values[i]));
    }

    /**
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final T value) {
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
    public void assign(final UnaryOperator<T> f) {
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
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void assign(
        final ObjectGrid1d<? extends T> a,
        final BinaryOperator<T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.apply(get(i), a.get(i))));
    }

    /**
     * Updates this grid with the values of {@code a} which are transformed by
     * the given function {@code f}.
     * <pre>{@code
     * this[i] = f(a[i])
     * }</pre>
     * This can be used to assign a grid of strings from a grid of integers.
     * <pre>{@code
     * final ObjectGrid1d<Integer> ints = ObjectGrid1d.<Integer>dense().create(15);
     * final ObjectGrid2d<String> strings = ObjectGrid2d.<String>dense().create(15);
     *
     * ints.forEach(i -> ints.set(i, i));
     * strings.assign(ints, Object::toString);
     * }</pre>
     *
     * @param a the grid used for the update
     * @param f the mapping function
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public <A> void assign(
        final ObjectGrid1d<? extends A> a,
        final Function<? super A, ? extends T> f
    ) {
        checkSameExtent(extent(), a.extent());
        forEach(i -> set(i, f.apply(a.get(i))));
    }

    /**
     * Swaps each element {@code this[i]} with {@code other[i]}.
     *
     * @throws IllegalArgumentException if {@code extent() != other.extent()}
     */
    public void swap(final ObjectGrid1d<T> other) {
        checkSameExtent(extent(), other.extent());

        forEach(i -> {
            final var tmp = get(i);
            set(i, other.get(i));
            other.set(i, tmp);
        });
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     * otherwise
     */
    public boolean equals(final ObjectGrid1d<?> other) {
        return extent().equals(other.extent()) &&
            allMatch(i -> Objects.equals(get(i), other.get(i)));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[]{37};
        forEach(i -> hash[0] += Objects.hashCode(get(i)) * 17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof ObjectGrid1d<?> grid &&
                equals(grid);
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();
        out.append("[");
        for (int i = 0; i < size(); ++i) {
            out.append(get(i));
            if (i < size() - 1) {
                out.append(", ");
            }
        }
        out.append("]");
        return out.toString();
    }

    /**
     * Return a factory for creating dense 1-d object grids.
     *
     * @param __ not used (Java trick for getting "reified" element type)
     * @param <T> the grid element type
     * @return the dense object factory
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Factory1d<ObjectGrid1d<T>> dense(final T... __) {
        return structure -> new ObjectGrid1d<T>(
            structure,
            DenseObjectArray.ofSize(structure.extent().size(), __)
        );
    }

}
