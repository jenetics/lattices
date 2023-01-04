![Build Status](https://github.com/jenetics/lattices/actions/workflows/gradle.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jenetics/lattices/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lattices%22)
[![Javadoc](https://www.javadoc.io/badge/io.jenetics/lattices.svg)](http://www.javadoc.io/doc/io.jenetics/lattices)

# Lattices

This library offers an object-oriented abstraction for modelling multidimensional _rectangular_ lattices. It is designed to be scalable in terms of performance and memory usage. Its architecture was inspired by the [Colt](https://dst.lbl.gov/ACSSoftware/colt/) library, but the current design and its implementation has been brought into the _modern_ Java world.

## Build the library

**Lattices** requires at least **Java 17** to compile and run.

Check out the master branch from GitHub.

    $ git clone https://github.com/jenetics/lattices.git <builddir>

_Lattices_ uses [Gradle](http://www.gradle.org/downloads) as build system.

## Functionality

### Grid

_Grids_ are multidimensional views onto one-dimensional arrays. This approach makes the implementation of the _matrices_ in this library very flexible and configurable. It is possible to configure the _structure_ (extent and iteration order) independently of the layout of the underlying array (dense or sparse). How this can be done is shown in the following example.

```java
// Double array, which is created somewhere else.
final var data = new double[10*15];
// Wrap the data into an array. This is just a view, no
// actual data are copied.
final var array = new DenseDoubleArray(data);

// Define the structure (extent) of your 2-d grid.
final var structure = new Structure2d(new Extent2d(10, 15));
// Create the grid with your defined structure and data.
// The grid is a 2-d view onto your one-dimensional double array.
final var grid = new DoubleGrid2d(structure, array);

// Assign each grid element the value 42.
grid.forEach((i, j) -> grid.set(i, j, 42.0));

// The value is written to the underlying double[] array
for (var value : data) {
    assertThat(value).isEqualTo(42.0);
}
```

### Matrix

_Matrices_ are extending _grids_ and share the same design principles. They are also highly configurable and are _just_ multidimensional _views_ onto the underlying one-dimensional arrays. Additionally, they support the usual linear algebra functionality.

```java
// Creating matrix via factory.
final var A = DoubleMatrix2d.DENSE.create(3, 3);
A.assign(new double[][] {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
});

// "Direct" creation from element array. Faster and
// doesn't create additional objects.
final var B = new DoubleMatrix2d(
    new Structure2d(new Extent2d(3, 3)),
    new DenseDoubleMatrix(new double[] {
        10, 11, 12
        13, 14, 15,
        16, 17, 18
    })
);
    
// Create a new matrix with the same extent than B.
var C = B.like();

// Do the matrix multiplication C = A*B.
A.mult(B, C);

// Multiply A*B and let C create.
C = A.mult(B, null);
    
// The result of the multiplication.    
// [[84.0, 90.0, 96.0]
//  [201.0, 216.0, 231.0]
//  [318.0, 342.0, 366.0]]
```

The `Algebra` and the `Blas` class contains additional operations.



