package io.jenetics.lattices.serialize;

import java.util.List;
import java.util.stream.IntStream;

import org.testng.annotations.Test;

import io.jenetics.lattices.grid.DoubleGrid1d;
import io.jenetics.lattices.grid.DoubleGrid2d;
import io.jenetics.lattices.structure.Projection2d;

public class CsvTest {


    @Test
    public void serialize() {
        final var grid = DoubleGrid2d.DENSE.create(10, 5);

        final List<DoubleGrid1d> rows = IntStream.range(0, grid.rows())
            .mapToObj(i -> grid.project(Projection2d.row(i)))
            .toList();

        final DoubleGrid1d line = rows.get(0);
        final List<Double> list = IntStream.range(0, line.size())
            .mapToObj(line::get)
            .toList();
    }

}
