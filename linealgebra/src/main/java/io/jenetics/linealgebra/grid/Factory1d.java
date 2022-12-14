package io.jenetics.linealgebra.grid;

/**
 * Factory interface for creating 1-d matrices.
 *
 * @param <T> the type created by the factory
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
@FunctionalInterface
public interface Factory1d<T> {

    /**
     * Create a new matrix with the given {@code structure}.
     *
     * @param structure the structure of the new matrix
     * @return a new matrix with the given {@code structure}
     */
    T create(final Structure1d structure);

    /**
     * Create a new matrix with the given {@code dimension} and default
     * <em>order</em>.
     *
     * @param dim the dimension of the created array
     * @return a new matrix with the given {@code dimension}
     */
    default T create(final Extent1d dim) {
        return create(new Structure1d(dim));
    }

    /**
     * Create a new matrix with the given {@code size}.
     *
     * @param size the number of matrix elements
     * @return a new matrix with the given size
     */
    default T create(final int size) {
        return create(new Extent1d(size));
    }
}
