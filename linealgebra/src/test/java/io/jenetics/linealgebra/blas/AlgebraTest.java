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

import cern.colt.matrix.linalg.Algebra;

import org.testng.annotations.Test;

import io.jenetics.linealgebra.grid.Extent2d;
import io.jenetics.linealgebra.testfuxtures.Colts;
import io.jenetics.linealgebra.testfuxtures.LinealgebraAsserts;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class AlgebraTest {

    @Test
    public void solve() {
        final var extent = new Extent2d(55, 55);
        final var A = next(extent);
        final var B = next(extent);

        final var X = io.jenetics.linealgebra.blas.Algebra.solve(A, B);

        final var expected = Colts.toLinealgebra(
            Algebra.DEFAULT.solve(Colts.toColt(A), Colts.toColt(B)));

        LinealgebraAsserts.assertEquals(X, expected);
    }

}
