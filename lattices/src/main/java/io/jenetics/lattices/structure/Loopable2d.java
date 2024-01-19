package io.jenetics.lattices.structure;

import java.util.function.IntFunction;

@FunctionalInterface
public interface Loopable2d {

    Loop2d in(Range2d range);

    default Loop2d in(Extent2d extent) {
        return in(new Range2d(extent));
    }

    static Loopable2d forward(IntFunction<Precedence> precedence) {
        return range -> Looper.forward(range, precedence.apply(range.dimensionality()));
    }

    static Loopable2d backward(IntFunction<Precedence> precedence) {
        return range -> Looper.backward(range, precedence.apply(range.dimensionality()));
    }

}
