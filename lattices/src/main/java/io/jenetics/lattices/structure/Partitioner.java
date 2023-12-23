package io.jenetics.lattices.structure;

import java.util.List;

public interface Partitioner<T> {
    List<T> partition(T value);
}
