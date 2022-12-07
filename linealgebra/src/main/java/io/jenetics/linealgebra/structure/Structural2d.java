package io.jenetics.linealgebra.structure;

public interface Structural2d extends Structural {

    /**
     * Return the structure for 2-d structures.
     *
     * @return the structure for 2-d structures
     */
    Structure2d structure();

    /**
     * Return the dimension of {@code this} 2-d matrix.
     *
     * @return the dimension of {@code this} 2-d matrix
     */
    default Extent2d extent() {
        return structure().extent();
    }

    /**
     * Return the defined order of {@code this} 2-d matrix.
     *
     * @return the defined order of {@code this} 2-d matrix
     */
    default Order2d order() {
        return structure().order();
    }

    @Override
    default int size() {
        return extent().size();
    }

    /**
     * Return the number of rows of {@code this} matrix.
     *
     * @return the number of rows of {@code this} matrix
     */
    default int rows() {
        return extent().rows();
    }

    /**
     * Return the number of columns of {@code this} matrix.
     *
     * @return the number of columns of {@code this} matrix
     */
    default int cols() {
        return extent().cols();
    }

}
