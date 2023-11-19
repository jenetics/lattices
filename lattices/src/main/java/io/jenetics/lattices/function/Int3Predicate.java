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
package io.jenetics.lattices.function;

/**
 * Represents a predicate (boolean-valued function) of an (int, int, int)-valued
 * argument.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
@FunctionalInterface
public interface Int3Predicate {

    /**
     * Tests whether the two arguments are treated equals.
     *
     * @param i the first value
     * @param j the second value
     * @param k the third value
     * @return {@code true} if the values are treated as equal, {@code false}
     *         otherwise
     */
    boolean test(int i, int j, int k);

}
