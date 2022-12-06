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
package io.jenetics.linealgebra;

/**
 * Base interface of all matrix implementations. An matrix is a container of
 * elements, which can be accessed by a <em>multidimensional index</em> and has
 * a fixed number of elements (<em>size</em>).
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix<ARRAY_TYPE> {

    /**
     * Return the number of cells of {@code this} matrix.
     *
     * @return the number of cells of {@code this} matrix
     */
    int size();

    /**
     * Return the underlying data array. The data are not copied by this method.
     *
     * @return the underlying data array
     */
    ARRAY_TYPE elements();

}