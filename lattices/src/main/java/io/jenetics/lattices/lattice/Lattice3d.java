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
package io.jenetics.lattices.lattice;

import io.jenetics.lattices.array.Array;
import io.jenetics.lattices.structure.Structure3d;

/**
 * A lattice is defined via an <em>array</em> and a 3-d structure.
 *
 * @param <A> the array type
 */
public interface Lattice3d<A extends Array<A>> {

    /**
     * Return the lattice structure.
     *
     * @return the lattice structure
     */
    Structure3d structure();

    /**
     * Return the array storing the lattice elements.
     *
     * @return the array storing the lattice elements
     */
    A array();

}