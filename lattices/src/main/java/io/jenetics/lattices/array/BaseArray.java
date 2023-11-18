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
package io.jenetics.lattices.array;

/**
 * Base interface of all array implementations. An array is a container of
 * elements, which can be accessed by an <em>index</em> and has a fixed
 * <em>size</em>.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface BaseArray {

    /**
     * Return the size of {@code this} array.
     *
     * @return the size of {@code this} array
     */
    int length();

    /**
     * Definition of an array with {@code double} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfDouble extends BaseArray {
        /**
         * Return the array value at the given {@code index}.
         *
         * @param index the array index of the returned element
         * @return the element at the given {@code index}
         */
        double get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         */
        void set(int index, double value);
    }

    /**
     * Definition of an array with {@code int} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfInt extends BaseArray {
        /**
         * Return the array value at the given {@code index}.
         *
         * @param index the array index of the returned element
         * @return the element at the given {@code index}
         */
        int get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         */
        void set(int index, int value);
    }

    /**
     * Definition of an array with {@code long} values.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfLong extends BaseArray {
        /**
         * Return the array value at the given {@code index}.
         *
         * @param index the array index of the returned element
         * @return the element at the given {@code index}
         */
        long get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         */
        void set(int index, long value);
    }

    /**
     * Definition of an array with {@code T} objects.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @since 3.0
     * @version 3.0
     */
    interface OfObject<T> extends BaseArray {
        /**
         * Return the array value at the given {@code index}.
         *
         * @param index the array index of the returned element
         * @return the element at the given {@code index}
         */
        T get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         */
        void set(int index, T value);
    }

}
