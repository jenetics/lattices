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
package io.jenetics.lattices.grid;

/**
 * 2-d structural mixin interface.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Grid2d extends Loopable2d {

    /**
     * Return the structure for 2-d grid.
     *
     * @return the structure for 2-d grid
     */
    Structure2d structure();

    /**
     * Return the dimension of {@code this} 2-d structures.
     *
     * @return the dimension of {@code this} 2-d structures
     */
    default Extent2d extent() {
        return structure().extent();
    }

    /**
     * Return the defined order of {@code this} 2-d structures.
     *
     * @return the defined order of {@code this} 2-d structures
     */
    default Order2d order() {
        return structure().order();
    }

    /**
     * Return the number of cells of this {@code this} 2-d structures.
     *
     * @return the number of cells of this {@code this} 2-d structures
     */
    default int size() {
        return extent().size();
    }

    /**
     * Return the number of rows of {@code this} structures.
     *
     * @return the number of rows of {@code this} structures
     */
    default int rows() {
        return extent().rows();
    }

    /**
     * Return the number of columns of {@code this} structures.
     *
     * @return the number of columns of {@code this} structures
     */
    default int cols() {
        return extent().cols();
    }

    /**
     * Return the default looping strategy of this structural, which can be
     * overridden by the implementation, if desired.
     *
     * @return the looping strategy of this structural
     */
    default Loop2d loop() {
        return new RowFirst(extent());
    }

}
