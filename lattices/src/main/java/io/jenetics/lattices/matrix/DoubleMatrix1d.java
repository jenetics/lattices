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

import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

import io.jenetics.lattices.NumericalContext;
import io.jenetics.lattices.array.DenseDoubleArray;
import io.jenetics.lattices.array.DoubleArray;
import io.jenetics.lattices.grid.DoubleGrid1d;
import io.jenetics.lattices.grid.Factory1d;
import io.jenetics.lattices.grid.Range1d;
import io.jenetics.lattices.grid.StrideOrder1d;
import io.jenetics.lattices.grid.Structure1d;

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
public class DoubleMatrix1d
    extends DoubleGrid1d
    implements Matrix1d<DoubleMatrix1d>
{

    /**
     * Factory for creating dense 1-d double matrices.
     */
    public static final Factory1d<DoubleMatrix1d> DENSE = struct ->
        new DoubleMatrix1d(
            struct,
            DenseDoubleArray.ofSize(struct.extent().size())
        );

    /**
     * Create a new 1-d matrix with the given {@code structure} and element
     * {@code array}.
     *
     * @param structure the matrix structure
     * @param array the element array
     */
    public DoubleMatrix1d(final Structure1d structure, final DoubleArray array) {
        super(structure, array);
    }

    /**
     * Create a new matrix <em>view</em> from the given {@code grid}.
     *
     * @param grid the data grid
     */
    public DoubleMatrix1d(final DoubleGrid1d grid) {
        this(grid.structure(), grid.array());
    }

    @Override
    public Factory1d<DoubleMatrix1d> factory() {
        return struct -> new DoubleMatrix1d(
            struct,
            array.like(struct.extent().size())
        );
    }

    @Override
    public DoubleMatrix1d view(final Structure1d structure) {
        return new DoubleMatrix1d(structure, array);
    }

    @Override
    public DoubleMatrix1d copy(final Range1d range) {
        final var struct = structure.copy(range);

        // Check if we can do a fast copy.
        if (structure.order() instanceof StrideOrder1d so) {
            return new DoubleMatrix1d(
                struct,
                array.copy(range.start() + so.start(), range.size())
            );
        } else {
            final var elems = array.like(range.size());
            for (int i = 0; i < range.size(); ++i) {
                elems.set(i, get(i + range.start()));
            }
            return new DoubleMatrix1d(struct, elems);
        }
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
    public double dotProduct(
        final DoubleMatrix1d y,
        final int from,
        final int length
    ) {
        if (from < 0 || length <= 0) {
            return 0;
        }

        final int to = Math.min(Math.min(size(), y.size()), from + length);

        // Fast track dot-product.
        /*
        if (order() instanceof StrideOrder1d so1 &&
            y.order() instanceof StrideOrder1d so2 &&
            so1.stride() == 1 &&
            so2.stride() == 1 &&
            array instanceof DenseDoubleArray dda1 &&
            y.array instanceof DenseDoubleArray dda2)
        {

        } else {

        }
         */

        double sum = 0;
        for (int i = from; i < to; ++i) {
            sum = Math.fma(get(i), y.get(i), sum);
        }
        return sum;

        /*
        int tail = from + length;
        if (size() < tail) {
            tail = size();
        }
        if (y.size() < tail) {
            tail = y.size();
        }
        int l = tail - from;

        double sum = 0;
        int i = tail - 1;
        for (int k = l; --k >= 0; --i) {
            sum = Math.fma(get(i), y.get(i), sum);
        }
        return sum;
         */
    }

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}, where {@code x == this}. Operates on cells at
     * indexes {@code 0 .. Math.min(size(), y.size())}.
     *
     * @param y the second vector
     * @return the sum of products
     */
    public double dotProduct(final DoubleMatrix1d y) {
        return dotProduct(y, 0, size());
    }

    /**
     * Returns the sum of all cells {@code Sum(x[i])}.
     *
     * @return the sum of the vector elements
     */
    public double sum() {
        return size() == 0
            ? 0
            : reduce(Double::sum, DoubleUnaryOperator.identity());
    }

    /**
     * Return the number of cells having non-zero values.
     *
     * @return the number of cells having non-zero values
     */
    public int cardinality() {
        final var context = NumericalContext.get();

        int cardinality = 0;
        for (int i = 0; i < size(); ++i) {
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
        for (int i = 0; i < size(); ++i) {
            if (context.isNotZero(get(i))) {
                indices.add(i);
            }
        }

        return indices.build().toArray();
    }

    @Override
    public boolean equals(final Object object) {
        return object == this ||
            object instanceof DoubleMatrix1d matrix &&
            equals(matrix);
    }

}
