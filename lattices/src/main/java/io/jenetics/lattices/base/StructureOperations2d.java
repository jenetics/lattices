package io.jenetics.lattices.base;

import io.jenetics.lattices.grid.Loop2d;
import io.jenetics.lattices.grid.Loopable2d;
import io.jenetics.lattices.structure.Range2d;
import io.jenetics.lattices.structure.Structured2d;

public interface StructureOperations2d extends Structured2d, Loopable2d {

    /**
     * Return the default looping strategy of this structural, which can be
     * overridden by the implementation, if desired.
     *
     * @return the looping strategy of this structural
     */
    @Override
    default Loop2d loop() {
        return Loop2d.of(new Range2d(extent()));
    }

}
