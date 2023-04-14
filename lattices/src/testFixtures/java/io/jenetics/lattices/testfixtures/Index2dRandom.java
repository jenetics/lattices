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
package io.jenetics.lattices.testfixtures;

import java.util.random.RandomGenerator;

import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.Range2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Index2dRandom {

    private final RandomGenerator random;

    public Index2dRandom(RandomGenerator random) {
        this.random = random;
    }

    public Index2d next(Range2d range) {
        final Index2d start = range.start();
        final Extent2d extent = range.extent();

        final int row = random.nextInt(
            start.row(),
            start.row() + extent.rows()
        );
        final int col = random.nextInt(
            start.col(),
            start.col() + extent.cols()
        );

        return new Index2d(row, col);
    }

}
