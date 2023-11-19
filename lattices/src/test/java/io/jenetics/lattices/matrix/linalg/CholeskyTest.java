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
package io.jenetics.lattices.matrix.linalg;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.Colts.toColt;
import static io.jenetics.lattices.Colts.toLinealgebra;
import static io.jenetics.lattices.LinealgebraAsserts.assertEquals;

import cern.colt.matrix.linalg.CholeskyDecomposition;

import org.testng.annotations.Test;

import io.jenetics.lattices.Colts;
import io.jenetics.lattices.MatrixRandom;
import io.jenetics.lattices.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class CholeskyTest {

    @Test(invocationCount = 20, successPercentage = 80)
    public void decompose() {
        final var matrix = MatrixRandom.nextDoubleMatrix2d(new Extent2d(15, 15));

        final var expected = new CholeskyDecomposition(Colts.toColt(matrix));
        final var cholesky = Cholesky.decompose(matrix);

        assertEquals(cholesky.L(), toLinealgebra(expected.getL()));
        assertThat(cholesky.isSymmetricPositiveDefinite())
            .isEqualTo(expected.isSymmetricPositiveDefinite());
    }

    @Test(invocationCount = 20, successPercentage = 80)
    public void solver() {
        final var extent = new Extent2d(15, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);
        final var B = MatrixRandom.nextDoubleMatrix2d(new Extent2d(extent.rows(), 23));

        assertEquals(
            Cholesky.decompose(A).solve(B),
            toLinealgebra(new CholeskyDecomposition(toColt(A)).solve(toColt(B)))
        );
    }

}
