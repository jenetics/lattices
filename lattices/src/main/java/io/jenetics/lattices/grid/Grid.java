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

import io.jenetics.lattices.Self;
import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.structure.Copyable;

/**
 * A grid defines a structure onto a given (one-dimensional) array. Every grid
 * is <em>just</em> a <em>structural</em> view onto the underlying array. If
 * you want ot create a copy of the grid, you have to explicitly call the
 * {@link #copy()} method.
 *
 * @see #copy()
 *
 * @param <A> the array type which stores the grid elements
 * @param <G> the <em>self</em> grid type
 */
public interface Grid<A extends Array<A>, G extends Grid<A, G>>
    extends Copyable<G>, Self<G>
{

    /**
     * Return the underlying (one-dimensional) array of this grid.
     *
     * @return the underlying array of this grid
     */
    A array();

}
