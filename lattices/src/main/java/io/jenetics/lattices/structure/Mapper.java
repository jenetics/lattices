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

/**
 * Mapper for {@code index -> offset} and {@code offset -> index} mapping.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Mapper extends Dimensional {

    /**
     * Return the position of the element with the given relative {@code rank}
     * within the (virtual or non-virtual) internal n-d array.
     *
     * @param index the index of the element.
     * @return the (linearized) index of the given {@code index}
     */
    int offset(int... index);

    //int offset(Index index);

    /**
     * Calculates the index for the given {@code offset}.
     *
     * @param offset the offset for which to calculate the index
     * @return the index for the given {@code offset}
     */
    Index index(int offset);

}
