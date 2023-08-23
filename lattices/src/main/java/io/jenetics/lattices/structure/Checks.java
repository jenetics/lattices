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
package io.jenetics.lattices.structure;

/**
 * Some helper methods for checking pre-conditions.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
final class Checks {
    private Checks() {
    }

    static boolean multNotSave(int a, int b) {
        long r = (long)a*(long)b;
        return (int)r != r;
    }

    static boolean multNotSave(int a, int b, int c) {
        final long r1 = (long)a*(long)b;
        if ((int)r1 == r1) {
            final long r2 = r1*(long)c;
            return (int)r2 != r2;
        } else {
            return true;
        }
    }

    static boolean multNotSave(int a, int b, int c, int d) {
        final long r1 = (long)a*(long)b;
        if ((int)r1 == r1) {
            final long r2 = r1*(long)c;
            if ((int)r2 == r2) {
                final long r3 = r2*(long)d;
                return (int)r3 != r3;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
