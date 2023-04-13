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
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;

import io.jenetics.lattices.grid.array.DenseDoubleArray;
import io.jenetics.lattices.grid.array.DenseIntArray;
import io.jenetics.lattices.grid.array.DenseObjectArray;
import io.jenetics.lattices.grid.DoubleGrid2d;
import io.jenetics.lattices.grid.IntGrid2d;
import io.jenetics.lattices.grid.ObjectGrid2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Structure2d;

/**
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
public final class CsvReader implements Closeable {

    private final Reader reader;

    public CsvReader(final Reader reader) {
        this.reader = requireNonNull(reader);
    }

    public ObjectGrid2d<String> readStringGrid() throws IOException {
        try {
            final var array = new ArrayList<String>();
            final var lines = (Iterable<String>)CsvSupport.read(reader)::iterator;

            int cols = -1;
            int row = 0;
            for (var line : lines) {
                ++row;

                final var parts = CsvSupport.split(line);

                if (cols == -1) {
                    cols = parts.size();
                }
                if (cols != parts.size()) {
                    throw new IOException(
                        ("Invalid number of columns at row %d. Expected %d " +
                            "columns, but got %d.").formatted(row, cols, parts.size())
                    );
                }

                array.addAll(parts);
            }

            return new ObjectGrid2d<>(
                Structure2d.of(new Extent2d(row, cols)),
                new DenseObjectArray<>(array.toArray(String[]::new))
            );
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    public DoubleGrid2d readDoubleGrid() throws IOException {
        final var data = readStringGrid();
        /*
        final var values = new double[data.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Double.parseDouble(data.array().get(i));
        }
         */

        return new DoubleGrid2d(data.structure(), new DenseDoubleArray(null));
    }

    public IntGrid2d readIntGrid() throws IOException {
        final var data = readStringGrid();
        /*
        final var values = new int[data.size()];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Integer.parseInt(data.array().get(i));
        }
         */

        return new IntGrid2d(data.structure(), new DenseIntArray(null));
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
