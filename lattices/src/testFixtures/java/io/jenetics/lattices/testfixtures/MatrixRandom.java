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
package io.jenetics.lattices.testfixtures;

import java.util.random.RandomGenerator;

import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public final class MatrixRandom {
    private MatrixRandom() {
    }

    public static DoubleMatrix2d next(final Extent2d extent, final RandomGenerator random) {
        final var result = DoubleMatrix2d.DENSE.create(extent);
        result.assign(a -> random.nextDouble(-1, 1));
        return result;
    }

    public static DoubleMatrix2d nextDoubleMatrix2d(final Extent2d extent) {
        return next(extent, RandomGenerator.getDefault());
    }

    public static DoubleMatrix2d nextDoubleMatrix2d(final int rows, final int cols) {
        return nextDoubleMatrix2d(new Extent2d(rows, cols));
    }

    public static DoubleMatrix1d next(final Extent1d extent, final RandomGenerator random) {
        final var result = DoubleMatrix1d.DENSE.create(extent);
        result.assign(a -> random.nextDouble(-1, 1));
        return result;
    }

    public static DoubleMatrix1d nextDoubleMatrix1d(final Extent1d extent) {
        return next(extent, RandomGenerator.getDefault());
    }

    public static DoubleMatrix1d nextDoubleMatrix1d(final int size) {
        return nextDoubleMatrix1d(new Extent1d(size));
    }

}
