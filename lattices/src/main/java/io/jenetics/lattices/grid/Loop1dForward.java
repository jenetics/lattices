package io.jenetics.lattices.grid;

import static java.util.Objects.requireNonNull;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

import io.jenetics.lattices.structure.Extent1d;
import io.jenetics.lattices.structure.Range1d;

/**
 * Implements a forward loop iteration.
 *
 * @param range the range which defines the boundaries of the loop
 */
record Loop1dForward(Range1d range) implements Loop1d {

    /**
     * Implements a forward loop iteration.
     *
     * @param extent the extent which defines the boundaries of the loop
     */
    Loop1dForward(final Extent1d extent) {
        this(new Range1d(extent));
    }

    @Override
    public void forEach(final IntConsumer action) {
        requireNonNull(action);

        for (int i = range.start().value(),
             n = range.start().value() + range.extent().size(); i < n; ++i)
        {
            action.accept(i);
        }
    }

    @Override
    public boolean anyMatch(final IntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = range.start().value(),
             n = range.start().value() + range.extent().size(); i < n; ++i)
        {
            if (predicate.test(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allMatch(final IntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = range.start().value(),
             n = range.start().value() + range.extent().size(); i < n; ++i)
        {
            if (!predicate.test(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean nonMatch(final IntPredicate predicate) {
        requireNonNull(predicate);

        for (int i = range.start().value(),
             n = range.start().value() + range.extent().size(); i < n; ++i)
        {
            if (predicate.test(i)) {
                return false;
            }
        }
        return true;
    }
}
