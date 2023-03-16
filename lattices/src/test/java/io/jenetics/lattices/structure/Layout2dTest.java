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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

import java.util.random.RandomGenerator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.jenetics.lattices.testfuxtures.Index2dRandom;

public class Layout2dTest {

    @Test(dataProvider = "layouts")
    public void indexOffset(Layout2d layout) {
        final var random = new Index2dRandom(RandomGenerator.getDefault());

        var lo = new Layout2d(new Range2d(new Index2d(0, 0), new Extent2d(100,1000)));
        var os = lo.offset(new Index2d(99, 999));
        System.out.println(os);
        var idx = lo.index(os);
        System.out.println(idx);

        final var range = new Range2d(layout.start(), new Extent2d(100,1000));
        for (int i = 0; i < 1000; ++i) {
            final Index2d index = random.next(range);

            final int offset = layout.offset(index);
            assertThat(offset).isGreaterThan(0);
            assertThat(layout.index(offset))
                .withFailMessage("Got %s != expect %s: %s %s"
                    .formatted(layout.index(offset), index, layout.start(), layout.stride()))
                .isEqualTo(index);
        }
    }

    @DataProvider
    public Object[][] layouts() {
        return new Object[][] {
            //{ new Layout2d(new Range2d(new Index2d(0, 0), new Extent2d(100,1000))) }
            { new Layout2d(new Range2d(new Index2d(5, 5), new Extent2d(100,1000))) }
        };
    }

    @Test
    public void foo() {
        final var structure = new Structure2d(new Extent2d(10, 10));
        final var s1 = View2d.of(new Index2d(2, 2)).apply(structure);

        System.out.println(s1.layout().offset(0, 0));
        var offset = s1.layout().offset(8, 8);
        var index = s1.layout().index(offset);
        System.out.println(index);
    }

}
