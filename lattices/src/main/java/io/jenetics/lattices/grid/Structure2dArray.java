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

import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Structure2d;

public record Structure2dArray<A extends Array<A>>(Structure2d structure, A array)
    implements Producible2d<Structure2dArray<A>>
{
    @Override
    public Structure2dArray<A> view(final Structure2d structure) {
        return new Structure2dArray<>(structure, array);
    }

    @Override
    public Structure2dArray<A> like(final Extent2d extent) {
        return new Structure2dArray<>(
            new Structure2d(extent),
            array.like(extent.size())
        );
    }

    public Structure2dArray<A> copy() {
        return null;
    }
}
