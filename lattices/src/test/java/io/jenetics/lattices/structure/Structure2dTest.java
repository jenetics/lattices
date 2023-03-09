package io.jenetics.lattices.structure;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class Structure2dTest {

    @Test
    public void foo() {
        var structure = new Structure2d(new Extent2d(10, 34));
        var layout = structure.layout();

        final var values = new double[structure.extent().size()];
        values[layout.offset(3, 5)] = Math.PI;
        assertThat(values[layout.offset(3, 5)]).isEqualTo(Math.PI);

        var offset = layout.offset(5, 6);
        System.out.println(layout.index(offset));

        structure = View2d
            .of(new Range2d(new Index2d(2, 3), new Index2d(5, 23)))
            .apply(structure);

        offset = structure.layout().offset(4, 10);
        System.out.println(structure.layout().index(offset));
    }

    record IntAccessor(Layout2d layout) {
        int get(int[] values, int row, int col) {
            return values[layout.offset(row, col)];
        }
        void set(int[] values, int row, int col, int value) {
            values[layout.offset(row, col)] = value;
        }
    }

}
