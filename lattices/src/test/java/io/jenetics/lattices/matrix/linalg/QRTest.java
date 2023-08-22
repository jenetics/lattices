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

import static io.jenetics.lattices.testfixtures.Colts.toColt;
import static io.jenetics.lattices.testfixtures.Colts.toLinealgebra;
import static io.jenetics.lattices.testfixtures.LinealgebraAsserts.assertEquals;

import cern.colt.matrix.linalg.QRDecomposition;

import org.testng.annotations.Test;

import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.testfixtures.MatrixRandom;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class QRTest {

    @Test(invocationCount = 20, successPercentage = 80)
    public void decompose() {
        final var A = MatrixRandom.nextDoubleMatrix2d(new Extent2d(15, 15));

        final var expected = new QRDecomposition(toColt(A));
        final var qr = QR.decompose(A);

        assertEquals(qr.Q(), toLinealgebra(expected.getQ()));
        assertEquals(qr.R(), toLinealgebra(expected.getR()));
        assertEquals(qr.H(), toLinealgebra(expected.getH()));
    }

    @Test(invocationCount = 20, successPercentage = 80)
    public void solver() {
        final var extent = new Extent2d(15, 15);
        final var A = MatrixRandom.nextDoubleMatrix2d(extent);
        final var B = MatrixRandom.nextDoubleMatrix2d(new Extent2d(extent.nrows(), 45));

        assertEquals(
            QR.decompose(A).solve(B),
            toLinealgebra(new QRDecomposition(toColt(A)).solve(toColt(B)))
        );
    }

}
