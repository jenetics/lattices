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

import cern.colt.matrix.DoubleMatrix2D;

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

import io.jenetics.lattices.matrix.DoubleMatrix2d;
import io.jenetics.lattices.matrix.linalg.Algebra;

@Warmup(iterations = 3)
@Measurement(iterations = 7)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class DenseDoubleMatrix2dPerf {

    @Param({"10", "100", "1000", "5000"})
    public static int size;

    @State(Scope.Benchmark)
    public static class Matrices {
        DoubleMatrix2d latticesA = MatrixRandom.nextDoubleMatrix2d(size, size);
        DoubleMatrix2d latticesB = MatrixRandom.nextDoubleMatrix2d(size, size);
        DoubleMatrix2D coltA = Colts.toColt(latticesA);
        DoubleMatrix2D coltB = Colts.toColt(latticesB);
    }


    @Benchmark
    public Object lattices_mult(Matrices matrices) {
        return matrices.latticesA.mult(
            matrices.latticesB, null, 2.3, 3.4, false, true
        );
    }

    @Benchmark
    public Object colt_mult(Matrices matrices) {
        return matrices.coltA.zMult(
            matrices.coltB, null, 2.3, 3.4, false, true
        );
    }

    @Benchmark
    public Object latticesSolve(Matrices matrices) {
        return Algebra.solve(matrices.latticesA, matrices.latticesB);
    }

    @Benchmark
    public Object coltSolve(Matrices matrices) {
        return cern.colt.matrix.linalg.Algebra.DEFAULT
            .solve(matrices.coltA, matrices.coltB);
    }

}

/*
2023.04.20: AMD Ryzen 9 6900HX with Radeon Graphics
# Run complete. Total time: 06:49:41

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark                              (size)  Mode  Cnt          Score       Error  Units
DenseDoubleMatrix2dPerf.coltSolve          10  avgt   35          4.150 ±       0.045  us/op
DenseDoubleMatrix2dPerf.coltSolve         100  avgt   35       1046.594 ±     155.752  us/op
DenseDoubleMatrix2dPerf.coltSolve        1000  avgt   35     586896.972 ±    1226.371  us/op
DenseDoubleMatrix2dPerf.coltSolve        5000  avgt   35  126671442.380 ± 4076229.379  us/op
DenseDoubleMatrix2dPerf.colt_mult          10  avgt   35          0.709 ±       0.015  us/op
DenseDoubleMatrix2dPerf.colt_mult         100  avgt   35        378.015 ±       8.594  us/op
DenseDoubleMatrix2dPerf.colt_mult        1000  avgt   35     330048.586 ±     379.302  us/op
DenseDoubleMatrix2dPerf.colt_mult        5000  avgt   35   53925701.172 ± 3435970.365  us/op
DenseDoubleMatrix2dPerf.latticesSolve      10  avgt   35          7.951 ±       0.484  us/op
DenseDoubleMatrix2dPerf.latticesSolve     100  avgt   35       1159.098 ±      24.599  us/op
DenseDoubleMatrix2dPerf.latticesSolve    1000  avgt   35     878433.700 ±    6961.658  us/op
DenseDoubleMatrix2dPerf.latticesSolve    5000  avgt   35  136369539.169 ±  579327.266  us/op
DenseDoubleMatrix2dPerf.lattices_mult      10  avgt   35          0.630 ±       0.001  us/op
DenseDoubleMatrix2dPerf.lattices_mult     100  avgt   35        387.917 ±       2.299  us/op
DenseDoubleMatrix2dPerf.lattices_mult    1000  avgt   35     389612.837 ±     423.564  us/op
DenseDoubleMatrix2dPerf.lattices_mult    5000  avgt   35   52829619.097 ±   76934.165  us/op
 */

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

/*
Dell
# Run complete. Total time: 04:12:57

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                              (size)  Mode  Cnt          Score         Error  Units
DenseDoubleMatrix2dPerf.coltSolve          10  avgt   20          2.310 ±       0.029  us/op
DenseDoubleMatrix2dPerf.coltSolve         100  avgt   20        767.252 ±       7.778  us/op
DenseDoubleMatrix2dPerf.coltSolve        1000  avgt   20     685306.808 ±    3873.094  us/op
DenseDoubleMatrix2dPerf.coltSolve        5000  avgt   20  113501978.682 ± 2780211.335  us/op
DenseDoubleMatrix2dPerf.colt_mult          10  avgt   20          0.762 ±       0.015  us/op
DenseDoubleMatrix2dPerf.colt_mult         100  avgt   20        624.214 ±       3.463  us/op
DenseDoubleMatrix2dPerf.colt_mult        1000  avgt   20     574483.679 ±   43703.373  us/op
DenseDoubleMatrix2dPerf.colt_mult        5000  avgt   20   63948852.493 ±  122083.286  us/op
DenseDoubleMatrix2dPerf.latticesSolve      10  avgt   20          3.487 ±       0.135  us/op
DenseDoubleMatrix2dPerf.latticesSolve     100  avgt   20       1085.257 ±       7.587  us/op
DenseDoubleMatrix2dPerf.latticesSolve    1000  avgt   20    1124746.059 ±    6299.722  us/op
DenseDoubleMatrix2dPerf.latticesSolve    5000  avgt   20  137793060.425 ± 1203070.965  us/op
DenseDoubleMatrix2dPerf.lattices_mult      10  avgt   20          0.790 ±       0.009  us/op
DenseDoubleMatrix2dPerf.lattices_mult     100  avgt   20        510.138 ±      17.419  us/op
DenseDoubleMatrix2dPerf.lattices_mult    1000  avgt   20     476460.970 ±    2442.226  us/op
DenseDoubleMatrix2dPerf.lattices_mult    5000  avgt   20   63955552.722 ±  132984.221  us/op
 */
