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

import static java.util.Objects.requireNonNull;
import static io.jenetics.linealgebra.blas.Algebra.checkRectangular;
import static io.jenetics.linealgebra.blas.Algebra.isNonSingular;

import io.jenetics.linealgebra.function.IntIntConsumer;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;
import io.jenetics.linealgebra.structure.Range1d;

/**
 * Performs in place LU-decomposition.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since !__version__!
 * @version !__version__!
 */
final class LU {

    private final DoubleMatrix2d lu;
    private final int pivsign;
    private final int[] pivot;
    private final boolean singular;

    // Some cached values.
    private int[] work1;

    private LU(
        final DoubleMatrix2d lu,
        final int pivsign,
        final int[] pivot
    ) {
        this.lu = requireNonNull(lu);
        this.pivsign = pivsign;
        this.pivot = requireNonNull(pivot);

        singular = !isNonSingular(lu);
    }

    public void solve(final DoubleMatrix2d B) {
//        final int CUT_OFF = 10;
        checkRectangular(lu);

        int m = lu.rows();
        int n = lu.cols();

        if (m*n == 0) {
            return;
        }

        if (B.rows() != m) {
            throw new IllegalArgumentException(
                "Matrix row dimensions must agree: %s != %s."
                    .formatted(B.rows(), m)
            );
        }
        if (singular) {
            throw new IllegalArgumentException("LU-matrix is singular.");
        }

        // right hand side with pivoting
        permuteRows(B, this.pivot);

        int nx = B.cols();

        //precompute and cache some views to avoid regenerating them time and again
        final DoubleMatrix1d[] Brows = new DoubleMatrix1d[n];
        for (int k = 0; k < n; ++k) {
            Brows[k] = B.rowAt(k);
        }

        // transformations
        //cern.jet.math.Mult div = cern.jet.math.Mult.div(0);
        //cern.jet.math.PlusMult minusMult = cern.jet.math.PlusMult.minusMult(0);

        //cern.colt.list.IntArrayList nonZeroIndexes = new cern.colt.list.IntArrayList(); // sparsity
        DoubleMatrix1d Browk = DoubleMatrix1d.DENSE_FACTORY.newInstance(nx); // blocked row k

        // Solve L*Y = B(piv,:)
        for (int k = 0; k < n; k++) {
            // blocking (make copy of k-th row to localize references)
            Browk.assign(Brows[k]);

            // sparsity detection
//            int maxCardinality = nx / CUT_OFF; // == heuristic depending on speedup
//            Browk.getNonZeros(nonZeroIndexes, null, maxCardinality);
//            int cardinality = nonZeroIndexes.size();
//            boolean sparse = (cardinality < maxCardinality);

            for (int i = k + 1; i < n; i++) {
//                //for (int j = 0; j < nx; j++) B[i][j] -= B[k][j]*LU[i][k];
//                //for (int j = 0; j < nx; j++) B.set(i,j, B.get(i,j) - B.get(k,j)*LU.get(i,k));

                final double multiplier = -lu.get(i, k);
                if (Double.compare(multiplier, 0.0) != 0) {
                    Brows[i].update(Browk, (a, b) -> a + multiplier*b);
                }
//                minusMult.multiplicator = -LU.getQuick(i, k);
//                if (minusMult.multiplicator != 0) {
//                    if (sparse) {
//                        Brows[i].assign(Browk, minusMult, nonZeroIndexes);
//                    } else {

//                        Brows[i].assign(Browk, minusMult);
//                    }
//                }
            }
        }

        // Solve U*B = Y;
        for (int k = n - 1; k >= 0; k--) {
//            // for (int j = 0; j < nx; j++) B[k][j] /= LU[k][k];
//            // for (int j = 0; j < nx; j++) B.set(k,j, B.get(k,j) / LU.get(k,k));
//            div.multiplicator = 1 / LU.getQuick(k, k);
            final double multiplier1 = lu.get(k, k);
            Brows[k].update(a -> a*multiplier1);
//
//            // blocking
//            if (Browk == null) {
//                Browk = cern.colt.matrix.DoubleFactory1D.dense.make(B.columns());
//            }
            Browk.assign(Brows[k]);
//
//            // sparsity detection
//            int maxCardinality = nx / CUT_OFF; // == heuristic depending on speedup
//            Browk.getNonZeros(nonZeroIndexes, null, maxCardinality);
//            int cardinality = nonZeroIndexes.size();
//            boolean sparse = (cardinality < maxCardinality);
//
//            //Browk.getNonZeros(nonZeroIndexes,null);
//            //boolean sparse = nonZeroIndexes.size() < nx/10;
//
            for (int i = 0; i < k; i++) {
//                // for (int j = 0; j < nx; j++) B[i][j] -= B[k][j]*LU[i][k];
//                // for (int j = 0; j < nx; j++) B.set(i,j, B.get(i,j) - B.get(k,j)*LU.get(i,k));
//
//                minusMult.multiplicator = -LU.getQuick(i, k);
//                if (minusMult.multiplicator != 0) {
//                    if (sparse) {
//                        Brows[i].assign(Browk, minusMult, nonZeroIndexes);
//                    } else {
//                        Brows[i].assign(Browk, minusMult);
//                    }
//                }
                final double multiplier2 = -lu.get(i, k);
                if (Double.compare(multiplier2, 0.0) != 0) {
                    Brows[i].update(Browk, (a, b) -> a + multiplier2*b);
                }
            }
        }
    }

    private static void permuteRows(final DoubleMatrix2d matrix, final int[] indexes) {
        if (indexes.length != matrix.rows()) {
            throw new IndexOutOfBoundsException("invalid permutation");
        }

        permute((a, b) -> matrix.rowAt(a).swap(matrix.rowAt(b)), indexes);
    }

    private static void
    permute(final IntIntConsumer swapper, final int[] indexes) {
        int s = indexes.length;

        final int[] tracks = new int[s];
        for (int i = s; --i >= 0; ) {
            tracks[i] = i;
        }
        final int[] pos = tracks.clone();

        for (int i = 0; i < s; ++i) {
            final int index = indexes[i];
            final int track = tracks[index];

            if (i != track) {
                swapper.accept(i, track);
                tracks[index] = i;
                tracks[pos[i]] = track;

                final int tmp = pos[i];
                pos[i] = pos[track];
                pos[track] = tmp;
            }
        }
    }

    /**
     * Performs an in-place LU-decomposition of the given {@code matrix}.
     *
     * @param matrix the matrix to be decomposed
     */
    static LU decompose(final DoubleMatrix2d matrix) {
        requireNonNull(matrix);

        final int m = matrix.rows();
        final int n = matrix.cols();

        int pivsign = 1;
        final int[] piv = new int[m];
        for (int i = 0; i < m; ++i) {
            piv[i] = i;
        }

        if (m*n == 0) {
            return new LU(matrix, pivsign, piv);
        }

        final var rows = new DoubleMatrix1d[m];
        for (int i = 0; i < m; ++i) {
            rows[i] = matrix.rowAt(i);
        }

        final DoubleMatrix1d colj = matrix.columnAt(0).like();
        for (int j = 0; j < n; ++j) {
            colj.assign(matrix.columnAt(j));

            // Apply previous transformations.
            for (int i = 0; i < m; ++i) {
                int kmax = Math.min(i, j);
                double s = rows[i].dotProduct(colj, 0, kmax);
                double before = colj.get(i);
                double after = before - s;

                colj.set(i, after);
                matrix.set(i, j, after);
            }

            // Find pivot and exchange if necessary.
            int p = j;
            if (p < m) {
                double max = Math.abs(colj.get(p));
                for (int i = j + 1; i < m; ++i) {
                    double v = Math.abs(colj.get(i));
                    if (v > max) {
                        p = i;
                        max = v;
                    }
                }
            }

            if (p != j) {
                rows[p].swap(rows[j]);

                int k = piv[p];
                piv[p] = piv[j];
                piv[j] = k;
                pivsign = -pivsign;
            }

            final double jj = matrix.get(j, j);
            if (j < m && Double.compare(jj, 0.0) != 0) {
                final var multiplier = 1.0/jj;
                matrix
                    .columnAt(j)
                    .view(new Range1d(j + 1, m - (j + 1)))
                    .update(v -> v*multiplier);
            }
        }

        return new LU(matrix, pivsign, piv);
    }

}
