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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public final class Colts {
    private Colts() {
    }

    public static Object toLinealgebra(final Object colt) {
        if (colt instanceof DoubleMatrix1D matrix) {
            return toLinealgebra(matrix);
        } else if (colt instanceof DoubleMatrix2D matrix) {
            return toLinealgebra(matrix);
        } else {
            return null;
        }
    }

    public static DenseDoubleMatrix1D toColt(final DoubleMatrix1d matrix) {
        final var colt = new DenseDoubleMatrix1D(matrix.size());
        matrix.forEach(i -> colt.setQuick(i, matrix.get(i)));
        return colt;
    }

    public static DenseDoubleMatrix2D toColt(final DoubleMatrix2d matrix) {
        final var colt = new DenseDoubleMatrix2D(matrix.rows(), matrix.cols());
        matrix.forEach((r, c) -> colt.setQuick(r, c, matrix.get(r, c)));
        return colt;
    }

    public static DoubleMatrix2d toLinealgebra(final DoubleMatrix2D matrix) {
        final var la = DoubleMatrix2d.DENSE_FACTORY
            .newInstance(matrix.rows(), matrix.columns());

        la.forEach((r, c) -> la.set(r, c, matrix.getQuick(r, c)));
        return la;
    }

    public static DoubleMatrix1d toLinealgebra(final DoubleMatrix1D matrix) {
        final var la = DoubleMatrix1d.DENSE_FACTORY
            .newInstance(matrix.size());

        la.forEach(i -> la.set(i, matrix.getQuick(i)));
        return la;
    }

}
