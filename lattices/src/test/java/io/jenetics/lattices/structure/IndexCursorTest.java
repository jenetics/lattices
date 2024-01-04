package io.jenetics.lattices.structure;

import java.util.Arrays;

import org.testng.annotations.Test;

public class IndexCursorTest {

    @Test
    public void forward() {
        final var range = Range.of(Index.of(0, 0, 0), Extent.of(2, 2, 2));

        final var forward = IndexCursor.Forward.of(range);
        int count = 0;
        while (forward.hasNext()) {
            System.out.println(Arrays.toString(forward.next()));
            if (count > range.extent().elements()) {
                break;
            }
            ++count;
        }
    }

    @Test
    public void backward() {
        final var range = Range.of(Index.of(0, 0, 0), Extent.of(2, 2, 2));

        final var backward = IndexCursor.Backward.of(range);
        int count = 0;
        while (backward.hasNext()) {
            System.out.println(Arrays.toString(backward.next()));
            if (count > range.extent().elements()) {
                break;
            }
            ++count;
        }
    }

}
