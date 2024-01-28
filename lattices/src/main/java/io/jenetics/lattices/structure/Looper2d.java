package io.jenetics.lattices.structure;

import java.util.function.IntFunction;

@FunctionalInterface
public interface Looper2d {

    Loop2d loop(Range2d range);

    default Loop2d loop(Extent2d extent) {
        return loop(new Range2d(extent));
    }


    static Looper2d forward(IntFunction<Precedence> precedence) {
        return range -> Looper.forward(range, precedence.apply(range.dimensionality()));
    }

}
