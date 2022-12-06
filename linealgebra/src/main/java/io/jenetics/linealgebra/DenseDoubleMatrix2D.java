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
package io.jenetics.linealgebra;

import io.jenetics.linealgebra.array.DenseDoubleArray;

import static java.util.Objects.requireNonNull;

/**
 * Dense 2-d matrix holding holding {@code double} elements.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public record DenseDoubleMatrix2D(Structure structure, DenseDoubleArray elements)
    implements DoubleMatrix2D
{

    public DenseDoubleMatrix2D{
        requireNonNull(structure);
        requireNonNull(elements);
    }

    public DenseDoubleMatrix2D(final Dimension dimension) {
        this(
            new Structure(dimension, RowMajor.of(dimension)),
            DenseDoubleArray.ofSize(dimension.size())
        );
    }

    public DenseDoubleMatrix2D(final int rows, final int cols) {
        this(new Dimension(rows, cols));
    }

    public DenseDoubleMatrix2D(final double[][] values) {
        this(values.length, values.length == 0 ? 0 : values[0].length);
        assign(values);
    }

    @Override
    public double get(final int row, final int col) {
        return elements.get(order().index(row, col));
    }

    @Override
    public void set(final int row, final int col, final double value) {
        elements.set(order().index(row, col),  value);
    }

    @Override
    public DenseDoubleMatrix2D view(final Structure structure) {
        return new DenseDoubleMatrix2D(structure, elements);
    }

    @Override
    public DenseDoubleMatrix2D copy(final Structure structure) {
        return new DenseDoubleMatrix2D(structure, elements.copy());
    }

}
