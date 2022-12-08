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
import io.jenetics.linealgebra.structure.Range1d;
import io.jenetics.linealgebra.structure.Range2d;

class LU {

    protected DoubleMatrix2d LU;

    protected int pivsign;

    protected int[] piv;

    protected boolean isNonSingular;

    //protected Algebra algebra;

    transient protected double[] workDouble;
    transient protected int[] work1;
    transient protected int[] work2;

    public void decompose(DoubleMatrix2d A) {
        final int CUT_OFF = 10;

        // Setup
        LU = A;
        final int m = A.rows();
        final int n = A.cols();

        // Setup pivot vector
        if (piv == null || piv.length != m) {
            piv = new int[m];
        }
        for (int i = m; --i >= 0; ) {
            piv[i] = i;
        }
        pivsign = 1;

        if (m*n == 0) {
            setLU(LU);
            return; // nothing to do
        }

        // Precompute and cache some views to avoid regenerating them time and again
        final var LUrows = new DoubleMatrix1d[m];
        for (int i = 0; i < m; i++) {
            LUrows[i] = LU.rowAt(i);
        }

//        cern.colt.list.IntArrayList nonZeroIndexes = new cern.colt.list.IntArrayList(); // sparsity
        DoubleMatrix1d LUcolj = LU.columnAt(0).like();  // blocked column j
//        cern.jet.math.Mult multFunction = cern.jet.math.Mult.mult(0);
//
//        // Outer loop.
        for (int j = 0; j < n; j++) {
//            // blocking (make copy of j-th column to localize references)
//            LUcolj.assign(LU.viewColumn(j));
//
//            // sparsity detection
//            int maxCardinality = m / CUT_OFF; // == heuristic depending on speedup
//            LUcolj.getNonZeros(nonZeroIndexes, null, maxCardinality);
//            int cardinality = nonZeroIndexes.size();
//            boolean sparse = (cardinality < maxCardinality);
//
//            // Apply previous transformations.
            for (int i = 0; i < m; i++) {
                int kmax = Math.min(i, j);
                double s = LUrows[i].dotProduct(LUcolj, 0, kmax);
//                if (sparse) {
//                    s = LUrows[i].zDotProduct(LUcolj, 0, kmax, nonZeroIndexes);
//                } else {
//                    s = LUrows[i].zDotProduct(LUcolj, 0, kmax);
//                }
                double before = LUcolj.get(i);
                double after = before - s;
                LUcolj.set(i, after); // LUcolj is a copy
                LU.set(i, j, after);   // this is the original
//                if (sparse) {
//                    if (before == 0 && after != 0) { // nasty bug fixed!
//                        int pos = nonZeroIndexes.binarySearch(i);
//                        pos = -pos - 1;
//                        nonZeroIndexes.beforeInsert(pos, i);
//                    }
//                    if (before != 0 && after == 0) {
//                        nonZeroIndexes.remove(nonZeroIndexes.binarySearch(i));
//                    }
//                }
            }
//
//            // Find pivot and exchange if necessary.
            int p = j;
            if (p < m) {
                double max = Math.abs(LUcolj.get(p));
                for (int i = j + 1; i < m; i++) {
                    double v = Math.abs(LUcolj.get(i));
                    if (v > max) {
                        p = i;
                        max = v;
                    }
                }
            }
            if (p != j) {
                LUrows[p].swap(LUrows[j]);
                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }
//
//            // Compute multipliers.
            double jj;
            if (j < m && (jj = LU.get(j, j)) != 0.0) {
                final var range = new Range1d(j + 1, m - (j + 1));
                final var multiplier = 1.0/jj;
                LU.columnAt(j).view(range).update(v -> v*multiplier);
            }
//
        }
//        setLU(LU);
    }

    public void setLU(DoubleMatrix2d LU) {
        this.LU = LU;
        //this.isNonSingular = isNonsingular(LU);
    }

    static DoubleMatrix2d lowerTriangular(DoubleMatrix2d A) {
        int rows = A.rows();
        int columns = A.cols();
        int min = Math.min(rows, columns);

        for (int r = min; --r >= 0; ) {
            for (int c = min; --c >= 0; ) {
                if (r < c) {
                    A.set(r, c, 0);
                } else if (r == c) {
                    A.set(r, c, 1);
                }
            }
        }
        if (columns > rows) {
            A.view(new Range2d(0, min, rows, columns - min)).assign(0);
        }

        return A;
    }

}
