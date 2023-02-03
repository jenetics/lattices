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

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Interface for reading CSV lines from a given input stream.
 *
 * <pre>{@code
 * // The opened Stream must be closed after usage.
 * try (Stream<String> lines = CSV.LINE_READER.read(Path.of("file.csv"))) {
 *     lines.forEach(System.out::println);
 * }
 * }</pre>
 *
 * @see CsvSupport#read(Reader)
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 3.0
 * @since 3.0
 */
@FunctionalInterface
interface CsvLineReader {

    /**
     * Splits the given content of the given {@code reader} into a
     * {@code Stream} of CSV lines. The lines are split at line breaks, as long
     * as they are not part of a quoted column. <em>The returned stream must be
     * closed by the caller, which also closes the given reader.</em>
     *
     * <pre>{@code
     * try (var lines = CSV.LINE_READER.read(reader)) {
     *     lines.forEach(System.out::println);
     * }
     * }</pre>
     *
     * @param reader the reader stream to split into CSV lines
     * @return the stream of CSV lines
     * @see #read(Path)
     */
    Stream<String> read(final Reader reader);

    /**
     * Splits the given {@code path} into a {@code Stream} of CSV lines. The
     * lines are split at line breaks, as long as they are not part of a quoted
     * column. <em>The returned stream must be closed by the caller.</em>
     *
     * <pre>{@code
     * try (var lines = CSV.LINE_READER.read(path, UTF_8)) {
     *     lines.forEach(System.out::println);
     * }
     * }</pre>
     *
     * @param path the CSV file to split
     * @param cs the charset to use for decoding
     * @return the stream of CSV lines
     * @throws IOException if an I/O error occurs
     * @see #read(Reader)
     */
    default Stream<String> read(final Path path, final Charset cs) throws IOException {
        return read(Files.newBufferedReader(path, cs));
    }

    /**
     * Splits the given {@code path} into a {@code Stream} of CSV lines. The
     * lines are split at line breaks, as long as they are not part of a quoted
     * column. <em>The returned stream must be closed by the caller.</em>
     *
     * <pre>{@code
     * try (var lines = CSV.LINE_READER.read(path, UTF_8)) {
     *     lines.forEach(System.out::println);
     * }
     * }</pre>
     *
     * @param path the CSV file to split
     * @return the stream of CSV lines
     * @throws IOException if an I/O error occurs
     * @see #read(Reader)
     */
    default Stream<String> read(final Path path) throws IOException {
        return read(path, Charset.defaultCharset());
    }

    /**
     * Reads all CSV lines form the given {@code reader}.
     *
     * @param reader the CSV {@code reader} stream
     * @return all CSV lines form the given {@code reader} stream
     * @throws IOException if an error occurs while reading the CSV lines
     * @see #readAll(Path)
     */
    default List<String> readAll(final Reader reader) throws IOException {
        try (var stream = read(reader)) {
            return stream.collect(Collectors.toList());
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    /**
     * Reads all CSV lines form the given input {@code path}.
     *
     * @param path the CSV file to read
     * @param cs the charset to use for decoding
     * @return all CSV lines form the given {@code input} stream
     * @throws IOException if an error occurs while reading the CSV lines
     * @see #read(Reader)
     */
    default List<String> readAll(final Path path, final Charset cs) throws IOException {
        try (var stream = read(path, cs)) {
            return stream.collect(Collectors.toList());
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    /**
     * Reads all CSV lines form the given input {@code path}.
     *
     * @param path the CSV file to read
     * @return all CSV lines form the given {@code input} stream
     * @throws IOException if an error occurs while reading the CSV lines
     * @see #read(Reader)
     */
    default List<String> readAll(final Path path) throws IOException {
        return readAll(path, Charset.defaultCharset());
    }
}
