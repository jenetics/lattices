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

import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Index1d;
import io.jenetics.lattices.structure.Range1d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class Loop1dTest {

    record Result(int start, int end, int count) {}

    @Test(dataProvider = "loops")
    public void loop(final Loop1d loop, final Result result) {
        final var start = new AtomicReference<Integer>();
        final var end = new AtomicInteger();
        final var count = new AtomicInteger();

        loop.forEach(i -> {
            if (start.get() == null) {
                start.set(i);
            }
            end.set(i);
            count.incrementAndGet();
        });

        assertThat(start.get()).isEqualTo(result.start());
        assertThat(end.get()).isEqualTo(result.end());
        assertThat(count.get()).isEqualTo(result.count());
    }

    @DataProvider
    public Object[][] loops() {
        return new Object[][] {
            {
                new Loop1dForward(new Extent1d(11)),
                new Result(0, 10, 11)
            },
            {
                new Loop1dForward(new Range1d(new Index1d(11), new Extent1d(10))),
                new Result(11, 20, 10)
            },
            {
                new Loop1dForward(new Range1d(new Index1d(11), new Index1d(20))),
                new Result(11, 19, 9)
            },
            {
                new Loop1dBackward(new Extent1d(11)),
                new Result(10, 0, 11)
            },
            {
                new Loop1dBackward(new Range1d(new Index1d(11), new Extent1d(10))),
                new Result(20, 11, 10)
            },
            {
                new Loop1dBackward(new Range1d(new Index1d(11), new Index1d(20))),
                new Result(19, 11, 9)
            }
        };
    }


}
