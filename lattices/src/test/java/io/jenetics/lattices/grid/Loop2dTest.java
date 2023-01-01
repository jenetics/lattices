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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Index2d;
import io.jenetics.lattices.structure.Range2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Loop2dTest {

    record Result(
        int rowStart,
        int colStart,
        int rowEnd,
        int colEnd,
        int count
    ) {}

    @Test(dataProvider = "loops")
    public void loop(final Loop2d loop, final Result result) {
        final var rowStart = new AtomicReference<Integer>();
        final var colStart = new AtomicReference<Integer>();
        final var rowEnd = new AtomicInteger();
        final var colEnd = new AtomicInteger();
        final var count = new AtomicInteger();

        loop.forEach((r, c) -> {
            if (rowStart.get() == null) {
                rowStart.set(r);
            }
            if (colStart.get() == null) {
                colStart.set(c);
            }
            rowEnd.set(r);
            colEnd.set(c);
            count.incrementAndGet();
        });

        assertThat(rowStart.get()).isEqualTo(result.rowStart());
        assertThat(colStart.get()).isEqualTo(result.colStart());
        assertThat(rowEnd.get()).isEqualTo(result.rowEnd());
        assertThat(colEnd.get()).isEqualTo(result.colEnd());
        assertThat(count.get()).isEqualTo(result.count());
    }

    @DataProvider
    public Object[][] loops() {
        return new Object[][] {
            {
                new Loop2dRowFirst(new Extent2d(11, 5)),
                new Result(0, 0, 10, 4, 55)
            },
            {
                new Loop2dRowFirst(new Range2d(new Index2d(2, 2), new Extent2d(11, 5))),
                new Result(2, 2, 12, 6, 55)
            },
            {
                new Loop2dColFirst(new Extent2d(11, 5)),
                new Result(0, 0, 10, 4, 55)
            }
        };
    }

}
