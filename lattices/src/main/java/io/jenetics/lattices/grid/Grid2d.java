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

import java.util.function.Function;

import io.jenetics.lattices.Self;
import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.structure.Copyable;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * Base interface for 2-d grids.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Grid2d<A extends Array<A>, G extends Grid2d<A, G>>
    extends Structural2d, Loopable2d, Producible2d<G>, Copyable<G>, Self<G>
{

    A array();

    G create(final Structure2d structure, final A array);

    @Override
    default G view(final Structure2d structure) {
        return create(structure, array());
    }

    @Override
    default G like(final Extent2d extent) {
        return create(
            new Structure2d(extent),
            array().like(extent.size())
        );
    }

    default G view(final Function<? super Structure2d, Structure2d> f) {
        return view(f.apply(structure()));
    }

}
