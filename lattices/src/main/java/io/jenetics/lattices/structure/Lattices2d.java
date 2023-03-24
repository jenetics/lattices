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
 * This class provides structural access to a one-dimensional array.
 *
 * @param structure the defining access structure
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Lattices2d(Structure2d structure) {

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     * @param <T> the lattice element type
     */
    public <T> T get(T[] array, Index2d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     * @param <T> the lattice element type
     */
    public <T> void set(T[] array, Index2d index, T value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public int get(int[] array, Index2d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(int[] array, Index2d index, int value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public long get(long[] array, Index2d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(long[] array, Index2d index, long value) {
        array[structure.layout().offset(index)] = value;
    }

    /**
     * Return the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @return the element at the given {@code index}
     */
    public double get(double[] array, Index2d index) {
        return array[structure.layout().offset(index)];
    }

    /**
     * Set the lattice element at the given index. The elements are stored in
     * the given {@code array}. This method doesn't do any index checking.
     *
     * @param array the array where the elements are stored
     * @param index the lattice index of the returned element
     * @param value the new lattice value at the given {@code index}
     */
    public void set(double[] array, Index2d index, double value) {
        array[structure.layout().offset(index)] = value;
    }

}
