package io.jenetics.lattices.structure;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class Structure2dTest {

    @Test
    public void foo() {
        final var structure = new Structure2d(new Extent2d(10, 34));
        final var layout = structure.layout();

        final var values = new double[structure.extent().size()];
        values[layout.offset(3, 5)] = Math.PI;
        assertThat(values[layout.offset(3, 5)]).isEqualTo(Math.PI);
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
