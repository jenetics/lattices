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
package io.jenetics.lattices.grid.lattice;

import io.jenetics.lattices.structure.Range2d;

/**
 * This interface implements operations for 2-d structures.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
interface Structure2dOps extends Structured2d, Loopable2d {

    /**
     * Return the default looping strategy of this structural, which can be
     * overridden by the implementation, if desired.
     *
     * @return the looping strategy of this structural
     */
    @Override
    default Loop2d loop() {
        return Loop2d.of(new Range2d(extent()));
    }

}
