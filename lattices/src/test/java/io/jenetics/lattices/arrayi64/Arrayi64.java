package io.jenetics.lattices.arrayi64;

public class Arrayi64<T> {

    private final long length;
    private final Object[][] elements;


    public Arrayi64(long length) {
        this.length = length;

        final int rows = (int)(length/Integer.MAX_VALUE);
        final int cols = (int)(length - (long)rows*Integer.MAX_VALUE);

        final Object[][] elements = new Object[rows + 1][];
        for (int i = 0; i < rows; ++i) {
            elements[i] = new Object[Integer.MAX_VALUE];
        }
        elements[rows] = new Object[cols];

        this.elements = elements;
    }

    public long length() {
        return length;
    }

    public void set(long index, T value) {
        final int row = (int)(index/Integer.MAX_VALUE);
        final int col = (int)(index - (long)row*Integer.MAX_VALUE);
        elements[row][col] = value;
    }

    @SuppressWarnings("unchecked")
    public T get(long index) {
        final int row = (int)(index/Integer.MAX_VALUE);
        final int col = (int)(index - (long)row*Integer.MAX_VALUE);
        return (T)elements[row][col];
    }

}
