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
 * Functional interface for doing view transformation.
 */
@FunctionalInterface
public interface View1d {

    /**
     * Applies the <em>view</em> transformation of the given {@code structure}.
     *
     * @param structure the structure to apply the view transformation on
     * @return a new <em>view</em>-structure
     */
    Structure1d apply(Structure1d structure);

    /**
     * Return a transformation which creates a view of the given {@code range}.
     *
     * @param range the range of the view
     * @return a transformation which creates a view of the given {@code range}
     */
    static View1d of(Range1d range) {
        requireNonNull(range);

        return structure -> new Structure1d(
            range.extent(),
            new Layout1d(
                structure.layout().start().value() +
                    structure.layout().stride().value()*range.start().value(),
                structure.layout().stride().value()
            )
        );
    }

    /**
     * Return a transformation which creates a view of the given {@code start}.
     *
     * @param start the start of the view
     * @return a transformation which creates a view of the given {@code start}
     */
    static View1d of(Index1d start) {
        requireNonNull(start);

        return structure -> View1d
            .of(
                new Range1d(
                    start,
                    new Extent1d(structure.extent().size() - start.value())
                )
            )
            .apply(structure);
    }

    /**
     * Return a new stride view transformation.
     *
     * @param stride the stride of the created view transformation
     * @return a new stride view transformation
     */
    static View1d of(Stride1d stride) {
        requireNonNull(stride);

        return structure -> new Structure1d(
            new Extent1d(
                structure.extent().size() != 0
                    ? (structure.extent().size() - 1)/stride.value() + 1
                    : 0
            ),
            new Layout1d(
                structure.layout().start().value(),
                structure.layout().stride().value()*stride.value()
            )
        );
    }

}
