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
package io.jenetics.lattices.serialize;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 */
public class CsvWriterReaderTest {

    /*
    @Test
    public void writeAndRead() throws IOException {
        final var grid = ObjectGrid2d.<String>dense().create(15, 5);
        grid.forEach((r, c) -> grid.set(r, c, r + "::" + c));

        final var out = new ByteArrayOutputStream();
        try (var writer = new CsvWriter(new OutputStreamWriter(out))) {
            writer.write(grid);
        }

        final var in = new ByteArrayInputStream(out.toByteArray());
        try (var reader = new CsvReader(new InputStreamReader(in))) {
            final var rgrid = reader.readStringGrid();
            System.out.println(rgrid);
            assertThat(rgrid).isEqualTo(grid);
        }
    }
     */

}
