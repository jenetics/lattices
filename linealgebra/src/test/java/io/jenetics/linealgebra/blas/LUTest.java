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

import static io.jenetics.linealgebra.MatrixRandom.next;

import cern.colt.matrix.linalg.LUDecomposition;
import cern.colt.matrix.linalg.LUDecompositionQuick;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.Colts;
import io.jenetics.linealgebra.LinealgebraAsserts;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class LUTest {

    @Test(invocationCount = 10)
    public void decompose() {
        final var matrix = next(new Extent2d(50, 50));

        final var expected = coldDecompose(matrix);
        LU.decompose(matrix);

        LinealgebraAsserts.assertEquals(matrix, expected);
    }

    private static DoubleMatrix2d coldDecompose(final DoubleMatrix2d matrix) {
        final var colt = Colts.toColt(matrix);

        final var decomposer = new LUDecompositionQuick();
        decomposer.decompose(colt);
        return Colts.toLinealgebra(colt);
    }

    @Test
    public void solver() {
        final var extent = new Extent2d(55, 55);
        final var matrix = next(extent);
        final var B = next(extent);

        final var expected = coldSolve(matrix, B);

        final var lu = LU.decompose(matrix);
        lu.solve(B);

        LinealgebraAsserts.assertEquals(B, expected);
    }

    private static DoubleMatrix2d coldSolve(final DoubleMatrix2d matrix, final DoubleMatrix2d B) {
        final var coltMatrix = Colts.toColt(matrix);
        final var coltB = Colts.toColt(B);

        final var decomposer = new LUDecomposition(coltMatrix);
        final var result = decomposer.solve(coltB);
        return Colts.toLinealgebra(result);
    }

}
