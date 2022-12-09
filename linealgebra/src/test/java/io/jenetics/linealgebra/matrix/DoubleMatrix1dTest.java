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
package io.jenetics.linealgebra.matrix;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class DoubleMatrix1dTest {

    @Test
    public void like() {
        final var matrix = DoubleMatrix1d.DENSE_FACTORY.newInstance(23);
        matrix.forEach(i -> matrix.set(i, 2*i));
        final var like = matrix.like();

        assertThat(like.size()).isEqualTo(matrix.size());
        like.forEach(i -> assertThat(like.get(i)).isEqualTo(0.0));
    }


}
