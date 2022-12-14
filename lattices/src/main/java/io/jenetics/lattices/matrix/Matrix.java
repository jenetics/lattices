/*
 * Java Linear Algebra Library (@__identifier__@).
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
package io.jenetics.lattices.matrix;

import io.jenetics.lattices.Self;

/**
 * Base interface of all matrix implementations. An matrix is a container of
 * elements, which can be accessed by a <em>multidimensional index</em> and has
 * a fixed number of elements (<em>size</em>).
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix<M extends Matrix<M>> extends Self<M> {

    /**
     * Return a new matrix with is like this one.
     *
     * @return a new matrix which is like this one
     */
    M like();

    /**
     * Return a new minimal copy of the underlying element array.
     *
     * @return a new minimal copy of the underlying element array
     */
    M copy();

}
