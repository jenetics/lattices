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
package io.jenetics.lattices.blas;

import static org.assertj.core.api.Assertions.assertThat;
import static io.jenetics.lattices.testfuxtures.Colts.toColt;
import static io.jenetics.lattices.testfuxtures.Colts.toLinealgebra;
import static io.jenetics.lattices.testfuxtures.LinealgebraAsserts.EPSILON;
import static io.jenetics.lattices.testfuxtures.LinealgebraAsserts.assertEquals;
import static io.jenetics.lattices.testfuxtures.MatrixRandom.next;

import cern.colt.matrix.linalg.SingularValueDecomposition;

import org.testng.annotations.Test;

import io.jenetics.lattices.matrix.Matrices;
import io.jenetics.lattices.matrix.blas.SingularValue;
import io.jenetics.lattices.structure.Extent2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class SingularValueTest {

    @Test(invocationCount = 5)
    public void decompose() {
        final var A = next(new Extent2d(50, 50));
        assertThat(Matrices.isSymmetric(A)).isFalse();

        final var expected = new SingularValueDecomposition(toColt(A));
        final var singular = SingularValue.decompose(A);

        assertEquals(singular.S(), toLinealgebra(expected.getS()));
        assertEquals(singular.U(), toLinealgebra(expected.getU()));
        assertEquals(singular.V(), toLinealgebra(expected.getV()));
        assertThat(singular.rank()).isEqualTo(expected.rank());
        assertThat(singular.norm2())
            .isCloseTo(expected.norm2(), EPSILON);
        assertThat(singular.cond())
            .isCloseTo(expected.cond(), EPSILON);

        assertEquals(
            singular.values(),
            toLinealgebra(expected.getSingularValues())
        );
    }

}
