package io.jenetics.lattices.serialize;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_BGR;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import io.jenetics.lattices.array.DenseIntArray;
import io.jenetics.lattices.function.Int2Consumer;
import io.jenetics.lattices.lattice.IntLattice2d;
import io.jenetics.lattices.lattice.Lattice2d;
import io.jenetics.lattices.structure.Extent2d;
import io.jenetics.lattices.structure.Structure2d;

public final class Images {
    private Images() {
    }

    public static Lattice2d.OfInt<?> read(final Path path) throws IOException {
        final BufferedImage image = ImageIO.read(path.toFile());

        final var rows = image.getHeight();
        final var cols = image.getWidth();

        // Verbose way for creating the lattice.
        final var extent = new Extent2d(rows, cols);
        final var structure = new Structure2d(extent);
        final var array = DenseIntArray.ofLength(extent.cells());
        final var lattice = new IntLattice2d(structure, array);

        // The short way for creating the lattice.
        //final var lattice = IntLattice2d.DENSE.create(rows, cols);

        lattice.forEach((r, c) -> lattice.set(r, c, image.getRGB(c, r)));
        return lattice;
    }

    private static Lattice2d.OfInt<?> toLattice(final BufferedImage image) {
        final var lattice = IntLattice2d.DENSE.create(image.getHeight(), image.getWidth());
        lattice.forEach((r, c) -> lattice.set(r, c, image.getRGB(c, r)));
        return lattice;
    }

    private static BufferedImage toImage(final Lattice2d.OfInt<?> lattice) {
        final var image = new BufferedImage(lattice.cols(), lattice.rows(), TYPE_INT_ARGB);
        lattice.forEach((r, c) -> image.setRGB(c, r, lattice.get(r, c)));
        return image;
    }


    public static void main(String[] args) throws Exception {
        final var image = ImageIO.read(new File("/home/fwilhelm/Downloads/DSC00270.JPG"));
        final var lattice = toLattice(image);

        lattice.forEach(avg(image.getColorModel(), lattice));
        System.out.println(lattice.structure());

        ImageIO.write(toImage(lattice), "JPG", new File("/home/fwilhelm/Downloads/DSC00270_avg.JPG"));
    }

    private static Int2Consumer avg(final ColorModel model, final Lattice2d.OfInt<?> lattice) {
        return (r, c) -> {
            var rgb = lattice.get(r, c);
            var pixel = new int[4];
            model.getComponents(rgb, pixel, 0);

            lattice.set(r, c, model.getDataElement(new int[]{pixel[0], pixel[1], pixel[1], pixel[1]}, 0));
        };
    }

}
