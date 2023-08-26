package io.jenetics.lattices.grid.array;

public interface BaseArray {

    /**
     * Return the size of {@code this} array.
     *
     * @return the size of {@code this} array
     */
    int length();

    interface OfFloat extends BaseArray {
        float get(int index);
        void set(int index, float value);
    }

    interface Copier<A extends BaseArray> {

        /**
         * Copies the specified range of the specified array into a new array.
         * The initial index of the range ({@code from}) must lie between zero
         * and {@code original.length}, inclusive. The length of the returned
         * array will be {@code to - from}. The resulting array is of exactly
         * the same class as the original array.
         *
         * @param source the array from which a range is to be copied
         * @param from the initial index of the range to be copied, inclusive
         * @param to the final index of the range to be copied, exclusive.
         *        (This index may lie outside the array.)
         * @return a new array containing the specified range from the original
         *         array
         * @throws ArrayIndexOutOfBoundsException if {@code from < 0}
         *     or {@code from > original.length}
         * @throws IllegalArgumentException if {@code from > to}
         * @throws NullPointerException if {@code original} is null
         */
        A copy(A source, int from, int to);

        /**
         * Return a new array of the same type with the given {@code length}.
         *
         * @param length the size of the new array
         * @return a new array of the same type with the given {@code length}
         */
        A like(int length);

    }

}
