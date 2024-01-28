![Build Status](https://github.com/jenetics/lattices/actions/workflows/gradle.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jenetics/lattices/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22lattices%22)
[![Javadoc](https://www.javadoc.io/badge/io.jenetics/lattices.svg)](http://www.javadoc.io/doc/io.jenetics/lattices)

# Lattices

This library offers an object-oriented abstraction for modelling multidimensional _rectangular_ lattices. It is designed to be scalable in terms of performance and memory usage. Its architecture was inspired by the [Colt](https://dst.lbl.gov/ACSSoftware/colt/) library, but the current design and its implementation has been brought into the _modern_ Java world.

The library contains of two modules. The `io.jenetics.lattices.structure` module defines the mapping values of _multi_-dimensional coordinates onto one-dimensional arrays. This module also contains _view_- and _projection_ functions. The main module, `io.jenetics.lattices` contains the _multi_-dimensional data structures and matrix classes for doing linear algebra.

> **Note:** The main design principle of the library is, that everything is a _view_. No data is actually copied for every transformation applied. A copy is only performed, if explicitly requested. Mutability is embraced.

## Build the library

**Lattices** requires at least **Java 21** to compile and run.

Check out the master branch from GitHub.

    $ git clone https://github.com/jenetics/lattices.git <builddir>

_Lattices_ uses [Gradle](http://www.gradle.org/downloads) as build system.

## `io.jenetics.lattices.structure`

This module is used for defining the mappings from _n_-d space to the _1_-d space. It doesn't contain any concrete data structures containing _n_-dimensional points.

```java
// Interface for calculating the array offset for a given 2-d coordinate.
public interface Mapper2d {
    // Return the (array) offset (index) of the given [row, col] point.
    int offset(int row, int col);
    // Return the index point for the given (array) offset.
    Index2d index(int offset);
}
```

The main entry point for creating mapper functions is the `Structure<N>d` class, which can be created the following way.

```java
// Defining the extent of the 2-d structure.
final var extent = new Extent2d(100, 400);

// Creating the 2-d structure.
final var structure = new Structure2d(extent);

// The structure layout.
final var layout = structure.layout();

// Creating the array, which holds the values to be stored.
final var values = new double[extent.cells()];

// Set the value 'Math.PI' at the point [20, 30];
values[layout.offset(20, 30)] = Math.PI;
```

Beside this basic functionality, the _View_ and _Projection_ functions are able to do some basic transformations of the _n_-d data, without copying actual data.

```java
// Create the range of the view to be created.
final var range = new Range2d(
    // Start index of the range.
    new Index2d(5, 5),
    // Extent of the range.
    new Extend2d(50, 50)
);

// Create the view function with the given range.
final var view = View2d.of(range)

// Create a new structure (view) from the previous one.
final var structure2 = view.apply(structure);

// The value Math.PI is now located at position [15, 25], 
// since we we created a view from the original structure, 
// which started at point [5, 5].    
assert values[structure2.layout().offset(15, 25)] == Math.PI;
```

The _view_ functions don't change the dimensionality of the structure, in contrast to the _projection_ functions, which reduces the dimensionality by one.

```java
// Create a projection for row 20.
final var projection = Projection2d.row(20);

// Create a one-dimensional structure.
final Structure1d structure3 = projection.apply(structure);

// At index 25, we can access our stored value.
assert values[structure3.layout().offset(30)] == Math.PI;
```

It is off course possible to create a _projection_ from a structure _view_.

```java
// Create a projection for row 20.
final var projection = Projection2d.row(15);

// Create a one-dimensional structure.
final Structure1d structure4 = projection.apply(structure2);

// At index 25, we can access our stored value.
assert values[structure4.layout().offset(25)] == Math.PI;
```

Or you can compose _projection_ and _view_ functions.

```java
// Create a projection for row 20.
final var projection = Projection2d.row(15)
    .compose(View2d.of(range));

// Create a one-dimensional structure.
final Structure1d structure5 = projection.apply(structure);

// At index 25, we can access our stored value.
assert values[structure5.offset(25)] == Math.PI;
```


## `io.jenetics.lattices.grid`

This module defines _multi_-dimensional data structures and matrix classes for doing linear algebra.

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
final var B = DoubleMatrix2d.of(
    new Extent2d(3, 3),
    10, 11, 12
    13, 14, 15,
    16, 17, 18
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



