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
package io.jenetics.lattices.matrix;

import io.jenetics.lattices.grid.array.DenseDoubleArray;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
final class DenseDoubleMatrix2dMult {

    private static final int BLOCK_SIZE = 30_000;

    private DenseDoubleMatrix2dMult() {
    }

    static boolean isDense(
        DoubleMatrix2d A,
        DoubleMatrix2d B,
        DoubleMatrix2d C
    ) {
        return
            A.array() instanceof DenseDoubleArray &&
            B.array() instanceof DenseDoubleArray &&
            C.array() instanceof DenseDoubleArray;
    }

    static void denseMult(
        DoubleMatrix2d A,
        DoubleMatrix2d B,
        DoubleMatrix2d C,
        double alpha,
        double beta
    ) {
        final int m = A.rows();
        final int n = A.cols();
        final int p = B.cols();

        final double[] A_array = ((DenseDoubleArray)A.array()).elements();
        final double[] B_array = ((DenseDoubleArray)B.array()).elements();
        final double[] C_array = ((DenseDoubleArray)C.array()).elements();

        final int A_col_stride = A.structure().layout().stride().col();
        final int B_col_stride = B.structure().layout().stride().col();
        final int C_col_stride = C.structure().layout().stride().col();

        final int A_row_stride = A.structure().layout().stride().row();
        final int B_row_stride = B.structure().layout().stride().row();
        final int C_row_stride = C.structure().layout().stride().row();

        /*
        A is blocked to hide memory latency
                xxxxxxx B
                xxxxxxx
                xxxxxxx
        A
        xxx     xxxxxxx C
        xxx     xxxxxxx
        ---     -------
        xxx     xxxxxxx
        xxx     xxxxxxx
        ---     -------
        xxx     xxxxxxx
        */

        int m_block = (BLOCK_SIZE - n)/(n + 1);
        if (m_block <= 0) {
            m_block = 1;
        }

        int blocks = m/m_block;
        int rr = 0;
        if (m%m_block != 0) {
            blocks++;
        }

        while (--blocks >= 0) {
            int B_j = B.structure().offset(0, 0);
            int A_index = A.structure().offset(rr, 0);
            int C_j = C.structure().offset(rr, 0);
            rr += m_block;
            if (blocks == 0) {
                m_block += m - rr;
            }

            for (int j = p; --j >= 0;) {
                int A_i = A_index;
                int C_i = C_j;
                for (int i = m_block; --i >= 0;) {
                    int A_k = A_i;
                    int B_k = B_j;
                    double s = 0;

                    A_k -= A_col_stride;
                    B_k -= B_row_stride;

                    for (int k = n%4; --k >= 0;) {
                        s = Math.fma(A_array[A_k += A_col_stride], B_array[B_k += B_row_stride], s);
                    }
                    for (int k = n/4; --k >= 0;) {
                        s += A_array[A_k += A_col_stride] * B_array[B_k += B_row_stride] +
                            A_array[A_k += A_col_stride] * B_array[B_k += B_row_stride] +
                            A_array[A_k += A_col_stride] * B_array[B_k += B_row_stride] +
                            A_array[A_k += A_col_stride] * B_array[B_k += B_row_stride];
                    }

                    C_array[C_i] = Math.fma(alpha, s,  beta*C_array[C_i]);
                    A_i += A_row_stride;
                    C_i += C_row_stride;
                }
                B_j += B_col_stride;
                C_j += C_col_stride;
            }
        }
    }

}
