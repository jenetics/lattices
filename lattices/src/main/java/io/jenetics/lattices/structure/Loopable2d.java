package io.jenetics.lattices.structure;

@FunctionalInterface
public interface Loopable2d {

    Loop2d loop(Range2d range);

    default Loop2d loop(Extent2d extent, Precedence precedence) {
        return loop(new Range2d(extent));
    }

}
