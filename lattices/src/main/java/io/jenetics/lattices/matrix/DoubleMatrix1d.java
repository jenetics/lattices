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
package io.jenetics.lattices.matrix;

import static java.lang.Math.min;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

import io.jenetics.lattices.grid.Grid1d;
import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.grid.lattice.Lattice1d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Structure1d;

/**
 * Generic class for 1-d matrices (aka <em>vectors</em>) holding {@code double}
 * elements. Instances of this class are usually created via a factory.
 * <pre>{@code
 * final DoubleMatrix1d matrix10 = DoubleMatrix1d.DENSE.create(10);
 * }</pre>
 *
 * @see #DENSE
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DoubleMatrix1d(Structure1d structure, Array.OfDouble array)
    implements Lattice1d.OfDouble<Array.OfDouble>, Grid1d<Array.OfDouble, DoubleMatrix1d>
{

    /**
     * Factory for creating dense 1-d double matrices.
     */
    public static final Grid1d.Factory<DoubleMatrix1d> DENSE =
        extent -> new DoubleMatrix1d(
            new Structure1d(extent),
            DenseDoubleArray.ofSize(extent.cells())
        );

    /**
     * Create a new matrix view from the given lattice.
     *
     * @param lattice the underlying lattice data
     */
    public DoubleMatrix1d(Lattice1d<? extends Array.OfDouble> lattice) {
        this(lattice.structure(), lattice.array());
    }

    @Override
    public DoubleMatrix1d create(Structure1d structure, Array.OfDouble array) {
        return new DoubleMatrix1d(structure, array);
    }

    /* *************************************************************************
     * Additional matrix methods.
     * ************************************************************************/

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}, where {@code x == this}. Operates on cells at
     * indexes {@code from .. Min(size(), y.size(), from + length) - 1}.
     *
     * @param y the second vector
     * @param from the first index to be considered
     * @param length the number of cells to be considered
     * @return the sum of products, start if {@code from < 0 || length < 0}
     */
    public double dotProduct(DoubleMatrix1d y, int from, int length) {
        if (from < 0 || length <= 0) {
            return 0;
        }

        final int to = min(min(extent().elements(), y.extent().elements()), from + length);

        double sum = 0;
        for (int i = from; i < to; ++i) {
            sum = Math.fma(get(i), y.get(i), sum);
        }
        return sum;
    }

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}, where {@code x == this}. Operates on cells at
     * indexes {@code 0 .. Math.min(size(), y.size())}.
     *
     * @param y the second vector
     * @return the sum of products
     */
    public double dotProduct(DoubleMatrix1d y) {
        return dotProduct(y, 0, extent().elements());
    }

    /**
     * Returns the sum of all cells {@code Sum(x[i])}.
     *
     * @return the sum of the vector elements
     */
    public double sum() {
        return reduce(Double::sum, DoubleUnaryOperator.identity())
            .orElse(0);
    }

    /**
     * Return the number of cells having non-zero values.
     *
     * @return the number of cells having non-zero values
     */
    public int cardinality() {
        final var context = NumericalContext.get();

        int cardinality = 0;
        for (int i = 0; i < extent().elements(); ++i) {
            if (context.isZero(get(i))) {
                ++cardinality;
            }
        }
        return cardinality;
    }

    /**
     * Return the indices of non-zero values.
     *
     * @return the indices of non-zero values
     */
    public int[] nonZeroIndices() {
        final var context = NumericalContext.get();

        final var indices = IntStream.builder();
        for (int i = 0; i < extent().elements(); ++i) {
            if (context.isNotZero(get(i))) {
                indices.add(i);
            }
        }

        return indices.build().toArray();
    }

    /**
     * Checks whether the given matrices have the same dimension and contains
     * the same values.
     *
     * @param other the second matrix to compare
     * @return {@code true} if the two given matrices are equal, {@code false}
     *         otherwise
     */
    public boolean equals(DoubleMatrix1d other) {
        final var context = NumericalContext.get();

        return extent().equals(other.extent()) &&
            allMatch(i -> context.equals(get(i), other.get(i)));
    }

    @Override
    public boolean equals(Object object) {
        return object == this ||
            object instanceof DoubleMatrix1d matrix &&
            equals(matrix);
    }

    /**
     * Return a 1-d matrix view of the given input {@code values}.
     *
     * @implSpec
     * The given input data is <b>not</b> copied, the returned object is a
     * <em>view</em> onto the given input data.
     *
     * @param values the returned matrix
     * @return a matrix view of the given input data
     */
    public static DoubleMatrix1d of(double... values) {
        return new DoubleMatrix1d(
            new Structure1d(new Extent1d(values.length)),
            new DenseDoubleArray(values)
        );
    }

}
