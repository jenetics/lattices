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

import java.util.function.Function;

/**
 * Definition of an array with {@code Object} values.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface ObjectArray<T> extends Array<ObjectArray<T>> {

    /**
     * Return the array value at the given {@code index}.
     *
     * @param index the array index of the returned element
     * @return the element at the given {@code index}
     */
    T get(final int index);

    /**
     * Set the given {@code value} at the given {@code index}.
     *
     * @param index the array index of the new value
     * @param value the value to be set at the given index
     */
    void set(final int index, final T value);


    default <A> ObjectArray<A> map(final Function<? super T, ? extends A> f) {
        return new ObjectArray<A>() {
            @Override
            public A get(int index) {
                return null;
            }
            @Override
            public void set(int index, A value) {

            }
            @Override
            public int length() {
                return 0;
            }
            @Override
            public ObjectArray<A> copy(int start, int length) {
                return null;
            }
            @Override
            public ObjectArray<A> like(int length) {
                return null;
            }
        };
    }

}
