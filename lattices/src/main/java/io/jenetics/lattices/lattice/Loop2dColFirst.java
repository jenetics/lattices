package io.jenetics.lattices.lattice;

import static java.util.Objects.requireNonNull;

import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.function.Int2Predicate;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Range2d;

/**
 * Column-major loop implementation. he rows and columns are iterated forward.
 *
 * @param range the range which defines the boundaries of the loop
 */
record Loop2dColFirst(Range2d range) implements Loop2d {

    /**
     * Column-major implementation of the loop strategy
     *
     * @param extent the extent which defines the boundaries of the loop
     */
    Loop2dColFirst(Extent2d extent) {
        this(new Range2d(extent));
    }

    @Override
    public void forEach(final Int2Consumer action) {
        requireNonNull(action);

        for (int c = range.start().col(),
             w = range.start().col() + range.extent().cols();
             c < w; ++c)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                action.accept(r, c);
            }
        }
    }

    @Override
    public boolean anyMatch(Int2Predicate predicate) {
        requireNonNull(predicate);

        for (int c = range.start().col(),
             w = range.start().col() + range.extent().cols();
             c < w; ++c)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                if (predicate.test(r, c)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean allMatch(Int2Predicate predicate) {
        requireNonNull(predicate);

        for (int c = range.start().col(),
             w = range.start().col() + range.extent().cols();
             c < w; ++c)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                if (!predicate.test(r, c)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean nonMatch(Int2Predicate predicate) {
        requireNonNull(predicate);

        for (int c = range.start().col(),
             w = range.start().col() + range.extent().cols();
             c < w; ++c)
        {
            for (int r = range.start().row(),
                 h = range.start().row() + range.extent().rows();
                 r < h; ++r)
            {
                if (predicate.test(r, c)) {
                    return false;
                }
            }
        }

        return true;
    }
}
