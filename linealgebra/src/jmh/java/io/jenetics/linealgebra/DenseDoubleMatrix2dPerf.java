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
package io.jenetics.linealgebra;

import cern.colt.matrix.DoubleMatrix2D;

import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Extent2d;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DenseDoubleMatrix2dPerf {

    private DoubleMatrix2d linealgebraA;
    private DoubleMatrix2d linealgebraB;
    private DoubleMatrix2D coltA;
    private DoubleMatrix2D coltB;

    @Setup
    public void setup() {
        final var random = new DenseDoubleMatrix2dRandom(RandomGenerator.getDefault());
        final var dimension = new Extent2d(10, 10);

        linealgebraA = random.next(dimension);
        linealgebraB = random.next(dimension);
        coltA = ColtMatrices.of(linealgebraA);
        coltB = ColtMatrices.of(linealgebraB);
    }

    @Benchmark
    public Object linealgebraMult() {
        return linealgebraA.mult(linealgebraB, null, 2, 3, false, false);
    }

    @Benchmark
    public Object coltMult() {
        return coltA.zMult(coltB, null, 2, 3, false, false);
    }

}

/*
Benchmark                                Mode  Cnt  Score   Error  Units
DenseDoubleMatrix2dPerf.coltMult         avgt   25  1,225 ± 0,021  us/op
DenseDoubleMatrix2dPerf.linealgebraMult  avgt   25  1,328 ± 0,015  us/op
 */
