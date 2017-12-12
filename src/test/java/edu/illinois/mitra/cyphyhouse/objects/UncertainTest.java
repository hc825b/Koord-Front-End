package edu.illinois.mitra.cyphyhouse.objects;

import static org.junit.Assert.*;

import java.util.function.Supplier;

import org.junit.Test;

public class UncertainTest {

	class FakeRNG implements Supplier<Integer> {
		int _i;

		FakeRNG(int i) {
			_i = i;
		}

		public Integer get() {
			_i = _i + 1;
			return _i;
		}
	}

	@Test
	public void testSamplePrimitive() {
		FakeRNG rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(rng);
		int result;

		for (int i = 1; i < 10; ++i) {
			result = X.sample();
			assertTrue(result == i);
		}
	}

	@Test
	public void testSampleUnary() {
		FakeRNG rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(rng);

		X = UncertainWrapper.opNegative(X);
		X = UncertainWrapper.opNegative(X);
		X = UncertainWrapper.opNegative(X);

		int result = X.sample();
		assertTrue("result=" + Integer.toString(result), result == -1);
	}

	@Test
	public void testSampleDAG() {
		FakeRNG x_rng = new FakeRNG(10);
		FakeRNG y_rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(x_rng);
		Uncertain<Integer> Y = new BlackBox<Integer>(y_rng);

		X = UncertainWrapper.opPlus(X, Y);
		X = UncertainWrapper.opPlus(X, Y);

		int result = X.sample();
		// If Y is sampled twice, the implementation of sampling is wrong
		assertTrue(y_rng._i == 1);
		// Note that X_2 = X_1 + Y_0 = X_0 + Y_0 + Y_0 = 11 + 1 + 1 = 13
		assertTrue("result=" + Integer.toString(result), result == 13);

	}
	
	// TODO Test conditionals
}
