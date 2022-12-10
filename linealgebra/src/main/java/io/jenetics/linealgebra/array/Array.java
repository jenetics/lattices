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
package io.jenetics.linealgebra.array;

import io.jenetics.linealgebra.Self;

/**
 * Base interface of all array implementations. An array is a container of
 * elements, which can be accessed by an <em>index</em> and has a fixed
 * <em>size</em>.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Array<A extends Array<A>> extends Self<A> {

    /**
     * Return the size of {@code this} array.
     *
     * @return the size of {@code this} array
     */
    int length();

    /**
     * Return a new copy of the given double array.
     *
     * @return a new copy of the given double array
     */
    default A copy() {
        return copy(0, length());
    }

    /**
     * Copies the specified range of this array
     *
     * @param start the initial index of the range to be copied, inclusive
     * @param length the size the range to be copied
     * @return a new array of the given range
     */
    A copy(final int start, final int length);

    /**
     * Return a new array of the same type with the given {@code length}.
     *
     * @param length the size of the new array
     * @return a new array of the same type with the given {@code length}
     */
    A like(final int length);

    /**
     * Return a new array of the same type and {@link #length()} as this one.
     *
     * @return a new array of the same type
     */
    default A like() {
        return like(length());
    }

}
