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

}
