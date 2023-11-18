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
package io.jenetics.lattices.lattice;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Defines the looping strategy of a 1-d grid.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public interface Loopable1d extends Loop1d {

    /**
     * Return the default looping strategy.
     *
     * @return the default looping strategy
     */
    Loop1d loop();

    @Override
    default void forEach(IntConsumer action) {
        loop().forEach(action);
    }

    @Override
    default boolean anyMatch(IntPredicate predicate) {
        return loop().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(IntPredicate predicate) {
        return loop().allMatch(predicate);
    }

    @Override
    default boolean nonMatch(IntPredicate predicate) {
        return loop().nonMatch(predicate);
    }

}
