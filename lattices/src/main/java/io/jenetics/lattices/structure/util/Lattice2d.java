package io.jenetics.lattices.structure.util;

import io.jenetics.lattices.structure.Index2d;

public interface Lattice2d<T> {

    void set(Index2d index, T value);

    T get(Index2d index);

}
