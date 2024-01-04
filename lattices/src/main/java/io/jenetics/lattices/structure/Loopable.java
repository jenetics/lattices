package io.jenetics.lattices.structure;

public interface Loopable {

    Loop loop(Range range);

    default Loop loop(Extent extent) {
        return loop(Range.of(extent));
    }

}
