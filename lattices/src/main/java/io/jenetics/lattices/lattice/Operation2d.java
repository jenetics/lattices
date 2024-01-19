package io.jenetics.lattices.lattice;

import io.jenetics.lattices.array.BaseArray;
import io.jenetics.lattices.structure.Loop2d;

@FunctionalInterface
public interface Operation2d<L extends Lattice2d<?>> {
    void apply(L lattice);
}

final class AssignDouble implements Operation2d<Lattice2d.OfDouble<?>> {
    private final Loopable2d loop;
    private final double value;

    AssignDouble(Loopable2d loop, double value) {
        this.loop = loop;
        this.value = value;
    }

    @Override
    public void apply(Lattice2d.OfDouble<?> lattice) {
        loop.forEach((r, c) -> lattice.set(r, c, value));
    }

}

