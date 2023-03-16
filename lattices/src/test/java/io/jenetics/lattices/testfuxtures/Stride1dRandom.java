package io.jenetics.lattices.testfuxtures;

import java.util.random.RandomGenerator;

import io.jenetics.lattices.structure.Range1d;
import io.jenetics.lattices.structure.Stride1d;

public class Stride1dRandom {

    private final RandomGenerator random;

    public Stride1dRandom(RandomGenerator random) {
        this.random = random;
    }

    public Stride1d next(Range1d range) {
        final int start = range.start().value();
        final int bound = range.extent().size() + 1;
        return new Stride1d(random.nextInt(start, bound));
    }

}
