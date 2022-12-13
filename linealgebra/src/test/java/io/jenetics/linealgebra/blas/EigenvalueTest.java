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

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.linealgebra.testfuxtures.Colts.toColt;
import static io.jenetics.linealgebra.testfuxtures.Colts.toLinealgebra;
import static io.jenetics.linealgebra.testfuxtures.LinealgebraAsserts.assertEquals;
import static io.jenetics.linealgebra.testfuxtures.MatrixRandom.next;

import cern.colt.matrix.linalg.EigenvalueDecomposition;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.grid.Extent2d;
import io.jenetics.linealgebra.matrix.Matrices;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class EigenvalueTest {

    @Test
    public void decompose() {
        final var A = next(new Extent2d(50, 50));
        assertThat(Matrices.isSymmetric(A)).isFalse();

        final var expected = new EigenvalueDecomposition(toColt(A));
        final var eigen = Eigenvalue.decompose(A);

        assertEquals(eigen.D(), toLinealgebra(expected.getD()));
        assertEquals(eigen.V(), toLinealgebra(expected.getV()));
        assertEquals(
            eigen.realEigenvalues(),
            toLinealgebra(expected.getRealEigenvalues())
        );
        assertEquals(
            eigen.imagEigenvalues(),
            toLinealgebra(expected.getImagEigenvalues())
        );
    }

    @Test
    public void decomposeSymmetric() {
        final var A = next(new Extent2d(50, 50));
        A.forEach((i, j) -> A.set(i, j, A.get(j, i)));
        Matrices.checkSymmetric(A);

        final var expected = new EigenvalueDecomposition(toColt(A));
        final var eigen = Eigenvalue.decompose(A);

        assertEquals(eigen.D(), toLinealgebra(expected.getD()));
        assertEquals(eigen.V(), toLinealgebra(expected.getV()));
        assertEquals(
            eigen.realEigenvalues(),
            toLinealgebra(expected.getRealEigenvalues())
        );
        assertEquals(
            eigen.imagEigenvalues(),
            toLinealgebra(expected.getImagEigenvalues())
        );
    }

}
