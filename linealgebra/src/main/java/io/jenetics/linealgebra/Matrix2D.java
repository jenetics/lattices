package io.jenetics.linealgebra;

/**
 * This interface defines the structure for 2-d matrices holding objects or
 * primitive data types.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
public interface Matrix2D extends Matrix {

    /**
     * The dimension of the {@link Matrix2D} object.
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    record Dimension(int rows, int cols) {
        public Dimension {
            if (rows < 1) {
                throw new IllegalArgumentException(
                    "Number of rows must greater than zero: " + rows
                );
            }
            if (cols < 1) {
                throw new IllegalArgumentException(
                    "Number of columns must greater than zero: " + cols
                );
            }
        }

        public int size() {
            return rows*cols;
        }
    }

    /**
     * Represents the order for accessing the linearly stored matrix data.
     */
    interface Order {

        /**
         * Return the position of the given coordinate within the (virtual or
         * non-virtual) internal 1-dimensional array.
         *
         * @param row the row index
         * @param col the column index
         * @return the (linearized) index of the given {@code row} and {@code col}
         */
        int index(final int row, final int col);

    }

    /**
     * Represents the <em>row-major</em> order.
     *
     * @param rowStride the number of elements between two rows
     * @param colStride the number of elements between two columns
     * @param rowZero the index of the first row element
     * @param colZero the index of the first column element
     */
    record RowMajor(
        int rowStride,
        int colStride,
        int rowZero,
        int colZero
    )
        implements Order
    {
        @Override
        public int index(final int row, final int col) {
            return rowZero + row*rowStride + colZero + col*colStride;
        }
    }

    /**
     * Return the defined order of {@code this} 2-d matrix.
     *
     * @return the defined order of {@code this} 2-d matrix
     */
    Order order();

    /**
     * Return the dimension of {@code this} 2-d matrix.
     *
     * @return the dimension of {@code this} 2-d matrix
     */
    Dimension dimension();

    @Override
    default int size() {
        return dimension().size();
    }

}
