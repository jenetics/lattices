package io.jenetics.lattices.function;

public interface Int2ToDoubleFunction {

    /**
     * Performs the function.
     *
     * @param i the first argument
     * @param j the second argument
     * @return the function value
     */
    double apply(int i, int j);

}
