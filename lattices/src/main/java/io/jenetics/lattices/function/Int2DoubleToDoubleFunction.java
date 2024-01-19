package io.jenetics.lattices.function;

@FunctionalInterface
public interface Int2DoubleToDoubleFunction {

    /**
     * Performs the function.
     *
     * @param i the first argument
     * @param j the second argument
     * @param v the value argument
     * @return the function value
     */
    double apply(int i, int j, double v);

}
