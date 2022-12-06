package io.jenetics.linealgebra;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

public final class ColtMatrices {

    private ColtMatrices() {
    }

    public static DenseDoubleMatrix2D of(final DoubleMatrix2d matrix) {
        final var colt = new DenseDoubleMatrix2D(matrix.rows(), matrix.cols());
        for (int r = matrix.rows(); --r >= 0;) {
            for (int c = matrix.cols(); --c >= 0;) {
                colt.setQuick(r, c, matrix.get(r, c));
            }
        }
        return colt;
    }

}
