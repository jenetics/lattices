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

import static java.util.Objects.requireNonNull;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.linealgebra.array.DenseDoubleArray;
import io.jenetics.linealgebra.array.DoubleArray;
import io.jenetics.linealgebra.structure.DoubleGrid1d;
import io.jenetics.linealgebra.structure.Factory1d;
import io.jenetics.linealgebra.structure.Structure1d;

/**
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
    public DoubleMatrix1d copy(final Structure1d structure) {
        return new DoubleMatrix1d(structure, elements.copy());
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

//    /**
//     * Fills the coordinates and values of cells having non-zero values into the specified lists.
//     * Fills into the lists, starting at index 0.
//     * After this call returns the specified lists all have a new size, the number of non-zero values.
//     * <p>
//     * In general, fill order is <i>unspecified</i>.
//     * This implementation fills like: <tt>for (index = 0..size()-1)  do ... </tt>.
//     * However, subclasses are free to us any other order, even an order that may change over time as cell values are changed.
//     * (Of course, result lists indexes are guaranteed to correspond to the same cell).
//     * <p>
//     * <b>Example:</b>
//     * <br>
//     * <pre>
//     * 0, 0, 8, 0, 7
//     * -->
//     * indexList  = (2,4)
//     * valueList  = (8,7)
//     * </pre>
//     * In other words, <tt>get(2)==8, get(4)==7</tt>.
//     *
//     * @param indexList the list to be filled with indexes, can have any size.
//     * @param valueList the list to be filled with values, can have any size.
//     */
//    public void getNonZeros(IntArrayList indexList, DoubleArrayList valueList, int maxCardinality) {
//        boolean fillIndexList = indexList != null;
//        boolean fillValueList = valueList != null;
//        int card = cardinality(maxCardinality);
//        if (fillIndexList) indexList.setSize(card);
//        if (fillValueList) valueList.setSize(card);
//        if (!(card < maxCardinality)) return;
//
//        if (fillIndexList) indexList.setSize(0);
//        if (fillValueList) valueList.setSize(0);
//        int s = size;
//
//        for (int i = 0; i < s; i++) {
//            double value = getQuick(i);
//            if (value != 0) {
//                if (fillIndexList) indexList.add(i);
//                if (fillValueList) valueList.add(value);
//            }
//        }
//    }

}
