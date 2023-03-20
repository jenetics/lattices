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
package io.jenetics.lattices.structure.util;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

import io.jenetics.lattices.structure.Index1d;
import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.Stride1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
class Index1dIterator implements Iterator<Index1d> {

    private final Range1d range;
    private final Stride1d stride;

    private int cursor;

    Index1dIterator(Range1d range, Stride1d stride) {
        this.range = requireNonNull(range);
        this.stride = requireNonNull(stride);

        cursor = range.start().value();
    }

    @Override
    public boolean hasNext() {
        return cursor < range.start().value() + range.extent().size();
    }

    @Override
    public Index1d next() {
        final int i = cursor;
        if (cursor >= range.start().value() + range.extent().size()) {
            throw new NoSuchElementException();
        }

        cursor = i + stride.value();
        return new Index1d(i);
    }

}