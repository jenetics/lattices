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
package io.jenetics.lattices.grid.array;

import io.jenetics.lattices.grid.Self;

/**
 * Base interface of all array implementations. An array is a container of
 * elements, which can be accessed by an <em>index</em> and has a fixed
 * <em>size</em>.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface BaseArray<A extends BaseArray<A>> extends Self<A> {

    /**
     * Base interface of all <em>dense</em> array implementations. This interface
     * defines a lightweight wrapper for the underlying Java array of type
     * {@code T_PRIMITIVE}.
     *
     * @param <T_PRIMITIVE> the wrapped Java array type, like {@code double[]},
     *        {@code int[]} or {@code Object[]}
     * @param <D> the implementation type of the dense array wrapper
     */
    interface Dense<T_PRIMITIVE, D extends Dense<T_PRIMITIVE, D>>
        extends BaseArray<D>
    {
        T_PRIMITIVE elements();
        int from();

        @Override
        default void assign(D src, int srcPos, int destPos, int length) {
            System.arraycopy(
                src.elements(),
                srcPos + src.from(),
                elements(),
                destPos + from(),
                length
            );
        }
    }

    /**
     * Return the size of {@code this} array.
     *
     * @return the size of {@code this} array
     */
    int length();

    /**
     * Copies the specified range of this array
     *
     * @param start the initial index of the range to be copied, inclusive
     * @param length the size the range to be copied
     * @return a new array of the given range
     */
    A copy(int start, int length);

    /**
     * Return a new copy of the given double array.
     *
     * @see #copy(int, int)
     *
     * @return a new copy of the given double array
     */
    default A copy() {
        return copy(0, length());
    }

    /**
     * Return a new array of the same type with the given {@code length}.
     *
     * @param length the size of the new array
     * @return a new array of the same type with the given {@code length}
     */
    A like(int length);

    /**
     * Return a new array of the same type and {@link #length()} as this one.
     *
     * @return a new array of the same type
     */
    default A like() {
        return like(length());
    }

    default void assign(A src, int srcPos, int destPos, int length) {

    }

}
