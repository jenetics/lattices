package io.jenetics.lattices.grid.array;

import static java.util.Objects.checkFromIndexSize;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Implementation of a <em>dense</em> array of {@code int} values.
 *
 * @param elements the underlying {@code int} element values
 * @param from the index of the first array element (inclusively)
 * @param length the length of the sub-array
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 3.0
 */
public record DenseLongArray(long[] elements, int from, int length)
    implements Array.OfLong, Array.Dense<long[], DenseLongArray>
{

    /**
     * Create a new <em>dense</em> long array with the given values
     *
     * @param elements the underlying {@code long} element values
     * @param from the index of the first array element (inclusively)
     * @param length the length of the sub-array
     * @throws IndexOutOfBoundsException if the given {@code from} value and
     *         {@code length} is out of bounds
     */
    public DenseLongArray {
        requireNonNull(elements);
        checkFromIndexSize(from, length, elements.length);
    }

    /**
     * Create a new <em>dense</em> int array with the given values
     *
     * @param elements the underlying {@code long} element values
     * @param from the index of the first array element (inclusively)
     * @throws IndexOutOfBoundsException if the given {@code from} value is out
     *         of bounds
     */
    public DenseLongArray(long[] elements, int from) {
        this(elements, from, elements.length - from);
    }

    /**
     * Create a new <em>dense</em> array of {@code long} values.
     *
     * @param elements the underlying {@code long} element values
     */
    public DenseLongArray(long[] elements) {
        this(elements, 0, elements.length);
    }

    @Override
    public long get(int index) {
        return elements[index + from];
    }

    @Override
    public void set(int index, long value) {
        elements[index + from] = value;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public DenseLongArray copy() {
        final var elems = Arrays.copyOfRange(elements, from, from + length);
        return new DenseLongArray(elems);
    }

    @Override
    public DenseLongArray copy(int from, int length) {
        final var array = Arrays.copyOfRange(
            elements,
            from + this.from, from + this.from + length
        );
        return new DenseLongArray(array);
    }

    @Override
    public DenseLongArray like(int length) {
        return ofSize(length);
    }

    /**
     * Return {@code long}  stream from the given array.
     *
     * @return an {@code long} stream from the given array
     */
    public LongStream stream() {
        return IntStream.range(0, length())
            .mapToLong(this::get);
    }

    @Override
    public String toString() {
        return stream()
            .mapToObj(Long::toString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * Create a new dense {@code long} array with the given {@code length}.
     *
     * @param length the length of the created array
     * @return a new dense {@code long} array with the given {@code length}
     */
    public static DenseLongArray ofSize(int length) {
        return new DenseLongArray(new long[length]);
    }

}
