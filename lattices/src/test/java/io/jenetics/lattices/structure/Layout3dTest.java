package io.jenetics.lattices.structure;

import org.testng.annotations.Test;

public class Layout3dTest {

    @Test
    public void index() {
        final var layout = new Layout3d(new Extent3d(45, 46, 47));
        final var index = new Index3d(23, 15, 12);
        final var offset = layout.offset(index);

        System.out.println(offset);
        System.out.println(layout.index(offset));
    }

}
