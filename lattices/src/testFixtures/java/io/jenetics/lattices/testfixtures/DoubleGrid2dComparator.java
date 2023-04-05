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
package io.jenetics.lattices.testfixtures;

import java.util.Comparator;

import org.assertj.core.util.DoubleComparator;

import io.jenetics.lattices.grid.DoubleGrid2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class DoubleGrid2dComparator implements Comparator<DoubleGrid2d> {

    private final DoubleComparator comparator;

    public DoubleGrid2dComparator(final double precision) {
        this.comparator = new DoubleComparator(precision);
    }

    @Override
    public int compare(final DoubleGrid2d o1, final DoubleGrid2d o2) {
        final var equals = o1.extent().equals(o2.extent()) &&
            o1.allMatch((i, j) -> comparator.compare(o1.get(i, j), o2.get(i, 0)) == 0);

        return equals ? 0 : 1;
    }

    @Override
    public String toString() {
        return String.format("DoubleGrid1dComparator[precision=%s]", comparator.getEpsilon());
    }

}
