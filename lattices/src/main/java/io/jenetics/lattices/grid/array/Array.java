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
public interface Array<A extends Array<A>> extends BaseArray, Self<A> {

    /**
     * Mixin interface of <em>dense</em> array implementations. This interface
     * defines a lightweight wrapper for the underlying <em>dense</em>Java array
     * of type {@code JAVA_ARRAY}.
     *
     * @param <JAVA_ARRAY> the wrapped Java array type, like {@code double[]},
     *        {@code int[]} or {@code Object[]}
     * @param <D> the implementation type of the dense array wrapper
     */
    interface Dense<JAVA_ARRAY, D extends Dense<JAVA_ARRAY, D>> {

        /**
         * Return the underlying <em>native</em> Java array.
         *
         * @return the underlying <em>native</em> Java array
         */
        JAVA_ARRAY elements();

        /**
         * The start index of the underlying Java array (inclusively).
         *
         * @return start index of the underlying Java array (inclusively)
         */
        int from();

        /**
         * Return the length of the Java array view.
         *
         * @return the array length
         */
        int length();

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
     * Copies the specified range of this array
     *
     * @param from the initial index of the range to be copied, inclusive
     * @param length the size the range to be copied
     * @return a new array of the given range
     */
    A copy(int from, int length);

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

    /**
     * Definition of an array with {@code double} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfDouble extends BaseArray.OfDouble, Array<OfDouble> {
    }

    /**
     * Definition of an array with {@code int} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfInt extends BaseArray.OfInt, Array<OfInt> {
    }

    /**
     * Definition of an array with {@code long} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfLong extends BaseArray.OfLong, Array<OfLong> {
    }

    /**
     * Definition of an array with {@code Object} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfObject<T> extends BaseArray.OfObject<T>, Array<OfObject<T>> {
    }
}
