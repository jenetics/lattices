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
package io.jenetics.lattices.structure.testfixtures;

import java.util.random.RandomGenerator;

import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Index3d;
import io.jenetics.lattices.structure.Range3d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Index3dRandom {

    private final RandomGenerator random;

    public Index3dRandom(RandomGenerator random) {
        this.random = random;
    }

    public Index3d next(Range3d range) {
        final Index3d start = range.start();
        final Extent3d bound = range.extent();

        final int slice = random.nextInt(
            start.slice(),
            start.slice() + bound.slices()
        );
        final int row = random.nextInt(
            start.row(),
            start.row() + bound.rows()
        );
        final int col = random.nextInt(
            start.col(),
            start.col() + bound.cols()
        );

        return new Index3d(slice, row, col);
    }

}
