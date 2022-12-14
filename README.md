![Build Status](https://github.com/jenetics/colt/actions/workflows/gradle.yml/badge.svg)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.jenetics/colt/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22colt%22)
[![Javadoc](https://www.javadoc.io/badge/io.jenetics/colt.svg)](http://www.javadoc.io/doc/io.jenetics/colt)

# Colt

CERN Open Source Libraries for High Performance Scientific and Technical Computing in Java.

Copy of the [Colt](https://dst.lbl.gov/ACSSoftware/colt/) linear algebra library, made compilable with Java 8.  Dependencies have been removed and replaced with the version available now in the JDK.

## Build

Create your own binary version.

    $ ./gradlew jar

# Lattices

In geometry and group theory, a _lattice_ in the real coordinate space R^n is an infinite set of points in this space.

This library allows to map a multidimensional grid onto an one-dimensional array, natively supported by Java.

_Lattices_ is a library for implementing two multidimensional data structures, known as lattice. Implementation of multidimensional data structures.

## Grid

Grids are rectangular latices. It contains methods for iterating and accessing/setting the elements and defining the extent and the iteration order. It defines on how a multidimensional grid-point is mapped to an one-dimensional index of the underlying array element.

## Matrix

Implementation of grids. This package contains the commonly known _matrices_ and linear algebra functionality.


## Design

This Linear Algebra Library has been implemented with some basic concepts in mind.

* A matrix is a multi-dimensional container of elements.
* The elements are stored in a one-dimensional data structure (array).
* The `Matrix` class defines the structure of the container; 

