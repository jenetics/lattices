package io.jenetics.lattices.lattice;

import static java.util.Objects.requireNonNull;

import io.jenetics.lattices.structure.Loop2d;

public class Assign2d {

    private final Loop2d loop;

    public Assign2d(Loop2d loop) {
        this.loop = requireNonNull(loop);
    }

    public void apply(Lattice2d.OfDouble<?> lattice, double value) {
        loop.forEach((r, c) -> lattice.set(r, c, value));
    }

}
