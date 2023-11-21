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
package io.jenetics.lattices.array;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import io.jenetics.lattices.map.IntDoubleMap;

@Warmup(iterations = 1)
@Measurement(iterations = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class IntDoubleMapPerf {


    @Param({"10"})
    public static int size;

    @State(Scope.Benchmark)
    public static class Data {
        final RandomGenerator random = RandomGenerator.getDefault();
        final int[] keys = new int[size];
        final double[] values = new double[size];

        {
            for (int i = 0; i < size; ++i) {
                keys[i] = random.nextInt(100_000_000);
                values[i] = random.nextDouble();
            }
        }
    }

    @Benchmark
    public Object intDoubleMapPut(Data data) {
        final var map = new IntDoubleMap();
        for (int i = 0; i < data.keys.length; ++i) {
            map.put(data.keys[i], data.values[i]);
        }

        return map;
    }

    @Benchmark
    public Object hashMapPut(Data data) {
        final var map = new HashMap<Integer, Double>();
        for (int i = 0; i < data.keys.length; ++i) {
            map.put(data.keys[i], data.values[i]);
        }

        return map;
    }

    @Benchmark
    public Object eclipseMapPut(Data data) {
        final var map = new IntDoubleHashMap();
        for (int i = 0; i < data.keys.length; ++i) {
            map.put(data.keys[i], data.values[i]);
        }

        return map;
    }

}
