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

import org.testng.annotations.Test;

public class DoubleMatrix2DTest {

    @Test
    public void assign() {
        final var matrix = new DenseDoubleMatrix2D(4, 3);
        matrix.assign(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        assertThat(matrix.get(0,0)).isEqualTo(1);
        assertThat(matrix.get(1,1)).isEqualTo(5);
        assertThat(matrix.get(2,2)).isEqualTo(9);
        assertThat(matrix.get(3,2)).isEqualTo(12);
    }

    @Test
    public void reduce() {
        final var matrix = new DenseDoubleMatrix2D(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        final var result = matrix.reduce(Double::sum, a -> 2*a);
        assertThat(result).isEqualTo(156);
    }

    @Test
    public void zSum() {
        final var matrix = new DenseDoubleMatrix2D(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });

        final var result = matrix.zSum();
        assertThat(result).isEqualTo(78);
    }

}
