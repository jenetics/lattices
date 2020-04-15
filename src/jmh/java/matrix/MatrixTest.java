package matrix;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MatrixTest {

	@Param({"1", "100", "10000"})
	int rows;

	@Param({"1", "10", "100"})
	int cols;

	//@Param({"array", "matrix"})
	//String type;

	double[] array;
	double[][] matrix;
	DenseDoubleMatrix2D colt;

	@Setup
	public void setup() {
		array = new double[rows*cols];
		matrix = new double[rows][cols];
		colt = new DenseDoubleMatrix2D(rows, cols);
	}

	@Benchmark
	public void array(final Blackhole bh) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				array[i*cols + j] = 0;
			}
		}

		double sum = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				sum += array[i*cols + j];
			}
		}
		bh.consume(sum);
	}

	@Benchmark
	public void matrix(final Blackhole bh) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				matrix[i][j] = 0;
			}
		}

		double sum = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				sum += matrix[i][j];
			}
		}
		bh.consume(sum);
	}

	@Benchmark
	public void colt(final Blackhole bh) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				colt.set(i, j, 0);
			}
		}

		double sum = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				sum += colt.get(i, j);
			}
		}
		bh.consume(sum);
	}

	@Benchmark
	public void coltQuick(final Blackhole bh) {
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				colt.setQuick(i, j, 0);
			}
		}

		double sum = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				sum += colt.getQuick(i, j);
			}
		}
		bh.consume(sum);
	}

}

/*
Model Name:	MacBook Pro
Model Identifier:	MacBookPro15,1
Processor Name:	Intel Core i9
Processor Speed:	2,4 GHz
Number of Processors:	1
Total Number of Cores:	8
L2 Cache (per Core):	256 KB
L3 Cache:	16 MB
Hyper-Threading Technology:	Enabled
Memory:	32 GB
Boot ROM Version:	220.270.99.0.0 (iBridge: 16.16.6568.0.0,0)

Result "matrix.MatrixTest.matrix":
  1162721,384 ±(99.9%) 21912,623 ns/op [Average]
  (min, avg, max) = (1143672,194, 1162721,384, 1226967,107), stdev = 20497,081
  CI (99.9%): [1140808,761, 1184634,007] (assumes normal distribution)


# Run complete. Total time: 00:18:34

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark             (cols)  (rows)  Mode  Cnt        Score       Error  Units
MatrixTest.array           1       1  avgt   15        7,184 ±     0,168  ns/op
MatrixTest.array           1     100  avgt   15      436,935 ±     7,785  ns/op
MatrixTest.array           1   10000  avgt   15    41873,698 ±  1059,265  ns/op
MatrixTest.array          10       1  avgt   15       12,143 ±     0,232  ns/op
MatrixTest.array          10     100  avgt   15     1380,033 ±    25,542  ns/op
MatrixTest.array          10   10000  avgt   15   135354,746 ±  2273,261  ns/op
MatrixTest.array         100       1  avgt   15      120,410 ±     2,258  ns/op
MatrixTest.array         100     100  avgt   15    12305,549 ±   213,309  ns/op
MatrixTest.array         100   10000  avgt   15  1287750,482 ± 29363,458  ns/op
MatrixTest.colt            1       1  avgt   15        8,222 ±     0,210  ns/op
MatrixTest.colt            1     100  avgt   15      472,436 ±     8,629  ns/op
MatrixTest.colt            1   10000  avgt   15    49597,093 ±  2117,276  ns/op
MatrixTest.colt           10       1  avgt   15       21,102 ±     0,524  ns/op
MatrixTest.colt           10     100  avgt   15     1862,489 ±    54,922  ns/op
MatrixTest.colt           10   10000  avgt   15   175308,469 ±  3750,202  ns/op
MatrixTest.colt          100       1  avgt   15      143,305 ±     2,943  ns/op
MatrixTest.colt          100     100  avgt   15    14257,218 ±   280,990  ns/op
MatrixTest.colt          100   10000  avgt   15  1460453,918 ± 76295,240  ns/op
MatrixTest.coltQuick       1       1  avgt   15        7,505 ±     0,476  ns/op
MatrixTest.coltQuick       1     100  avgt   15      452,025 ±    15,067  ns/op
MatrixTest.coltQuick       1   10000  avgt   15    43457,597 ±   899,545  ns/op
MatrixTest.coltQuick      10       1  avgt   15       17,523 ±     0,481  ns/op
MatrixTest.coltQuick      10     100  avgt   15     1686,180 ±    22,083  ns/op
MatrixTest.coltQuick      10   10000  avgt   15   160392,699 ±  3305,107  ns/op
MatrixTest.coltQuick     100       1  avgt   15      141,330 ±     2,771  ns/op
MatrixTest.coltQuick     100     100  avgt   15    14140,583 ±   271,514  ns/op
MatrixTest.coltQuick     100   10000  avgt   15  1423531,002 ± 30997,874  ns/op
MatrixTest.matrix          1       1  avgt   15        6,929 ±     0,112  ns/op
MatrixTest.matrix          1     100  avgt   15      391,182 ±    11,316  ns/op
MatrixTest.matrix          1   10000  avgt   15    39208,878 ±   798,344  ns/op
MatrixTest.matrix         10       1  avgt   15       12,133 ±     0,290  ns/op
MatrixTest.matrix         10     100  avgt   15     1459,963 ±    28,341  ns/op
MatrixTest.matrix         10   10000  avgt   15   138881,003 ±  2144,326  ns/op
MatrixTest.matrix        100       1  avgt   15       90,618 ±     2,141  ns/op
MatrixTest.matrix        100     100  avgt   15    10931,680 ±   179,036  ns/op
MatrixTest.matrix        100   10000  avgt   15  1162721,384 ± 21912,623  ns/op
 */
