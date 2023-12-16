package io.jenetics.lattices.structure;

import org.testng.annotations.Test;

public class RangeTest {

    @Test
    public void iterator() {
        final var start = Index.of(new int[4]);
        final var extent = Extent.of(3, 3, 3, 3);
        final var range = Range.of(start, extent);

        int max = 1;
        for (int i = 0; i < extent.dimensionality(); ++i) {
            max *= extent.at(i);
        }
        System.out.println("MAX: " + max);

        int count = 0;
        for (var index : (Iterable<Index>)() -> Range.iterator(range)) {
            if (count > max) {
                break;
            }
            System.out.println(index);
            ++count;
        }
        System.out.println("COUNT: " + count);
    }

    /*
                for (int i = 0; i < range.dimensionality(); ++i) {
                    cursors[i] = crs[i] + 1;

                    if (cursors[i] >= limits[i] && i < range.dimensionality() - 1) {
                        for (int j = 0; j <= i; ++j) {
                            cursors[j] = range.start().at(j);
                            if (j + 1 < range.dimensionality()) {
                                cursors[j + 1] = limits[j + 1] + 1;
                            }
                        }
                    } else {
                        break;
                    }
                }
     */

    /*
                for (int i = range.dimensionality(); --i >= 0;) {
                    cursors[i] = crs[i] + 1;

                    if (cursors[i] >= limits[i] && i != 0) {
                        for (int j = i + 1; --j >= 0;) {
                            cursors[j] = range.start().at(j);
                        }
                    } else {
                        break;
                    }
                }
     */

}
