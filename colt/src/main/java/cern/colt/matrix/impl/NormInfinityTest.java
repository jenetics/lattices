/*
 * Copyright © 1999 CERN - European Organization for Nuclear Research.
 *
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that copyright
 * notice and this permission notice appear in supporting documentation. CERN
 * makes no representations about the suitability of this software for any
 * purpose. It is provided "as is" without expressed or implied warranty.
 */
package cern.colt.matrix.impl;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;

class NormInfinityTest {

	public static void main(String[] args) {
		DoubleMatrix1D x1 = DoubleFactory1D.dense
			.make(new double[]{1.0, 2.0});
		DoubleMatrix1D x2 = DoubleFactory1D.dense
			.make(new double[]{1.0, -2.0});
		DoubleMatrix1D x3 = DoubleFactory1D.dense.make(new double[]{-1.0, -2.0});

		System.out.println(Algebra.DEFAULT.normInfinity(x1));
		System.out.println(Algebra.DEFAULT.normInfinity(x2));
		System.out.println(Algebra.DEFAULT.normInfinity(x3));
	}
}
