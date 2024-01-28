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
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public final class TestRanges {

    public static final Range[][] RANGES = new Range[][] {
        { Range.of(Index.of(0), Extent.of(1)) },
        { Range.of(Index.of(0), Extent.of(5)) },
        { Range.of(Index.of(0), Extent.of(17)) },
        { Range.of(Index.of(1), Extent.of(1)) },
        { Range.of(Index.of(2), Extent.of(5)) },
        { Range.of(Index.of(3), Extent.of(17)) },

        { Range.of(Index.of(0, 0), Extent.of(1, 1)) },
        { Range.of(Index.of(0, 0), Extent.of(5, 6)) },
        { Range.of(Index.of(0, 0), Extent.of(7, 6)) },
        { Range.of(Index.of(1, 2), Extent.of(1, 1)) },
        { Range.of(Index.of(1, 2), Extent.of(5, 6)) },
        { Range.of(Index.of(1, 2), Extent.of(7, 6)) },

        { Range.of(Index.of(0, 0, 0), Extent.of(1, 1, 1)) },
        { Range.of(Index.of(0, 0, 0), Extent.of(5, 6, 7)) },
        { Range.of(Index.of(0, 0, 0), Extent.of(7, 6, 10)) },
        { Range.of(Index.of(1, 2, 3), Extent.of(1, 1, 1)) },
        { Range.of(Index.of(1, 2, 3), Extent.of(5, 6, 7)) },
        { Range.of(Index.of(1, 2, 3), Extent.of(7, 6, 10)) },

        { Range.of(Index.of(0, 0, 0, 0), Extent.of(1, 1, 1, 1)) },
        { Range.of(Index.of(0, 0, 0, 0), Extent.of(5, 6, 7, 9)) },
        { Range.of(Index.of(0, 0, 0, 0), Extent.of(7, 6, 10, 9)) },
        { Range.of(Index.of(1, 2, 3, 4), Extent.of(1, 1, 1, 1)) },
        { Range.of(Index.of(1, 2, 3, 4), Extent.of(5, 6, 7, 9)) },
        { Range.of(Index.of(1, 2, 3, 4), Extent.of(7, 6, 10, 9)) },

        { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(1, 1, 1, 1, 1)) },
        { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(5, 6, 7, 9, 17)) },
        { Range.of(Index.of(0, 0, 0, 0, 0), Extent.of(7, 6, 10, 9, 3)) },
        { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(1, 1, 1, 1, 1)) },
        { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(5, 6, 7, 9, 17)) },
        { Range.of(Index.of(1, 2, 3, 4, 5), Extent.of(7, 6, 10, 9, 3)) },

        { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(1, 1, 1, 1, 1, 1)) },
        { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(5, 6, 7, 9, 17, 11)) },
        { Range.of(Index.of(0, 0, 0, 0, 0, 0), Extent.of(7, 6, 10, 9, 3, 7)) },
        { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(1, 1, 1, 1, 1, 1)) },
        { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(5, 6, 7, 9, 17, 11)) },
        { Range.of(Index.of(1, 2, 3, 4, 5, 6), Extent.of(7, 6, 10, 9, 3, 7)) }
    };

    private TestRanges() {
    }
}
