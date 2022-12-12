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

import io.jenetics.linealgebra.grid.Range1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix1d;
import io.jenetics.linealgebra.matrix.DoubleMatrix2d;

final class QR {

    private QR() {
    }

    static QR decompose(final DoubleMatrix2d A) {
        A.requireRectangular();

//        cern.jet.math.Functions F = cern.jet.math.Functions.functions;
//        // Initialize.
        final var QR = A;
        final var m = A.rows();
        final var n = A.cols();
        final var Rdiag = A.colAt(0).like();
//        cern.colt.function.DoubleDoubleFunction hypot = cern.colt.matrix.linalg.Algebra.hypotFunction();
//
//        // precompute and cache some views to avoid regenerating them time and again
        DoubleMatrix1d[] QRcolumns = new DoubleMatrix1d[n];
        DoubleMatrix1d[] QRcolumnsPart = new DoubleMatrix1d[n];
        for (int k = 0; k < n; k++) {
            QRcolumns[k] = QR.colAt(k);
            QRcolumnsPart[k] = QR.colAt(k).view(new Range1d(k, m - k));
        }
//
//        // Main loop.
        for (int k = 0; k < n; k++) {
//            //DoubleMatrix1D QRcolk = QR.viewColumn(k).viewPart(k,m-k);
//            // Compute 2-norm of k-th column without under/overflow.
            double nrm = 0;
//            //if (k<m) nrm = QRcolumnsPart[k].aggregate(hypot,F.identity);
//
            for (int i = k; i < m; i++) { // fixes bug reported by hong.44@osu.edu
                nrm = Math.hypot(nrm, QR.get(i, k));
            }
//
//
//            if (nrm != 0.0) {
//                // Form k-th Householder vector.
//                if (QR.getQuick(k, k) < 0) nrm = -nrm;
//                QRcolumnsPart[k].assign(cern.jet.math.Functions.div(nrm));
//			/*
//			for (int i = k; i < m; i++) {
//			   QR[i][k] /= nrm;
//			}
//			*/
//
//                QR.setQuick(k, k, QR.getQuick(k, k) + 1);
//
//                // Apply transformation to remaining columns.
//                for (int j = k + 1; j < n; j++) {
//                    DoubleMatrix1D QRcolj = QR.viewColumn(j).viewPart(k, m - k);
//                    double s = QRcolumnsPart[k].zDotProduct(QRcolj);
//				/*
//				// fixes bug reported by John Chambers
//				DoubleMatrix1D QRcolj = QR.viewColumn(j).viewPart(k,m-k);
//				double s = QRcolumnsPart[k].zDotProduct(QRcolumns[j]);
//				double s = 0.0;
//				for (int i = k; i < m; i++) {
//				  s += QR[i][k]*QR[i][j];
//				}
//				*/
//                    s = -s / QR.getQuick(k, k);
//                    //QRcolumnsPart[j].assign(QRcolumns[k], F.plusMult(s));
//
//                    for (int i = k; i < m; i++) {
//                        QR.setQuick(i, j, QR.getQuick(i, j) + s * QR.getQuick(i, k));
//                    }
//
//                }
//            }
//            Rdiag.setQuick(k, -nrm);
        }

        return new QR();
    }

    public void solve(final DoubleMatrix2d B) {
    }

}
