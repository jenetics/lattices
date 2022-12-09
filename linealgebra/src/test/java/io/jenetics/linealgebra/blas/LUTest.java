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
package io.jenetics.linealgebra.blas;

import cern.colt.matrix.linalg.LUDecompositionQuick;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.Colts;
import io.jenetics.linealgebra.DenseDoubleMatrix2dRandom;
import io.jenetics.linealgebra.LinealgebraAsserts;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class LUTest {

    @Test
    public void decompose() {
        final var matrix = DenseDoubleMatrix2dRandom
            .nextMatrix(new Extent2d(5, 5));

        final var expected = decompose(matrix);
        LU.decompose(matrix);

        LinealgebraAsserts.assertEquals(matrix, expected);
    }

    private static DoubleMatrix2d decompose(final DoubleMatrix2d matrix) {
        final var colt = Colts.toColt(matrix);

        final var decomposer = new LUDecompositionQuick();
        decomposer.decompose(colt);
        return Colts.toLinealgebra(colt);
    }

}
