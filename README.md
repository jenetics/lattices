![Build Status](https://github.com/jenetics/lattices/actions/workflows/gradle.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jenetics/lattices/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22colt%22)
[![Javadoc](https://www.javadoc.io/badge/io.jenetics/lattices.svg)](http://www.javadoc.io/doc/io.jenetics/lattices)

# Lattices

This library offers an object-oriented abstraction for modelling multidimensional _rectangular_ lattices. It is designed to be scalable in terms of performance and memory usage. Its architecture was inspired by the [Colt](https://dst.lbl.gov/ACSSoftware/colt/) library, but the current design and its implementation has been brought into the _modern_ Java world.

## Build the library

**Lattices** requires at least **Java 17** to compile and run.

Check out the master branch from GitHub.

    $ git clone https://github.com/jenetics/lattices.git <builddir>

_Lattices_ uses [Gradle](http://www.gradle.org/downloads) as build system.

## Functionality

In geometry and group theory, a _lattice_ in the real coordinate space R^n is an infinite set of points in this space.

This library allows to map a multidimensional grid onto an one-dimensional array, natively supported by Java.

_Lattices_ is a library for implementing two multidimensional data structures, known as lattice. Implementation of multidimensional data structures.

### Grid

Multi-dimensional arrays are arguably the most frequently used abstraction in scientific and technical codes. They support a broad variety of applications in the domain of Physics, Linear Algebra, Computational Fluid Dynamics, Relational Algebra, Statistics, Graphics Rendering and others. For example many physics problems can be mapped to matrix problems: Linear and nonlinear systems of equations, linear differential equations, quantum mechanical eigenvalue problems, Tensors, etc. Physics NTuples are essentially 2-d arrays. In the area of Online Analytic Processing (OLAP) multi-dimensional arrays are called Data Cubes. In this toolkit they are called Matrices, simply because the term Array is already heavily overloaded and Data Cube is somewhat fuzzy to many people.

Grids are rectangular latices. It contains methods for iterating and accessing/setting the elements and defining the extent and the iteration order. It defines on how a multidimensional grid-point is mapped to an one-dimensional index of the underlying array element.

The main design principle of _grids_ are, that they are multidimensional views on one-dimensional array, as they are natively supported by Java.

### Matrix

This package contains classes for creating _matrices_ and doing basic linear algebra operations.


