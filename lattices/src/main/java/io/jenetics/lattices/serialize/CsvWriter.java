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

import static java.util.Objects.requireNonNull;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

/**
 * A CSV writer class for serializing 2-d grids/matrices in CSV format.
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public final class CsvWriter implements Closeable, Flushable {
    private final Writer writer;

    public CsvWriter(final Writer writer) {
        this.writer = requireNonNull(writer);
    }

//    public void write(final ObjectGrid2d<?> grid) throws IOException {
//        @SuppressWarnings("unchecked")
//        final var ogrid = (ObjectGrid2d<Object>)grid;
//
//        for (int i = 0; i < ogrid.rows(); ++i) {
//            final var row = ogrid.project(Projection2d.row(i));
//            final var line = CsvSupport.join(row::iterator);
//            writer.write(line);
//            writer.write(CsvSupport.EOL);
//        }
//    }
//
//    public void write(final BaseIntGrid2d<?> grid) throws IOException {
//        for (int i = 0; i < grid.rows(); ++i) {
//            final var row = new ArrayList<Integer>(grid.cols());
//            for (int j = 0; j < grid.cols(); ++j) {
//                row.add(grid.get(i, j));
//            }
//            final var line = CsvSupport.join(row);
//            writer.write(line);
//            writer.write(CsvSupport.EOL);
//        }
//    }
//
//    public void write(final BaseDoubleGrid2d<?> grid) throws IOException {
//        for (int i = 0; i < grid.rows(); ++i) {
//            final var row = new ArrayList<Double>(grid.cols());
//            for (int j = 0; j < grid.cols(); ++j) {
//                row.add(grid.get(i, j));
//            }
//            final var line = CsvSupport.join(row);
//            writer.write(line);
//            writer.write(CsvSupport.EOL);
//        }
//    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
