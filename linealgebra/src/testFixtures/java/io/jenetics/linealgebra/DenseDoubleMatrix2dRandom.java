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

import static java.util.Objects.requireNonNull;

import java.util.random.RandomGenerator;

import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Extent2d;

public class DenseDoubleMatrix2dRandom {

    private final RandomGenerator random;

    public DenseDoubleMatrix2dRandom(final RandomGenerator random) {
        this.random = requireNonNull(random);
    }

    public DoubleMatrix2d next(final Extent2d extent) {
        final var result = DoubleMatrix2d.DENSE_FACTORY.newInstance(extent);
        result.update(a -> random.nextInt(1000)/100.0);
        return result;
    }

    public static DoubleMatrix2d nextMatrix(final Extent2d extent2d) {
        return new DenseDoubleMatrix2dRandom(RandomGenerator.getDefault())
            .next(extent2d);
    }

}
