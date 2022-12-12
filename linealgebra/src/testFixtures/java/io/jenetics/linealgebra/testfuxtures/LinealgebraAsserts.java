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
package io.jenetics.linealgebra.testfuxtures;

import static org.assertj.core.api.Assertions.assertThat;

import io.jenetics.linealgebra.NumericalContext;
import io.jenetics.linealgebra.grid.DoubleGrid1d;
import io.jenetics.linealgebra.grid.DoubleGrid2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public final class LinealgebraAsserts {

    public static final NumericalContext DEFAULT_CONTEXT = new NumericalContext() {
        @Override
        public double epsilon() {
            return 0.000000001;
        }
    };

    private LinealgebraAsserts() {
    }

    public static void assertEquals(final DoubleGrid2d a, final DoubleGrid2d b) {
        assertThat(a.equals(b, DEFAULT_CONTEXT))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isTrue();
    }

    public static void assertNotEquals(final DoubleGrid2d a, final DoubleGrid2d b) {
        assertThat(a.equals(b, DEFAULT_CONTEXT))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isFalse();
    }

    public static void assertEquals(final DoubleGrid1d a, final DoubleGrid1d b) {
        assertThat(a.equals(b, DEFAULT_CONTEXT))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isTrue();
    }

    public static void assertNotEquals(final DoubleGrid1d a, final DoubleGrid1d b) {
        assertThat(a.equals(b, DEFAULT_CONTEXT))
            .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
            .isFalse();
    }

}
