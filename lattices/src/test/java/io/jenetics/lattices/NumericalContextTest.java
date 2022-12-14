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
package io.jenetics.lattices;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class NumericalContextTest {

    @Test
    public void setDefault() {
        NumericalContext.reset();
        final var devault = NumericalContext.get();
        Assert.assertNotNull(devault);

        NumericalContext.set(new NumericalContext(0.1));
        assertThat(NumericalContext.get()).isNotNull();
        assertThat(NumericalContext.get().epsilon()).isEqualTo(0.1);

        NumericalContext.reset();
        assertThat(NumericalContext.get()).isSameAs(devault);
    }

    @Test
    public void setRandom() {
        final var context = new NumericalContext(0.1);
        NumericalContext.set(context);

        assertThat(NumericalContext.get()).isSameAs(context);
        NumericalContext.reset();
    }

    @Test
    public void setRandomThreading()
        throws ExecutionException, InterruptedException
    {
        final var context = new NumericalContext(0.1);
        NumericalContext.set(context);

        final ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            final var futures = IntStream.range(0, 500)
                .mapToObj(i -> executor
                    .submit(() -> assertThat(NumericalContext.get()).isSameAs(context)))
                .toList();

            for (Future<?> future : futures) {
                future.get();
            }
        } finally {
            executor.shutdown();
            NumericalContext.reset();
        }
    }


    @Test(expectedExceptions = NullPointerException.class)
    public void setNullRandom() {
        NumericalContext.set(null);
    }

    @Test
    public void localContext() {
        final var context = NumericalContext.get();

        final var context1 = new NumericalContext(0.1);
        NumericalContext.using(context1, () -> {
            final var context2 = new NumericalContext(0.2);
            NumericalContext.using(context2, () ->
                assertThat(NumericalContext.get()).isSameAs(context2)
            );

            assertThat(NumericalContext.get()).isSameAs(context1);
        });

        assertThat(NumericalContext.get()).isSameAs(context);
    }

}
