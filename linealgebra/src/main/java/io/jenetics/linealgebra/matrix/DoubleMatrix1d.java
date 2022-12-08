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
package io.jenetics.linealgebra.matrix;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;

import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.array.DoubleArray;
import io.jenetics.linealgebra.structure.DoubleGrid1d;
import io.jenetics.linealgebra.structure.Factory1d;
import io.jenetics.linealgebra.structure.Range1d;
import io.jenetics.linealgebra.structure.Structure1d;

/**
 * Generic class for 1-d matrices (aka <em>vectors</em>) holding {@code double}
 * elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public class DoubleMatrix1d
    extends DoubleGrid1d
    implements Matrix1d<DoubleMatrix1d>
{

    /**
     * Factory for creating dense 1-d double matrices.
     */
    public static final Factory1d<DoubleMatrix1d> DENSE_FACTORY = struct ->
        new DoubleMatrix1d(
            struct,
            DenseDoubleArray.ofSize(struct.extent().size())
        );

    public DoubleMatrix1d(final Structure1d structure, final DoubleArray elements) {
        super(structure, elements);
    }

    @Override
    public Factory1d<DoubleMatrix1d> factory() {
        return struct -> new DoubleMatrix1d(
            struct,
            elements.newArrayOfSize(struct.extent().size())
        );
    }

    @Override
    public DoubleMatrix1d view(final Structure1d structure) {
        return new DoubleMatrix1d(structure, elements);
    }

    @Override
    public DoubleMatrix1d copy(final Range1d range) {
        final var elems = elements.newArrayOfSize(range.size());
        final var struct = structure.copy(range);

        for (int i = 0; i < range.size(); ++i) {
            elems.set(i, get(i + range.index()));
        }

        return new DoubleMatrix1d(struct, elems);
    }

    /* *************************************************************************
     * Additional matrix methods.
     * ************************************************************************/

    /**
     * Returns the dot product of two vectors x and y, which is
     * {@code Sum(x[i]*y[i])}, where {@code x == this}.
     * Operates on cells at indexes {@code from ..
     * Min(size(), y.size(), from + length) - 1}.
     *
     * @param y the second vector
     * @param from the first index to be considered
     * @param length the number of cells to be considered
     * @return the sum of products, start if {@code from<0 || length<0}
     */
    public double dotProduct(
        final DoubleMatrix1d y,
        final int from,
        final int length
    ) {
        if (from < 0 || length <= 0) {
            return 0;
        }

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
        for (int k = l; --k >= 0; i--) {
            sum += get(i) * y.get(i);
        }
        return sum;
    }

    /**
     * Returns the dot product of two vectors x and y, which is {
     * @code Sum(x[i]*y[i])}, where {@code x == this}.
     * Operates on cells at indexes {@code 0 .. Math.min(size(), y.size())}.
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
     * @return the sum
     */
    public double sum() {
        if (size() == 0) {
            return 0;
        } else {
            return reduce(Double::sum, DoubleUnaryOperator.identity());
        }
    }

    /**
     * Returns the number of cells having non-zero values, ignores tolerance.
     */
    public int cardinality() {
        int cardinality = 0;
        for (int i = size(); --i >= 0; ) {
            if (Double.compare(get(i), 0) != 0) {
                cardinality++;
            }
        }
        return cardinality;
    }

    /**
     * Returns the number of cells having non-zero values, but at most
     * {@code maxCardinality}, ignores tolerance.
     */
    public int cardinality(int maxCardinality) {
        int cardinality = 0;
        int i = size();
        while (--i >= 0 && cardinality < maxCardinality) {
            if (Double.compare(get(i), 0) != 0) {
                cardinality++;
            }
        }
        return cardinality;
    }

    public int[] nonZeroIndices() {
        final var indices = IntStream.builder();

        for (int i = 0; i < size(); ++i) {
            final var value = get(i);
            if (Double.compare(value, 0.0) != 0) {
                indices.add(i);
            }
        }

        return indices.build().toArray();
    }

}
