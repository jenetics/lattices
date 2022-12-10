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

import static org.assertj.core.api.Assertions.assertThat;

import io.jenetics.linealgebra.structure.DoubleGrid2d;

public final class LinealgebraAsserts {

    public static final double DEFAULT_PRECISION = 0.0001;

    private LinealgebraAsserts() {
    }

    public static void assertEquals(final DoubleGrid2d a, final DoubleGrid2d b) {
        assertThat(a.equals(b, DEFAULT_PRECISION))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isTrue();
    }

    public static void assertNotEquals(final DoubleGrid2d a, final DoubleGrid2d b) {
        assertThat(a.equals(b, DEFAULT_PRECISION))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isFalse();
    }

}
