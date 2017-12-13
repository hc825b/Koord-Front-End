package edu.illinois.mitra.cyphyhouse.objects;

import static org.junit.Assert.*;

import java.util.function.Supplier;

import org.junit.Test;

public class UncertainTest {

	/**
	 * The class keeps increment whenever get() is called
	 * 
	 * @author Chiao Hsieh
	 *
	 */
	class FakeRNG implements Supplier<Integer> {
		int _i;

		FakeRNG(int i) {
			_i = i;
		}

		@Override
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

	@Test
	public void testSampleBoolean() {
		FakeRNG x_rng = new FakeRNG(10);
		FakeRNG y_rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(x_rng);
		Uncertain<Integer> Y = new BlackBox<Integer>(y_rng);

		Uncertain<Boolean> cond = UncertainWrapper.opGT(X, Y);
		cond = UncertainWrapper.opAnd(cond, UncertainWrapper.opGEQ(X, Y));
		cond = UncertainWrapper.opAnd(cond, UncertainWrapper.opLT(Y, X));
		cond = UncertainWrapper.opAnd(cond, UncertainWrapper.opLEQ(Y, X));

		for (int i = 1; i < 10; ++i) {
			boolean result = cond.sample();
			assertTrue(result == true);
		}
	}

	@Test
	public void testSPRT() {
		Supplier<Boolean> getObserver = () -> (Math.random() < 0.97); // bernoulli(0.97)

		Uncertain.Utils.SPRT sprt = new Uncertain.Utils.SPRT(getObserver, 0.90F, 0.95F, 0.99F, 0.01F);

		assertTrue(sprt.WaldTest());
	}

	@Test
	public void testConditionalAcceptFloat() {
		Supplier<Float> getObserver = () -> ((float) Math.random());

		Uncertain<Float> X = new BlackBox<Float>(getObserver);

		Uncertain<Boolean> cond = UncertainWrapper.opLT(X, 0.97F);

		assertTrue(UncertainWrapper.conditional(cond, 0.95F));
	}

	@Test
	public void testConditionalAcceptInterger() {
		FakeRNG x_rng = new FakeRNG(1);
		FakeRNG y_rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(x_rng);
		Uncertain<Integer> Y = new BlackBox<Integer>(y_rng);

		Uncertain<Boolean> cond = UncertainWrapper.opGT(X, Y);

		assertTrue(UncertainWrapper.conditional(cond));
	}

	@Test
	public void testConditionalReject() {
		FakeRNG x_rng = new FakeRNG(1);
		FakeRNG y_rng = new FakeRNG(0);

		Uncertain<Integer> X = new BlackBox<Integer>(x_rng);
		Uncertain<Integer> Y = new BlackBox<Integer>(y_rng);

		Uncertain<Boolean> cond = UncertainWrapper.opLT(X, Y);

		assertFalse(UncertainWrapper.conditional(cond));
	}

	// TODO Achieve higher coverage by testing all operators and different scenarios
}
