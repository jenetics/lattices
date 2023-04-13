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
package io.jenetics.lattices.grid;

import static java.util.Objects.requireNonNull;

import io.jenetics.lattices.grid.function.Int3Consumer;
import io.jenetics.lattices.grid.function.Int3Predicate;
import io.jenetics.lattices.structure.Extent3d;
import io.jenetics.lattices.structure.Range3d;

/**
 * Slice-major loop implementation. The rows and columns are iterated forward.
 *
 * @param range the range which defines the boundaries of the loop
 */
record Loop3dSliceFirst(Range3d range) implements Loop3d {

    public Loop3dSliceFirst(Extent3d extent) {
        this(new Range3d(extent));
    }

    @Override
    public void forEach(Int3Consumer action) {
        requireNonNull(action);

        for (int s = range.start().slice(),
             d = range.start().slice() + range.extent().slices();
             s < d; ++s)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                for (int c = range.start().col(),
                     w = range.start().col() + range.extent().cols();
                     c < w; ++c)
                {
                    action.accept(s, r, c);
                }
            }
        }
    }

    @Override
    public boolean anyMatch(Int3Predicate predicate) {
        requireNonNull(predicate);

        for (int s = range.start().slice(),
             d = range.start().slice() + range.extent().slices();
             s < d; ++s)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                for (int c = range.start().col(),
                     w = range.start().col() + range.extent().cols();
                     c < w; ++c)
                {
                    if (predicate.test(s, r, c)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean allMatch(Int3Predicate predicate) {
        requireNonNull(predicate);

        for (int s = range.start().slice(),
             d = range.start().slice() + range.extent().slices();
             s < d; ++s)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                for (int c = range.start().col(),
                     w = range.start().col() + range.extent().cols();
                     c < w; ++c)
                {
                    if (!predicate.test(s, r, c)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean nonMatch(Int3Predicate predicate) {
        requireNonNull(predicate);

        for (int s = range.start().slice(),
             d = range.start().slice() + range.extent().slices();
             s < d; ++s)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                for (int c = range.start().col(),
                     w = range.start().col() + range.extent().cols();
                     c < w; ++c)
                {
                    if (predicate.test(s, r, c)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
