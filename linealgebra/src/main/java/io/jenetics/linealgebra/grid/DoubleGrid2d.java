/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.linealgebra.grid;

import static java.util.Objects.requireNonNull;
import static io.jenetics.linealgebra.NumericalContext.ZERO_EPSILON;
import static io.jenetics.linealgebra.grid.Grids.checkSameExtent;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.NumericalContext;
import io.jenetics.linealgebra.array.DoubleArray;

/**
 * Generic class for 2-d grids holding {@code double} elements. The
 * {@code DoubleGrid2d} is <em>just</em> a 2-d view onto a 1-d Java
 * {@code double[]} array. The following example shows how to create such a grid
 * view from a given {@code double[]} array.
 *
 * <pre>{@code
 * final var values = new double[50*100];
 * final var grid = new DoubleGrid2d(
 *     new Structure2d(new Extent2d(50, 100)),
 *     new DenseDoubleArray(values)
 * );
 * }</pre>
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleGrid2d implements Grid2d {

    /**
     * The structure, which defines the <em>extent</em> of the grid and the
     * <em>order</em> which determines the index mapping {@code N^2 -> N}.
     */
    protected final Structure2d structure;

    /**
     * The underlying {@code double[]} array.
     */
    protected final DoubleArray array;

    /**
     * Create a new 2-d matrix with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     * @throws IllegalArgumentException if the size of the given {@code array}
     *         is not able to hold the required number of elements. It is still
     *         possible that an {@link IndexOutOfBoundsException} is thrown when
     *         the defined order of the grid tries to access an array index,
     *         which is not within the bounds of the {@code array}.
     * @throws NullPointerException if one of the arguments is {@code null}
     */
    public DoubleGrid2d(final Structure2d structure, final DoubleArray array) {
        if (structure.extent().size() > array.length()) {
            throw new IllegalArgumentException(
                "The number of available elements is smaller than the number of " +
                    "required grid cells: %s > %s."
                        .formatted(structure.extent(), array.length())
            );
        }

        this.structure = structure;
        this.array = array;
    }

    @Override
    public Structure2d structure() {
        return structure;
    }

    /**
     * Return the underlying element array.
     *
     * @return the underlying element array
     */
    public DoubleArray array() {
        return array;
    }

    /**
     * Returns the matrix cell value at coordinate {@code [row, col]}.
     *
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @return the value of the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public double get(final int row, final int col) {
        return array.get(order().index(row, col));
    }

    /**
     * Sets the matrix cell at coordinate {@code [row, col]} to the specified
     * {@code value}.
     *
     * @param row the index of the row-coordinate
     * @param col the index of the column-coordinate
     * @param value  the value to be filled into the specified cell
     * @throws IndexOutOfBoundsException if the given coordinates are out of
     *         bounds
     */
    public void set(final int row, final int col, final double value) {
        array.set(order().index(row, col),  value);
    }

    /**
     * Replaces all cell values of the receiver with the values of another
     * matrix. Both matrices must have the same number of rows and columns.
     *
     * @param other the source matrix to copy from (maybe identical to the
     *        receiver).
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    public void assign(final DoubleGrid2d other) {
        if (other == this) {
            return;
        }
        checkSameExtent(this, other);

        for (int r = rows(); --r >= 0;) {
            for (int c = cols(); --c >= 0;) {
                set(r, c, other.get(r, c));
            }
        }
    }

    /**
     * Sets all cells to the state specified by given {@code values}. The
     * {@code values} are required to have the form {@code values[row][column]}
     * and have exactly the same number of rows and columns as the receiver.
     *
     * @implNote
     * The {@code values} are copied and subsequent chances to the {@code values}
     * are not reflected in the matrix, and vice-versa
     *
     * @param values the values to be filled into the cells.
     * @throws IllegalArgumentException if {@code !extent().equals(other.extent())}
     */
    public void assign(final double[][] values) {
        if (values.length != rows()) {
            throw new IllegalArgumentException(
                "Values must have the same number of rows: " +
                    values.length + " != " + rows()
            );
        }

        for (int r = rows(); --r >= 0;) {
            final double[] row = values[r];

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
     * Sets all cells to the state specified by {@code values}.
     *
     * @param value the value to be filled into the cells
     */
    public void assign(final double value) {
        for (int r = rows(); --r >= 0;) {
            for (int c = cols(); --c >= 0;) {
                set(r, c, value);
            }
        }
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
    public void assign(
        final DoubleGrid2d y,
        final DoubleBinaryOperator f
    ) {
        requireNonNull(f);
        checkSameExtent(this, y);

        for (int r = rows(); --r >= 0;) {
            for (int c = cols(); --c >= 0;) {
                set(r, c, f.applyAsDouble(get(r, c), y.get(r, c)));
            }
        }
    }

    /**
     * Assigns the result of a function to each cell
     * {@code x[row, col] = f(x[row, col])}.
     *
     * @param f a function object taking as argument the current cell's value.
     */
    public void assign(final DoubleUnaryOperator f) {
        requireNonNull(f);

        for (int r = rows(); --r >= 0;) {
            for (int c = cols(); --c >= 0;) {
                set(r, c, f.applyAsDouble(get(r, c)));
            }
        }
    }

    /**
     * Swaps each element {@code this[i, j]} with {@code other[i, j]}.
     *
     * @throws IllegalArgumentException if {@code extent() != other.extent()}.
     */
    public void swap(final DoubleGrid2d other) {
        checkSameExtent(this, other);

        for (int r = rows(); --r >= 0;) {
            for (int c = cols(); --c >= 0;) {
                final var tmp = get(r, c);
                set(r, c, other.get(r, c));
                other.set(r, c, tmp);
            }
        };
    }

    /**
     * Applies a function to each cell and aggregates the results. Returns a
     * value <em>v</em> such that <em>v==a(size())</em> where
     * <em>a(i) == reduce(a(i - 1), f(get(row, col)))</em> and terminators are
     * <em>a(1) == f(get(0,0)), a(0) == Double.NaN</em>.
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
     * @return the aggregated value
     */
    public double reduce(
        final DoubleBinaryOperator reducer,
        final DoubleUnaryOperator f
    ) {
        requireNonNull(reducer);
        requireNonNull(f);

        if (size() == 0) {
            return Double.NaN;
        }

        double a = f.applyAsDouble(get(rows() - 1, cols() - 1));
        int d = 1;
        for (int r = rows(); --r >= 0;) {
            for (int c = cols() - d; --c >= 0;) {
                a = reducer.applyAsDouble(a, f.applyAsDouble(get(r, c)));
            }
            d = 0;
        }
        return a;
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(final DoubleGrid2d other) {
        final var context = NumericalContext.get();

        return extent().equals(other.extent()) &&
            allMatch((r, c) -> context.equals(get(r, c), other.get(r, c)));
    }

    @Override
    public int hashCode() {
        final int[] hash = new int[] { 37 };
        forEach((i, j) -> hash[0] += Double.hashCode(get(i, j))*17);
        return hash[0];
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof DoubleGrid2d grid &&
            NumericalContext.with(ZERO_EPSILON, () -> equals(grid));
    }

    @Override
    public String toString() {
        final var out = new StringBuilder();

        out.append("[");
        for (int i = 0; i < rows(); ++i) {
            if (i != 0) {
                out.append(" ");
            }
            out.append("[");
            for (int j = 0; j < cols(); ++j) {
                out.append(get(i, j));
                if (j < cols() - 1) {
                    out.append(", ");
                }
            }
            out.append("]");
            if (i < rows() - 1) {
                out.append("\n");
            }
        }

        out.append("]");

        return out.toString();
    }

}
