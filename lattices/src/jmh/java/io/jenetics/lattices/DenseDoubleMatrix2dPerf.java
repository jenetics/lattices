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
package io.jenetics.lattices;

import static io.jenetics.lattices.testfuxtures.MatrixRandom.next;

import cern.colt.matrix.DoubleMatrix2D;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.matrix.blas.Algebra;
import io.jenetics.lattices.testfuxtures.Colts;

@Warmup(iterations = 1)
@Measurement(iterations = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DenseDoubleMatrix2dPerf {

    @State(Scope.Benchmark)
    public static class Matrices {
        DoubleMatrix2d[] latticesA = new DoubleMatrix2d[5];
        DoubleMatrix2d[] latticesB = new DoubleMatrix2d[5];
        DoubleMatrix2D[] coltA = new DoubleMatrix2D[5];
        DoubleMatrix2D[] coltB = new DoubleMatrix2D[5];

        {
            for (int i = 0; i <= latticesA.length - 4; ++i) {
                final int size = (int)Math.pow(10, i);
                latticesA[i] = next(size, size);
                latticesB[i] = next(size, size);
                coltA[i] = Colts.toColt(latticesA[i]);
                coltB[i] = Colts.toColt(latticesB[i]);
            }
        }
    }


    @Benchmark
    public Object lattices_mul_10(Matrices matrices) {
        return matrices.latticesA[0].mult(
            matrices.latticesB[0], null, 2.3, 3.4, false, true
        );
    }

    @Benchmark
    public Object colt_mul_10(Matrices matrices) {
        return matrices.coltA[0].zMult(
            matrices.coltB[0], null, 2.3, 3.4, false, true
        );
    }

//    @Benchmark
//    public Object lattices_mul_100(Matrices matrices) {
//        return matrices.latticesA[1].mult(
//            matrices.latticesB[1], null, 2.3, 3.4, false, true
//        );
//    }
//
//    @Benchmark
//    public Object colt_mul_100(Matrices matrices) {
//        return matrices.coltA[1].zMult(
//            matrices.coltB[1], null, 2.3, 3.4, false, true
//        );
//    }
//
//    @Benchmark
//    public Object lattices_mul_1000(Matrices matrices) {
//        return matrices.latticesA[2].mult(
//            matrices.latticesB[2], null, 2.3, 3.4, false, true
//        );
//    }
//
//    @Benchmark
//    public Object colt_mul_1000(Matrices matrices) {
//        return matrices.coltA[2].zMult(
//            matrices.coltB[2], null, 2.3, 3.4, false, true
//        );
//    }
//
//    @Benchmark
//    public Object lattices_mul_10000(Matrices matrices) {
//        return matrices.latticesA[3].mult(
//            matrices.latticesB[3], null, 2.3, 3.4, false, true
//        );
//    }
//
//    @Benchmark
//    public Object colt_mul_10000(Matrices matrices) {
//        return matrices.coltA[3].zMult(
//            matrices.coltB[3], null, 2.3, 3.4, false, true
//        );
//    }

    /*
    @Benchmark
    public Object lattices_mul_100000(Matrices matrices) {
        return matrices.latticesA[4].mult(
            matrices.latticesB[4], null, 2.3, 3.4, false, true
        );
    }

    @Benchmark
    public Object colt_mul_100000(Matrices matrices) {
        return matrices.coltA[4].zMult(
            matrices.coltB[4], null, 2.3, 3.4, false, true
        );
    }
     */

//    @Benchmark
//    public Object latticesSolve_10000(Matrices matrices) {
//        return Algebra.solve(matrices.latticesA[3], matrices.latticesB[3]);
//    }
//
//    @Benchmark
//    public Object coltSolve_10000(Matrices matrices) {
//        return cern.colt.matrix.linalg.Algebra.DEFAULT
//            .solve(matrices.coltA[3], matrices.coltB[3]);
//    }

}


/*
# Run complete. Total time: 01:24:55

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                                    Mode  Cnt        Score        Error  Units
DenseDoubleMatrix2dPerf.coltSolve_10000      avgt   35  1104229,491 ± 102973,966  us/op
DenseDoubleMatrix2dPerf.colt_mul_100         avgt   35        1,140 ±      0,008  us/op
DenseDoubleMatrix2dPerf.colt_mul_1000        avgt   35      798,173 ±     26,493  us/op
DenseDoubleMatrix2dPerf.colt_mul_10000       avgt   35   847222,558 ±  20071,989  us/op
DenseDoubleMatrix2dPerf.latticesSolve_10000  avgt   35  1423294,647 ±   9832,563  us/op
DenseDoubleMatrix2dPerf.lattices_mul_100     avgt   35        1,207 ±      0,008  us/op
DenseDoubleMatrix2dPerf.lattices_mul_1000    avgt   35      898,336 ±      5,727  us/op
DenseDoubleMatrix2dPerf.lattices_mul_10000   avgt   35   955297,761 ±  20888,968  us/op
 */
