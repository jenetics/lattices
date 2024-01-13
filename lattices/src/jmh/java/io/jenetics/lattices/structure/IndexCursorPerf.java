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

import static io.jenetics.lattices.structure.IndexCursor.forward;
import static io.jenetics.lattices.structure.IndexCursor.iterable;
import static io.jenetics.lattices.structure.Precedence.natural;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 1)
@Measurement(iterations = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class IndexCursorPerf {

    @Param({"100"})
    public static int size;

    @Benchmark
    public Object cursor() {
        final var range = Range.of(Extent.of(size, size, size));
        final var cursor = forward(range, natural(range.dimensionality()));
        final var index = new int[cursor.dimensionality()];

        int result = 0;
        while (cursor.next(index)) {
            result += index[0] + index[1] + index[2];
        }

        return result;
    }

    @Benchmark
    public Object iterator() {
        final var range = Range.of(Extent.of(size, size, size));
        final var indexes = iterable(() -> forward(range, natural(range.dimensionality())));

        int result = 0;
        for (var index : indexes) {
            result += index[0] + index[1] + index[2];
        }

        return result;
    }

    @Benchmark
    public Object forLoop() {
        final var range = Range.of(Extent.of(size, size, size));

        int result = 0;
        for (int s = range.start().at(2),
             d = range.start().at(2) + range.extent().at(2);
             s < d; ++s)
        {
            for (int r = range.start().at(1),
                 h = range.start().at(1) + range.extent().at(1);
                 r < h; ++r)
            {
                for (int c = range.start().at(0),
                     w = range.start().at(0) + range.extent().at(0);
                     c < w; ++c)
                {
                    result += s + r + c;
                }
            }
        }

        return result;
    }

}

/*
Benchmark                 (size)  Mode  Cnt     Score     Error  Units
IndexCursorPerf.cursor       100  avgt    5  3635.877 ±  44.229  us/op
IndexCursorPerf.forLoop      100  avgt    5   213.450 ±   4.066  us/op
IndexCursorPerf.iterator     100  avgt    5  7232.854 ± 379.647  us/op
 */

/* Loop unrolling.
Benchmark                 (size)  Mode  Cnt     Score     Error  Units
IndexCursorPerf.cursor       100  avgt    5  1922.832 ±  82.881  us/op
IndexCursorPerf.forLoop      100  avgt    5   215.725 ±  26.294  us/op
IndexCursorPerf.iterator     100  avgt    5  6993.113 ± 329.675  us/op
 */

/* remove array access
Benchmark                 (size)  Mode  Cnt     Score     Error  Units
IndexCursorPerf.cursor       100  avgt    5   825.988 ±  31.530  us/op
IndexCursorPerf.forLoop      100  avgt    5   248.173 ±   1.598  us/op
IndexCursorPerf.iterator     100  avgt    5  6386.453 ± 490.454  us/op
 */
