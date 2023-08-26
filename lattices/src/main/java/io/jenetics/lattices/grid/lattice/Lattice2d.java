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
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.function.UnaryOperator;

import io.jenetics.lattices.grid.array.BaseArray;
import io.jenetics.lattices.structure.Structure2d;

/**
 * A lattice is defined via an <em>array</em> and a 2-d structure.
 *
 * @param <A> the array type
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Lattice2d<A extends BaseArray> extends Structure2dOps {

    /**
     * Return the lattice structure.
     *
     * @return the lattice structure
     */
    Structure2d structure();

    /**
     * Return the array storing the lattice elements.
     *
     * @return the array storing the lattice elements
     */
    A array();

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional lattice.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfDouble<A extends BaseArray.OfDouble> extends Lattice2d<A> {

        /**
         * Returns the grid cell value at coordinate {@code [row, col]}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @return the value of the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default double get(int row, int col) {
            return array().get(structure().layout().offset(row, col));
        }

        /**
         * Sets the grid cell at coordinate {@code [row, col]} to the specified
         * {@code value}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @param value  the value to be filled into the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default void set(int row, int col, double value) {
            array().set(structure().layout().offset(row, col), value);
        }

        /**
         * Replaces all cell values of the receiver with the values of another
         * matrix. Both matrices must have the same number of rows and columns.
         *
         * @param source the source lattice to copy from (maybe identical to the
         *        receiver).
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(OfDouble<? extends BaseArray.OfDouble> source) {
            requireNonNull(source);
            if (source == this) {
                return;
            }
            checkSameExtent(extent(), source.extent());
            forEach((r, c) -> set(r, c, source.get(r, c)));
        }

        /**
         * Sets all cells to the state specified by given {@code source}. The
         * {@code values} are required to have the form {@code source[row][column]}
         * and have exactly the same number of rows and columns as the receiver.
         *
         * @implNote
         * The {@code source} are copied and subsequent chances to the {@code source}
         * are not reflected in the matrix, and vice-versa
         *
         * @param source the values to be filled into the cells.
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(double[][] source) {
            if (source.length != rows()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of rows: " +
                        source.length + " != " + rows()
                );
            }

            for (int r = rows(); --r >= 0;) {
                final var row = source[r];

                if (row.length != cols()) {
                    throw new IllegalArgumentException(
                        "Values must have the same number of columns: " +
                            row.length + " != " + cols()
                    );
                }

                for (int c = cols(); --c >= 0;) {
                    set(r, c, row[c]);
                }
            }
        }

        /**
         * Sets all cells to the state specified by the {@code source}.
         *
         * @param source the value to be filled into the cells
         */
        default void assign(double source) {
            forEach((r, c) -> set(r, c, source));
        }

        /**
         * Assigns the result of a function to each cell {@code x[row, col] =
         * f(x[row, col], y[row, col])}.
         *
         * @param y the secondary matrix to operate on.
         * @param f a function object taking as first argument the current cell's
         *          value of {@code this}, and as second argument the current cell's
         *          value of {@code y}
         * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
         */
        default void assign(OfDouble<? extends BaseArray.OfDouble> y, DoubleBinaryOperator f) {
            requireNonNull(f);
            checkSameExtent(extent(), y.extent());

            forEach((r, c) -> set(r, c, f.applyAsDouble(get(r, c), y.get(r, c))));
        }

        /**
         * Assigns the result of a function to each cell
         * {@code x[row, col] = f(x[row, col])}.
         *
         * @param f a function object taking as argument the current cell's value.
         */
        default void assign(DoubleUnaryOperator f) {
            requireNonNull(f);
            forEach((r, c) -> set(r, c, f.applyAsDouble(get(r, c))));
        }

        /**
         * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
         *
         * @throws IllegalArgumentException if {@code extent() != other.extent()}.
         */
        default void swap(OfDouble<? extends BaseArray.OfDouble> other) {
            checkSameExtent(extent(), other.extent());

            forEach((r, c) -> {
                final var tmp = get(r, c);
                set(r, c, other.get(r, c));
                other.set(r, c, tmp);
            });
        }

        /**
         * Applies a function to each cell and aggregates the results. Returns a
         * value <em>v</em> such that <em>v==a(size())</em> where
         * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
         * <em>a(1) == f(get(0,0))</em>.
         * <p><b>Example:</b></p>
         * <pre>
         * 2 x 2 matrix
         * 0 1
         * 2 3
         *
         * // Sum(x[row, col]*x[row, col])
         * matrix.aggregate(Double::sum, a -> a*a) --> 14
         * </pre>
         *
         * @param reducer an aggregation function taking as first argument the
         *        current aggregation and as second argument the transformed current
         *        cell value
         * @param f a function transforming the current cell value
         * @return the aggregated measure or {@link OptionalDouble#empty()} if
         *         {@code size() == 0}
         */
        default OptionalDouble
        reduce(DoubleBinaryOperator reducer, DoubleUnaryOperator f) {
            requireNonNull(reducer);
            requireNonNull(f);

            if (extent().elements() == 0) {
                return OptionalDouble.empty();
            }

            double a = f.applyAsDouble(get(rows() - 1, cols() - 1));
            int d = 1;
            for (int r = rows(); --r >= 0;) {
                for (int c = cols() - d; --c >= 0;) {
                    a = reducer.applyAsDouble(a, f.applyAsDouble(get(r, c)));
                }
                d = 0;
            }
            return OptionalDouble.of(a);
        }

        /**
         * Checks whether the given matrices have the same dimension and contains
         * the same values.
         *
         * @param other the second matrix to compare
         * @return {@code true} if the two given matrices are equal, {@code false}
         *         otherwise
         */
        default boolean equals(OfDouble<? extends BaseArray.OfDouble> other) {
            return extent().equals(other.extent()) &&
                allMatch((r, c) -> Double.compare(get(r, c), other.get(r, c)) == 0);
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional lattice.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfInt<A extends BaseArray.OfInt> extends Lattice2d<A> {

        /**
         * Returns the grid cell value at coordinate {@code [row, col]}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @return the value of the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default int get(int row, int col) {
            return array().get(structure().layout().offset(row, col));
        }

        /**
         * Sets the grid cell at coordinate {@code [row, col]} to the specified
         * {@code value}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @param value  the value to be filled into the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default void set(int row, int col, int value) {
            array().set(structure().layout().offset(row, col), value);
        }

        /**
         * Replaces all cell values of the receiver with the values of another
         * matrix. Both matrices must have the same number of rows and columns.
         *
         * @param source the source lattice to copy from (maybe identical to the
         *        receiver).
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(OfInt<? extends BaseArray.OfInt> source) {
            requireNonNull(source);
            if (source == this) {
                return;
            }
            checkSameExtent(extent(), source.extent());
            forEach((r, c) -> set(r, c, source.get(r, c)));
        }

        /**
         * Sets all cells to the state specified by given {@code source}. The
         * {@code values} are required to have the form {@code source[row][column]}
         * and have exactly the same number of rows and columns as the receiver.
         *
         * @implNote
         * The {@code source} are copied and subsequent chances to the {@code source}
         * are not reflected in the matrix, and vice-versa
         *
         * @param source the values to be filled into the cells.
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(int[][] source) {
            if (source.length != rows()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of rows: " +
                        source.length + " != " + rows()
                );
            }

            for (int r = rows(); --r >= 0;) {
                final var row = source[r];

                if (row.length != cols()) {
                    throw new IllegalArgumentException(
                        "Values must have the same number of columns: " +
                            row.length + " != " + cols()
                    );
                }

                for (int c = cols(); --c >= 0;) {
                    set(r, c, row[c]);
                }
            }
        }

        /**
         * Sets all cells to the state specified by the {@code source}.
         *
         * @param source the value to be filled into the cells
         */
        default void assign(int source) {
            forEach((r, c) -> set(r, c, source));
        }

        /**
         * Assigns the result of a function to each cell {@code x[row, col] =
         * f(x[row, col], y[row, col])}.
         *
         * @param y the secondary matrix to operate on.
         * @param f a function object taking as first argument the current cell's
         *          value of {@code this}, and as second argument the current cell's
         *          value of {@code y}
         * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
         */
        default void assign(OfInt<? extends BaseArray.OfInt> y, IntBinaryOperator f) {
            requireNonNull(f);
            checkSameExtent(extent(), y.extent());

            forEach((r, c) -> set(r, c, f.applyAsInt(get(r, c), y.get(r, c))));
        }

        /**
         * Assigns the result of a function to each cell
         * {@code x[row, col] = f(x[row, col])}.
         *
         * @param f a function object taking as argument the current cell's value.
         */
        default void assign(IntUnaryOperator f) {
            requireNonNull(f);
            forEach((r, c) -> set(r, c, f.applyAsInt(get(r, c))));
        }

        /**
         * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
         *
         * @throws IllegalArgumentException if {@code extent() != other.extent()}.
         */
        default void swap(OfInt<? extends BaseArray.OfInt> other) {
            checkSameExtent(extent(), other.extent());

            forEach((r, c) -> {
                final var tmp = get(r, c);
                set(r, c, other.get(r, c));
                other.set(r, c, tmp);
            });
        }

        /**
         * Applies a function to each cell and aggregates the results. Returns a
         * value <em>v</em> such that <em>v==a(size())</em> where
         * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
         * <em>a(1) == f(get(0,0))</em>.
         * <p><b>Example:</b></p>
         * <pre>
         * 2 x 2 matrix
         * 0 1
         * 2 3
         *
         * // Sum(x[row, col]*x[row, col])
         * matrix.aggregate(Double::sum, a -> a*a) --> 14
         * </pre>
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

            if (extent().elements() == 0) {
                return OptionalInt.empty();
            }

            int a = f.applyAsInt(get(rows() - 1, cols() - 1));
            int d = 1;
            for (int r = rows(); --r >= 0;) {
                for (int c = cols() - d; --c >= 0;) {
                    a = reducer.applyAsInt(a, f.applyAsInt(get(r, c)));
                }
                d = 0;
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
        default boolean equals(OfInt<? extends BaseArray.OfInt> other) {
            return extent().equals(other.extent()) &&
                allMatch((r, c) -> get(r, c) == other.get(r, c));
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional lattice.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfLong<A extends BaseArray.OfLong> extends Lattice2d<A> {

        /**
         * Returns the grid cell value at coordinate {@code [row, col]}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @return the value of the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default long get(int row, int col) {
            return array().get(structure().layout().offset(row, col));
        }

        /**
         * Sets the grid cell at coordinate {@code [row, col]} to the specified
         * {@code value}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @param value  the value to be filled into the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default void set(int row, int col, long value) {
            array().set(structure().layout().offset(row, col), value);
        }

        /**
         * Replaces all cell values of the receiver with the values of another
         * matrix. Both matrices must have the same number of rows and columns.
         *
         * @param source the source lattice to copy from (maybe identical to the
         *        receiver).
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(OfLong<? extends BaseArray.OfLong> source) {
            requireNonNull(source);
            if (source == this) {
                return;
            }
            checkSameExtent(extent(), source.extent());
            forEach((r, c) -> set(r, c, source.get(r, c)));
        }

        /**
         * Sets all cells to the state specified by given {@code source}. The
         * {@code values} are required to have the form {@code source[row][column]}
         * and have exactly the same number of rows and columns as the receiver.
         *
         * @implNote
         * The {@code source} are copied and subsequent chances to the {@code source}
         * are not reflected in the matrix, and vice-versa
         *
         * @param source the values to be filled into the cells.
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(long[][] source) {
            if (source.length != rows()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of rows: " +
                        source.length + " != " + rows()
                );
            }

            for (int r = rows(); --r >= 0;) {
                final var row = source[r];

                if (row.length != cols()) {
                    throw new IllegalArgumentException(
                        "Values must have the same number of columns: " +
                            row.length + " != " + cols()
                    );
                }

                for (int c = cols(); --c >= 0;) {
                    set(r, c, row[c]);
                }
            }
        }

        /**
         * Sets all cells to the state specified by the {@code source}.
         *
         * @param source the value to be filled into the cells
         */
        default void assign(long source) {
            forEach((r, c) -> set(r, c, source));
        }

        /**
         * Assigns the result of a function to each cell {@code x[row, col] =
         * f(x[row, col], y[row, col])}.
         *
         * @param y the secondary matrix to operate on.
         * @param f a function object taking as first argument the current cell's
         *          value of {@code this}, and as second argument the current cell's
         *          value of {@code y}
         * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
         */
        default void assign(OfLong<? extends BaseArray.OfLong> y, LongBinaryOperator f) {
            requireNonNull(f);
            checkSameExtent(extent(), y.extent());

            forEach((r, c) -> set(r, c, f.applyAsLong(get(r, c), y.get(r, c))));
        }

        /**
         * Assigns the result of a function to each cell
         * {@code x[row, col] = f(x[row, col])}.
         *
         * @param f a function object taking as argument the current cell's value.
         */
        default void assign(LongUnaryOperator f) {
            requireNonNull(f);
            forEach((r, c) -> set(r, c, f.applyAsLong(get(r, c))));
        }

        /**
         * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
         *
         * @throws IllegalArgumentException if {@code extent() != other.extent()}.
         */
        default void swap(OfLong<? extends BaseArray.OfLong> other) {
            checkSameExtent(extent(), other.extent());

            forEach((r, c) -> {
                final var tmp = get(r, c);
                set(r, c, other.get(r, c));
                other.set(r, c, tmp);
            });
        }

        /**
         * Applies a function to each cell and aggregates the results. Returns a
         * value <em>v</em> such that <em>v==a(size())</em> where
         * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
         * <em>a(1) == f(get(0,0))</em>.
         * <p><b>Example:</b></p>
         * <pre>
         * 2 x 2 matrix
         * 0 1
         * 2 3
         *
         * // Sum(x[row, col]*x[row, col])
         * matrix.aggregate(Double::sum, a -> a*a) --> 14
         * </pre>
         *
         * @param reducer an aggregation function taking as first argument the
         *        current aggregation and as second argument the transformed current
         *        cell value
         * @param f a function transforming the current cell value
         * @return the aggregated measure or {@link OptionalDouble#empty()} if
         *         {@code size() == 0}
         */
        default OptionalLong reduce(LongBinaryOperator reducer, LongUnaryOperator f) {
            requireNonNull(reducer);
            requireNonNull(f);

            if (extent().elements() == 0) {
                return OptionalLong.empty();
            }

            long a = f.applyAsLong(get(rows() - 1, cols() - 1));
            int d = 1;
            for (int r = rows(); --r >= 0;) {
                for (int c = cols() - d; --c >= 0;) {
                    a = reducer.applyAsLong(a, f.applyAsLong(get(r, c)));
                }
                d = 0;
            }
            return OptionalLong.of(a);
        }

        /**
         * Checks whether the given matrices have the same dimension and contains
         * the same values.
         *
         * @param other the second matrix to compare
         * @return {@code true} if the two given matrices are equal, {@code false}
         *         otherwise
         */
        default boolean equals(OfLong<? extends BaseArray.OfLong> other) {
            return extent().equals(other.extent()) &&
                allMatch((r, c) -> get(r, c) == other.get(r, c));
        }

    }

    /**
     * This interface <em>structures</em> the elements into a 2-dimensional lattice.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfObject<T, A extends BaseArray.OfObject<T>> extends Lattice2d<A> {

        /**
         * Returns the grid cell value at coordinate {@code [row, col]}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @return the value of the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default T get(int row, int col) {
            return array().get(structure().layout().offset(row, col));
        }

        /**
         * Sets the grid cell at coordinate {@code [row, col]} to the specified
         * {@code value}.
         *
         * @param row the index of the row-coordinate
         * @param col the index of the column-coordinate
         * @param value  the value to be filled into the specified cell
         * @throws IndexOutOfBoundsException if the given coordinates are out of
         *         bounds
         */
        default void set(int row, int col, T value) {
            array().set(structure().layout().offset(row, col), value);
        }

        /**
         * Replaces all cell values of the receiver with the values of another
         * matrix. Both matrices must have the same number of rows and columns.
         *
         * @param source the source lattice to copy from (maybe identical to the
         *        receiver).
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(OfObject<? extends T, ?> source) {
            requireNonNull(source);
            if (source == this) {
                return;
            }
            checkSameExtent(extent(), source.extent());
            forEach((r, c) -> set(r, c, source.get(r, c)));
        }

        /**
         * Sets all cells to the state specified by given {@code source}. The
         * {@code values} are required to have the form {@code source[row][column]}
         * and have exactly the same number of rows and columns as the receiver.
         *
         * @implNote
         * The {@code source} are copied and subsequent chances to the {@code source}
         * are not reflected in the matrix, and vice-versa
         *
         * @param source the values to be filled into the cells.
         * @throws IllegalArgumentException if {@code !extent().equals(source.extent())}
         */
        default void assign(T[][] source) {
            if (source.length != rows()) {
                throw new IllegalArgumentException(
                    "Values must have the same number of rows: " +
                        source.length + " != " + rows()
                );
            }

            for (int r = rows(); --r >= 0;) {
                final var row = source[r];

                if (row.length != cols()) {
                    throw new IllegalArgumentException(
                        "Values must have the same number of columns: " +
                            row.length + " != " + cols()
                    );
                }

                for (int c = cols(); --c >= 0;) {
                    set(r, c, row[c]);
                }
            }
        }

        /**
         * Sets all cells to the state specified by the {@code source}.
         *
         * @param source the value to be filled into the cells
         */
        default void assign(T source) {
            forEach((r, c) -> set(r, c, source));
        }

        /**
         * Assigns the result of a function to each cell {@code x[row, col] =
         * f(x[row, col], y[row, col])}.
         *
         * @param y the secondary matrix to operate on.
         * @param f a function object taking as first argument the current cell's
         *          value of {@code this}, and as second argument the current cell's
         *          value of {@code y}
         * @throws IllegalArgumentException if {@code !extent().equals(y.extent())}
         */
        default void assign(OfObject<? extends T, ?> y, BinaryOperator<T> f) {
            requireNonNull(f);
            checkSameExtent(extent(), y.extent());

            forEach((r, c) -> set(r, c, f.apply(get(r, c), y.get(r, c))));
        }

        /**
         * Updates this grid with the values of {@code a} which are transformed by
         * the given function {@code f}.
         * <pre>{@code
         * this[i, j] = f(a[i, j])
         * }</pre>
         * <pre>{@code
         * final ObjectGrid2d<Integer> ints = ObjectGrid2d
         *     .<Integer>dense()
         *     .create(15, 40);
         *
         * final ObjectGrid2d<String> strings = ObjectGrid2d
         *     .<String>dense()
         *     .create(15, 40);
         *
         * ints.forEach((r, c) -> ints.set(r, c, r*c));
         * strings.assign(ints, Object::toString);
         * }</pre>
         *
         * @param a the grid used for the update
         * @param f the mapping function
         * @throws IllegalArgumentException if {@code extent() != other.extent()}
         */
        default  <A> void assign(
            OfObject<? extends A, ?> a,
            Function<? super A, ? extends T> f
        ) {
            checkSameExtent(extent(), a.extent());
            forEach((r, c) -> set(r, c, f.apply(a.get(r, c))));
        }

        /**
         * Assigns the result of a function to each cell
         * {@code x[row, col] = f(x[row, col])}.
         *
         * @param f a function object taking as argument the current cell's value.
         */
        default void assign(UnaryOperator<T> f) {
            requireNonNull(f);
            forEach((r, c) -> set(r, c, f.apply(get(r, c))));
        }

        /**
         * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
         *
         * @throws IllegalArgumentException if {@code extent() != other.extent()}.
         */
        default void swap(OfObject<T, ?> other) {
            checkSameExtent(extent(), other.extent());

            forEach((r, c) -> {
                final var tmp = get(r, c);
                set(r, c, other.get(r, c));
                other.set(r, c, tmp);
            });
        }

        /**
         * Applies a function to each cell and aggregates the results. Returns a
         * value <em>v</em> such that <em>v==a(size())</em> where
         * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
         * <em>a(1) == f(get(0,0))</em>.
         * <p><b>Example:</b></p>
         * <pre>
         * 2 x 2 matrix
         * 0 1
         * 2 3
         *
         * // Sum(x[row, col]*x[row, col])
         * matrix.aggregate(Double::sum, a -> a*a) --> 14
         * </pre>
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

            T a = f.apply(get(rows() - 1, cols() - 1));
            int d = 1;
            for (int r = rows(); --r >= 0;) {
                for (int c = cols() - d; --c >= 0;) {
                    a = reducer.apply(a, f.apply(get(r, c)));
                }
                d = 0;
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
        default boolean equals(OfObject<?, ?> other) {
            return extent().equals(other.extent()) &&
                allMatch((r, c) -> Objects.equals(get(r, c), other.get(r, c)));
        }

    }
}
