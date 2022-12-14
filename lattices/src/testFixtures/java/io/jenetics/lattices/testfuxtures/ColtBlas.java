/*
 * Java Lattice Library (@__identifier__@).
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
package io.jenetics.lattices.testfuxtures;

import static io.jenetics.lattices.testfuxtures.Colts.toColt;
import static io.jenetics.lattices.testfuxtures.Colts.toLinealgebra;

import cern.colt.matrix.linalg.SeqBlas;

import io.jenetics.lattices.blas.Blas;
import io.jenetics.lattices.matrix.DoubleMatrix1d;
import io.jenetics.lattices.matrix.DoubleMatrix2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class ColtBlas implements Blas {

    private static final cern.colt.matrix.linalg.Blas COLT = SeqBlas.seqBlas;

    @Override
    public void dcopy(DoubleMatrix1d x, DoubleMatrix1d y) {
        final var x_colt = toColt(x);
        final var y_colt = toColt(y);

        COLT.dcopy(x_colt, y_colt);

        x.assign(toLinealgebra(x_colt));
        y.assign(toLinealgebra(y_colt));
    }

    @Override
    public void drotg(double a, double b, double[] rotvec) {
        COLT.drotg(a, b, rotvec);
    }

    @Override
    public void drot(DoubleMatrix1d x, DoubleMatrix1d y, double c, double s) {
        final var x_colt = toColt(x);
        final var y_colt = toColt(y);

        COLT.drot(x_colt, y_colt, c, s);

        x.assign(toLinealgebra(x_colt));
        y.assign(toLinealgebra(y_colt));
    }

    @Override
    public void dscal(double alpha, DoubleMatrix1d x) {
        Blas.super.dscal(alpha, x);
    }

    @Override
    public void dswap(DoubleMatrix1d x, DoubleMatrix1d y) {
        Blas.super.dswap(x, y);
    }

    @Override
    public void daxpy(double alpha, DoubleMatrix1d x, DoubleMatrix1d y) {
        Blas.super.daxpy(alpha, x, y);
    }

    @Override
    public double ddot(DoubleMatrix1d x, DoubleMatrix1d y) {
        return Blas.super.ddot(x, y);
    }

    @Override
    public double dnrm2(DoubleMatrix1d x) {
        return Blas.super.dnrm2(x);
    }

    @Override
    public double dasum(DoubleMatrix1d x) {
        return Blas.super.dasum(x);
    }

    @Override
    public int idamax(DoubleMatrix1d x) {
        return Blas.super.idamax(x);
    }

    @Override
    public void dgemv(boolean transposeA, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y) {
        Blas.super.dgemv(transposeA, alpha, A, x, beta, y);
    }

    @Override
    public void dger(double alpha, DoubleMatrix1d x, DoubleMatrix1d y, DoubleMatrix2d A) {
        Blas.super.dger(alpha, x, y, A);
    }

    @Override
    public void dsymv(boolean isUpperTriangular, double alpha, DoubleMatrix2d A, DoubleMatrix1d x, double beta, DoubleMatrix1d y) {
        Blas.super.dsymv(isUpperTriangular, alpha, A, x, beta, y);
    }

    @Override
    public void dtrmv(boolean isUpperTriangular, boolean transposeA, boolean isUnitTriangular, DoubleMatrix2d A, DoubleMatrix1d x) {
        Blas.super.dtrmv(isUpperTriangular, transposeA, isUnitTriangular, A, x);
    }

    @Override
    public void dcopy(DoubleMatrix2d A, DoubleMatrix2d B) {
        Blas.super.dcopy(A, B);
    }

    @Override
    public void dscal(double alpha, DoubleMatrix2d A) {
        Blas.super.dscal(alpha, A);
    }

    @Override
    public void dgemm(boolean transposeA, boolean transposeB, double alpha, DoubleMatrix2d A, DoubleMatrix2d B, double beta, DoubleMatrix2d C) {
        Blas.super.dgemm(transposeA, transposeB, alpha, A, B, beta, C);
    }

    @Override
    public void daxpy(double alpha, DoubleMatrix2d A, DoubleMatrix2d B) {
        Blas.super.daxpy(alpha, A, B);
    }

    @Override
    public void dswap(DoubleMatrix2d A, DoubleMatrix2d B) {
        Blas.super.dswap(A, B);
    }
}
