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

import java.util.Objects;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.function.IntDoubleConsumer;
import io.jenetics.lattices.function.IntDoubleToDoubleFunction;
import io.jenetics.lattices.function.Int2ToIntFunction;
import io.jenetics.lattices.function.IntLongConsumer;
import io.jenetics.lattices.function.IntLongToLongFunction;
import io.jenetics.lattices.function.IntObjectConsumer;
import io.jenetics.lattices.function.IntObjectToObjectFunction;

/**
 * Base interface of all array implementations. An array is a container of
 * elements, which can be accessed by an <em>index</em> and has a fixed
 * <em>length</em>.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface BaseArray {

    /**
     * Return the length of {@code this} array.
     *
     * @return the length of {@code this} array
     */
    int length();

    /**
     * Definition of an array of {@code double} values.
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
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        double get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        void set(int index, double value);

        /**
         * Applies a procedure to each (index, value) pair of the receivers.
         *
         * @param consumer the procedure to be applied
         */
        default void forEach(IntDoubleConsumer consumer) {
            for (int i = 0; i < length(); ++i) {
                consumer.accept(i, get(i));
            }
        }

        /**
         * Update all array values using the given function {@code fn}.
         *
         * @param fn the update function
         */
        default void update(IntDoubleToDoubleFunction fn) {
            for (int i = 0; i < length(); ++i) {
                set(i, fn.apply(i, get(i)));
            }
        }

        /**
         * Return a double stream from the given array.
         *
         * @return a double stream from the given array
         */
        default DoubleStream stream() {
            return IntStream.range(0, length()).mapToDouble(this::get);
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         * @param srcPos starting position in the source array.
         * @param destPos starting position in the destination data
         * @param length the number of array elements to be copied
         * @throws IndexOutOfBoundsException  if copying causes access of data
         *         outside array bounds.
         * @throws NullPointerException if either {@code src} is {@code null}.
         */
        default void assign(OfDouble src, int srcPos, int destPos, int length) {
            Objects.checkFromIndexSize(srcPos, length, src.length());
            Objects.checkFromIndexSize(destPos, length, this.length());

            for (int i = 0; i < length; ++i) {
                set(destPos + i, src.get(srcPos + i));
            }
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         */
        default void assign(OfDouble src) {
            assign(src, 0, 0, Math.min(src.length(), this.length()));
        }
    }

    /**
     * Definition of an array of {@code int} values.
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
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        int get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        void set(int index, int value);

        /**
         * Applies a procedure to each (index, value) pair of the receivers.
         *
         * @param consumer the procedure to be applied
         */
        default void forEach(Int2Consumer consumer) {
            for (int i = 0; i < length(); ++i) {
                consumer.accept(i, get(i));
            }
        }

        /**
         * Update all array values using the given function {@code fn}.
         *
         * @param fn the update function
         */
        default void update(Int2ToIntFunction fn) {
            for (int i = 0; i < length(); ++i) {
                set(i, fn.apply(i, get(i)));
            }
        }

        /**
         * Return an int stream from the given array.
         *
         * @return an int stream from the given array
         */
        default IntStream stream() {
            return IntStream.range(0, length()).map(this::get);
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         * @param srcPos starting position in the source array.
         * @param destPos starting position in the destination data
         * @param length the number of array elements to be copied
         * @throws IndexOutOfBoundsException  if copying causes access of data
         *         outside array bounds.
         * @throws NullPointerException if either {@code src} is {@code null}.
         */
        default void assign(OfInt src, int srcPos, int destPos, int length) {
            Objects.checkFromIndexSize(srcPos, length, src.length());
            Objects.checkFromIndexSize(destPos, length, this.length());

            for (int i = 0; i < length; ++i) {
                set(destPos + i, src.get(srcPos + i));
            }
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         */
        default void assign(OfInt src) {
            assign(src, 0, 0, Math.min(src.length(), this.length()));
        }
    }

    /**
     * Definition of an array of {@code long} values.
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
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        long get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        void set(int index, long value);

        /**
         * Applies a procedure to each (index, value) pair of the receivers.
         *
         * @param consumer the procedure to be applied
         */
        default void forEach(IntLongConsumer consumer) {
            for (int i = 0; i < length(); ++i) {
                consumer.accept(i, get(i));
            }
        }

        /**
         * Update all array values using the given function {@code fn}.
         *
         * @param fn the update function
         */
        default void update(IntLongToLongFunction fn) {
            for (int i = 0; i < length(); ++i) {
                set(i, fn.apply(i, get(i)));
            }
        }

        /**
         * Return {@code long}  stream from the given array.
         *
         * @return an {@code long} stream from the given array
         */
        default LongStream stream() {
            return IntStream.range(0, length()).mapToLong(this::get);
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         * @param srcPos starting position in the source array.
         * @param destPos starting position in the destination data
         * @param length the number of array elements to be copied
         * @throws IndexOutOfBoundsException  if copying causes access of data
         *         outside array bounds.
         * @throws NullPointerException if either {@code src} is {@code null}.
         */
        default void assign(OfLong src, int srcPos, int destPos, int length) {
            Objects.checkFromIndexSize(srcPos, length, src.length());
            Objects.checkFromIndexSize(destPos, length, this.length());

            for (int i = 0; i < length; ++i) {
                set(destPos + i, src.get(srcPos + i));
            }
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         */
        default void assign(OfLong src) {
            assign(src, 0, 0, Math.min(src.length(), this.length()));
        }
    }

    /**
     * Definition of an array of objects of type {@code T}.
     *
     * @param <T> the array element type
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
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        T get(int index);

        /**
         * Set the given {@code value} at the given {@code index}.
         *
         * @param index the array index of the new value
         * @param value the value to be set at the given index
         * @throws ArrayIndexOutOfBoundsException if {@code index < 0} or
         *        {@code index > length()}
         */
        void set(int index, T value);

        /**
         * Applies a procedure to each (index, value) pair of the receivers.
         *
         * @param consumer the procedure to be applied
         */
        default void forEach(IntObjectConsumer<? super T> consumer) {
            for (int i = 0; i < length(); ++i) {
                consumer.accept(i, get(i));
            }
        }

        /**
         * Update all map values using the given function {@code fn}.
         *
         * @param fn the update function
         */
        default void update(IntObjectToObjectFunction<T> fn) {
            for (int i = 0; i < length(); ++i) {
                set(i, fn.apply(i, get(i)));
            }
        }

        /**
         * Return a double stream from the given array.
         *
         * @return a double stream from the given array
         */
        default Stream<T> stream() {
            return IntStream.range(0, length()).mapToObj(this::get);
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         * @param srcPos starting position in the source array.
         * @param destPos starting position in the destination data
         * @param length the number of array elements to be copied
         * @throws IndexOutOfBoundsException  if copying causes access of data
         *         outside array bounds.
         * @throws NullPointerException if either {@code src} is {@code null}.
         */
        default void assign(OfObject<? extends T> src, int srcPos, int destPos, int length) {
            Objects.checkFromIndexSize(srcPos, length, src.length());
            Objects.checkFromIndexSize(destPos, length, this.length());

            for (int i = 0; i < length; ++i) {
                set(destPos + i, src.get(srcPos + i));
            }
        }

        /**
         * Assigns the given {@code src} Java array to {@code this} array.
         *
         * @param src the source array.
         */
        default void assign(OfObject<? extends T> src) {
            assign(src, 0, 0, Math.min(src.length(), this.length()));
        }
    }

}
