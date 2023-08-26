package io.jenetics.lattices.grid.array;

public record DenseFloatArray(float[] elements, int from, int to)
    implements BaseArray.OfFloat
{

    @Override
    public float get(int index) {
        return elements[index + from];
    }

    @Override
    public void set(int index, float value) {
        elements[index + from] = value;
    }

    @Override
    public int length() {
        return to - from;
    }

}
