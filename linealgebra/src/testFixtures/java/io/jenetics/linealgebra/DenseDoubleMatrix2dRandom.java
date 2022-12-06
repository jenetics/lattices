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

import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;

public class DenseDoubleMatrix2dRandom {

    private final RandomGenerator random;

    public DenseDoubleMatrix2dRandom(final RandomGenerator random) {
        this.random = requireNonNull(random);
    }

    public DenseDoubleMatrix2d next(final Matrix2d.Dimension dimension) {
        final var result = new DenseDoubleMatrix2d(dimension);
        result.assign(a -> random.nextInt(1000)/100.0);
        return result;
    }

}
