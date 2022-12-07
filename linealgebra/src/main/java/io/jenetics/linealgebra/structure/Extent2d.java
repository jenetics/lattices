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
package io.jenetics.linealgebra.structure;

import io.jenetics.linealgebra.function.IntIntConsumer;
import io.jenetics.linealgebra.function.IntIntPredicate;

import static java.util.Objects.requireNonNull;

/**
 * The dimension object for two dimension.
 *
 * @param rows the number of rows
 * @param cols the number of columns
 */
public record Extent2d(int rows, int cols) {

    public Extent2d {
        if (rows < 0) {
            throw new IllegalArgumentException(
                "Number of rows must greater or equal than zero: " + rows
            );
        }
        if (cols < 0) {
            throw new IllegalArgumentException(
                "Number of columns must greater or equal than zero: " + cols
            );
        }
    }

    /**
     * The number of matrix elements (cells) a matrix with {@code this}
     * dimensions consists of.
     *
     * @return the number of cells for {@code this} matrix dimension
     */
    public int size() {
        return rows*cols;
    }

    /**
     * Swaps the dimensions of rows and columns.
     *
     * @return a new transposed dimension object
     */
    public Extent2d transpose() {
        return new Extent2d(cols, rows);
    }

    /**
     * Performs an action for each position of {@code this} dimension.
     *
     * @param action an action to perform on the positions
     */
    public void forEach(final IntIntConsumer action) {
        requireNonNull(action);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                action.accept(i, j);
            }
        }
    }

    /**
     * Returns whether any position of this dimension match the provided
     * predicate.  May not evaluate the predicate on all positions if not
     * necessary for determining the result. If the dimension is empty then
     * {@code false} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to elements of this dimension
     * @return {@code true} if any position of the dimension match the
     *         provided predicate, otherwise {@code false}
     */
    public boolean anyMatch(final IntIntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (predicate.test(i, j)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether all positions of {@code this} dimension match the
     * provided {@code predicate}. May not evaluate the predicate on all
     * positions if not necessary for determining the result. If the
     * dimension is empty then {@code true} is returned and the
     * {@code predicate} is not evaluated.
     *
     * @param predicate a non-interfering, stateless predicate to apply to
     *        positions of {@code this} dimension
     * @return {@code true} if either all positions of the dimension match
     *         the provided {@code predicate} or the dimension is empty,
     *         otherwise {@code false}
     */
    public boolean allMatch(final IntIntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (!predicate.test(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns whether no position of this dimension match the provided
     * predicate. May not evaluate the predicate on all positions if not
     * necessary for determining the result.  If the dimension is empty then
     * {@code true} is returned and the predicate is not evaluated.
     *
     * @param predicate predicate to apply to positions of this dimension
     * @return {@code true} if either no position of the dimension match the
     *         provided predicate or the dimension is empty, otherwise
     *         {@code false}
     */
    public boolean nonMatch(final IntIntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (predicate.test(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "[%s x %s]".formatted(rows(), cols());
    }

}
