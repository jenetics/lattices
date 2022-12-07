package io.jenetics.linealgebra.structure;

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
    T newMatrix(final Structure1d structure);

    /**
     * Create a new matrix with the given {@code dimension} and default
     * <em>order</em>.
     *
     * @param dim the dimension of the created array
     * @return a new matrix with the given {@code dimension}
     */
    default T newMatrix(final Extent1d dim) {
        return newMatrix(new Structure1d(dim));
    }

    /**
     * Create a new matrix with the given {@code size}.
     *
     * @param size the number of matrix elements
     * @return a new matrix with the given size
     */
    default T newMatrix(final int size) {
        return newMatrix(new Extent1d(size));
    }
}
