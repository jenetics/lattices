/*
 * Java Linear Algebra Library (@__identifier__@).
 * Copyright (c) @__year__@ Franz Wilhelmstötter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author:
 *    Franz Wilhelmstötter (franz.wilhelmstoetter@gmail.com)
 */
package io.jenetics.linealgebra.blas;

import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

public class ParallelBlas implements Blas {

    @Override
    public double dasum(DoubleMatrix1d x) {
        return 0;
    }

    @Override
    public void daxpy(double alpha, DoubleMatrix1d x, DoubleMatrix1d y) {

    }

    @Override
    public void daxpy(double alpha, DoubleMatrix2d A, DoubleMatrix2d B) {

    }

    @Override
    public void dcopy(DoubleMatrix1d x, DoubleMatrix1d y) {

    }

    @Override
    public void dcopy(DoubleMatrix2d A, DoubleMatrix2d B) {

    }

    @Override
    public double ddot(DoubleMatrix1d x, DoubleMatrix1d y) {
        return 0;
    }

    @Override
    public void dgemm(boolean transposeA, boolean transposeB, double alpha, DoubleMatrix2d A, DoubleMatrix2d B, double beta, DoubleMatrix2d C) {

    }

    @Override
    public void dgemv(boolean transposeA, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y) {

    }

    @Override
    public void dger(double alpha, DoubleMatrix1d x, DoubleMatrix1d y, DoubleMatrix2d A) {

    }

    @Override
    public double dnrm2(DoubleMatrix1d x) {
        return 0;
    }

    @Override
    public void drot(DoubleMatrix1d x, DoubleMatrix1d y, double c, double s) {

    }

    @Override
    public void drotg(double a, double b, double[] rotvec) {

    }

    @Override
    public void dscal(double alpha, DoubleMatrix1d x) {

    }

    @Override
    public void dscal(double alpha, DoubleMatrix2d A) {

    }

    @Override
    public void dswap(DoubleMatrix1d x, DoubleMatrix1d y) {

    }

    @Override
    public void dswap(DoubleMatrix2d A, DoubleMatrix2d B) {

    }

    @Override
    public void dsymv(boolean isUpperTriangular, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y) {

    }

    @Override
    public void dtrmv(boolean isUpperTriangular, boolean transposeA, boolean isUnitTriangular, DoubleMatrix2d A, DoubleMatrix1d x) {

    }

    @Override
    public int idamax(DoubleMatrix1d x) {
        return 0;
    }
}
