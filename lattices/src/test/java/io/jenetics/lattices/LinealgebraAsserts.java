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
package io.jenetics.lattices;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Percentage;

import io.jenetics.lattices.grid.Grid1d;
import io.jenetics.lattices.grid.Grid2d;
import io.jenetics.lattices.matrix.NumericalContext;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public final class LinealgebraAsserts {

    public static final Percentage EPSILON = Percentage
        .withPercentage(Math.pow(10, -6));

    private static final NumericalContext CONTEXT = new NumericalContext(Math.pow(10, -4));

    private LinealgebraAsserts() {
    }

    public static void assertEquals(final Grid2d<?, ?> a, final Grid2d<?, ?> b) {
        NumericalContext.using(CONTEXT, () ->
            assertThat(a.equals(b))
                .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
                .isTrue()
        );
    }

    public static void assertNotEquals(final Grid2d<?, ?> a, final Grid2d<?, ?> b) {
        NumericalContext.using(CONTEXT, () ->
            assertThat(a.equals(b))
                .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
                .isFalse()
        );
    }

    public static void assertEquals(final Grid1d<?, ?> a, final Grid1d<?, ?> b) {
        NumericalContext.using(CONTEXT, () ->
            assertThat(a.equals(b))
                .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
                .isTrue()
        );
    }

    public static void assertNotEquals(final Grid1d<?, ?> a, final Grid1d<?, ?> b) {
        NumericalContext.using(CONTEXT, () ->
            assertThat(a.equals(b))
                .withFailMessage("Expected \n%s\nbut got\n%s".formatted(a, b))
                .isFalse()
        );
    }

}
