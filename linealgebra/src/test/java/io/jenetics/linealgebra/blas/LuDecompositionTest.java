package io.jenetics.linealgebra.blas;

import cern.colt.matrix.linalg.LUDecompositionQuick;
import io.jenetics.linealgebra.ColtMatrices;
import io.jenetics.linealgebra.DenseDoubleMatrix2dRandom;
import io.jenetics.linealgebra.structure.Extent2d;
import org.testng.annotations.Test;

public class LuDecompositionTest {

    @Test
    public void coltDecompose() {
        final var matrix = ColtMatrices.of(
            DenseDoubleMatrix2dRandom
                .nextMatrix(new Extent2d(20, 20))
        );

        final var decomposer = new LUDecompositionQuick();
        decomposer.decompose(matrix);
        final var lu = decomposer.getU();
    }

    //@Test
    public void decompose() {
        final var matrix = DenseDoubleMatrix2dRandom
            .nextMatrix(new Extent2d(20, 20));

        final var decomposer = new LuDecomposition();
        decomposer.decompose(matrix);
        final var lu = decomposer.LU;
    }

}
