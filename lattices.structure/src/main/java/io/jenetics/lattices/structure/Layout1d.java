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
package io.jenetics.lattices.structure;

import static java.util.Objects.requireNonNull;

/**
 * This class defines the layout of the 1-d data onto the 1-d array like data
 * structure. The layout is defined by the 1-d start index and the 1-d strides.
 *
 * @apiNote
 * Note, that the direct manipulation/creation of the <em>layout</em> object
 * usually doesn't lead to the expected result. It is expected that layouts
 * are created by the <em>structure</em> object; {@link Structure1d#of(Extent1d)}.
 *
 * @see Structure1d
 *
 * @param start the index of the first element
 * @param stride the number of indexes between any two elements
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Layout1d(Index1d start, Stride1d stride)
    implements OffsetMapping1d
{

    /**
     * The default order.
     */
    public static final Layout1d DEFAULT = new Layout1d(0, 1);

    public Layout1d {
        requireNonNull(start);
        requireNonNull(stride);
    }

    public Layout1d(int start, int stride) {
        this(new Index1d(start), new Stride1d(stride));
    }

    @Override
    public int offset(int index) {
        return start.value() + index*stride.value();
    }

    @Override
    public int offset(Index1d index) {
        return offset(index.value());
    }

    @Override
    public Index1d index(int offset) {
        final int start = offset - this.start.value();
        final int index = start/stride.value();
        return new Index1d(index);
    }

}
