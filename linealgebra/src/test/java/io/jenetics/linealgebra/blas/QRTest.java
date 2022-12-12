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

import static io.jenetics.linealgebra.testfuxtures.MatrixRandom.next;

import cern.colt.matrix.linalg.QRDecomposition;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.grid.Extent2d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.testfuxtures.Colts;
import io.jenetics.linealgebra.testfuxtures.LinealgebraAsserts;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class QRTest {

    @Test(invocationCount = 5)
    public void decompose() {
        final var matrix = next(new Extent2d(50, 50));

        final var expected = coldDecompose(matrix);

        QR.decompose(matrix);
        LinealgebraAsserts.assertEquals(matrix, expected);
    }

    private static DoubleMatrix2d coldDecompose(final DoubleMatrix2d matrix) {
        return Colts.toLinealgebra(new QRDecomposition(Colts.toColt(matrix)).QR);
    }

    @Test(invocationCount = 5)
    public void solver() {
        final var extent = new Extent2d(50, 50);
        final var matrix = next(extent);
        final var B = next(new Extent2d(extent.rows(), 100));

        final var expected = new QRDecomposition(Colts.toColt(matrix))
            .solve(Colts.toColt(B));

        final var qr = QR.decompose(matrix);
        final var x = qr.solve(B);

        LinealgebraAsserts.assertEquals(x, Colts.toLinealgebra(expected));
    }

}
