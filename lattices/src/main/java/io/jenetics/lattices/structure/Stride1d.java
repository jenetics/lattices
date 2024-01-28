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
 * Defines a stride.
 *
 * @param value the stride value
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record Stride1d(int value) implements Stride {

    /**
     * A stride instance with stride one.
     */
    public static final Stride1d ONE = new Stride1d(1);

    /**
     * Create a new 1-d stride
     *
     * @param value the stride value
     * @throws IndexOutOfBoundsException if the {@code value} is smaller than
     *         one
     */
    public Stride1d {
        if (value < 1) {
            throw new IndexOutOfBoundsException(
                "Stride must be positive: [%d].".formatted(value)
            );
        }
    }

    /**
     * Return the number of dimensions; always 1.
     *
     * @return 1
     */
    @Override
    public int dimensionality() {
        return 1;
    }

    @Override
    public int at(int dimension) {
        return switch (dimension) {
            case 0 -> value;
            default -> throw new IndexOutOfBoundsException(
                "Dimension out of range [0..%d): %d."
                    .formatted(dimensionality(), dimension)
            );
        };
    }

}
