/*
 * Java Lattice Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.lattices.lattice;

import static io.jenetics.lattices.lattice.Lattice2d.DEFAULT_LOOPABLE;

import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Loop2d;

/**
 * Functional operation interface which performs <em>updates</em> on 2-d
 * lattices.
 *
 * @param <L> the lattice type.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
@FunctionalInterface
public interface Op2d<L extends Lattice2d<?>> extends BiConsumer<L, Loop2d> {

    /**
     * Apply {@code this} operation onto the given {@code lattice}. If the
     * operation needs to traverse the lattice, the given {@code loop} can be
     * used iterating through the lattice.
     *
     * @param lattice the lattice to operate on
     * @param loop the default looping strategy for the given lattice
     */
    void accept(L lattice, Loop2d loop);

    default Op2d<L> compose(BiConsumer<? super L, ? super Loop2d> before) {
        return (lattice, loop) -> {
            before.accept(lattice, loop);
            accept(lattice, loop);
        };
    }


    /* *************************************************************************
     * Simple operations.
     * ************************************************************************/

    static Op2d<Lattice2d.OfInt<?>> assign(int value) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, value)
        );
    }

    static Op2d<Lattice2d.OfLong<?>> assign(long value) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, value)
        );
    }

    /**
     *  Return an operation which sets all cells to the given {@code value}.
     *
     * @param value the value assigned to each element.
     * @return an operation which sets all cells to the given {@code value}
     */
    static Op2d<Lattice2d.OfDouble<?>> assign(double value) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, value)
        );
    }

    static Op2d<Lattice2d.OfDouble<?>> assign(double[][] values) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, values[r][c])
        );
    }

    static Op2d<Lattice2d.OfDouble<?>> assign(Lattice2d.OfDouble<?> values) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, values.get(r, c))
        );
    }

    static Op2d<Lattice2d.OfDouble<?>>
    assign(Lattice2d.OfDouble<?> y, DoubleBinaryOperator f) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, f.applyAsDouble(lattice.get(r, c), y.get(r, c)))
        );
    }

    static Op2d<Lattice2d.OfDouble<?>> assign(DoubleUnaryOperator f) {
        return (lattice, loop) -> loop.forEach((r, c) ->
            lattice.set(r, c, f.applyAsDouble(lattice.get(r, c)))
        );
    }




    static void main(String[] args) {
        final var extent = new Extent2d(2, 2);
        final var lattice = new DoubleLattice2d(extent, 1, 2, 3, 4);
        System.out.println(lattice);

        lattice
            .apply(assign(3d))
            .apply(DEFAULT_LOOPABLE, assign(5d))
            .apply((lattice1, loop) -> {});
        System.out.println(lattice);
    }

}
