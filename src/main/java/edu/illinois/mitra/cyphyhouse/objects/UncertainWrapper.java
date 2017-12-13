package edu.illinois.mitra.cyphyhouse.objects;

import java.util.function.Supplier;

/**
 * This wrapper class provides overloaded static functions for both primitive
 * type and Uncertain<T> type to simplify the code generation pass
 */
public final class UncertainWrapper extends Uncertain.Utils {

	static public int opPlus(int lhs, int rhs) {
		return lhs + rhs;
	}

	static public float opPlus(float lhs, float rhs) {
		return lhs + rhs;
	}

	static public <N extends Number> Uncertain<N> opPlus(Uncertain<N> lhs, N rhs) {
		return opPlus(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<N> opPlus(N lhs, Uncertain<N> rhs) {
		return opPlus(newConstant(lhs), rhs);
	}

	static public int opMinus(int lhs, int rhs) {
		return lhs - rhs;
	}

	static public float opMinus(float lhs, float rhs) {
		return lhs - rhs;
	}

	static public <N extends Number> Uncertain<N> opMinus(Uncertain<N> lhs, N rhs) {
		return opMinus(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<N> opMinus(N lhs, Uncertain<N> rhs) {
		return opMinus(newConstant(lhs), rhs);
	}

	static public int opTimes(int lhs, int rhs) {
		return lhs * rhs;
	}

	static public float opTimes(float lhs, float rhs) {
		return lhs * rhs;
	}

	static public <N extends Number> Uncertain<N> opTimes(Uncertain<N> lhs, N rhs) {
		return opTimes(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<N> opTimes(N lhs, Uncertain<N> rhs) {
		return opTimes(newConstant(lhs), rhs);
	}

	static public int opDivBy(int lhs, int rhs) {
		return lhs / rhs;
	}

	static public float opDivBy(float lhs, float rhs) {
		return lhs / rhs;
	}

	static public <N extends Number> Uncertain<N> opDivBy(Uncertain<N> lhs, N rhs) {
		return opDivBy(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<N> opDivBy(N lhs, Uncertain<N> rhs) {
		return opDivBy(newConstant(lhs), rhs);
	}

	static public boolean opGEQ(int lhs, int rhs) {
		return lhs >= rhs;
	}

	static public boolean opGEQ(float lhs, float rhs) {
		return lhs >= rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opGEQ(Uncertain<N> lhs, N rhs) {
		return opGEQ(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<Boolean> opGEQ(N lhs, Uncertain<N> rhs) {
		return opGEQ(newConstant(lhs), rhs);
	}

	static public boolean opLEQ(int lhs, int rhs) {
		return lhs <= rhs;
	}

	static public boolean opLEQ(float lhs, float rhs) {
		return lhs <= rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opLEQ(Uncertain<N> lhs, N rhs) {
		return opLEQ(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<Boolean> opLEQ(N lhs, Uncertain<N> rhs) {
		return opLEQ(newConstant(lhs), rhs);
	}

	static public boolean opGT(int lhs, int rhs) {
		return lhs > rhs;
	}

	static public boolean opGT(float lhs, float rhs) {
		return lhs > rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opGT(Uncertain<N> lhs, N rhs) {
		return opGT(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<Boolean> opGT(N lhs, Uncertain<N> rhs) {
		return opGT(newConstant(lhs), rhs);
	}

	static public boolean opLT(int lhs, int rhs) {
		return lhs < rhs;
	}

	static public boolean opLT(float lhs, float rhs) {
		return lhs < rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opLT(Uncertain<N> lhs, N rhs) {
		return opLT(lhs, newConstant(rhs));
	}

	static public <N extends Number> Uncertain<Boolean> opLT(N lhs, Uncertain<N> rhs) {
		return opLT(newConstant(lhs), rhs);
	}

	static public boolean opAnd(boolean lhs, boolean rhs) {
		return lhs && rhs;
	}

	static public boolean opOr(boolean lhs, boolean rhs) {
		return lhs || rhs;
	}

	static public boolean opNot(boolean value) {
		return !value;
	}

	static public boolean conditional(boolean cond) {
		return cond;
	}

	static public <T> T newValue(T old_value, Supplier<T> sampler) {
		return sampler.get();
	}

	/**
	 * Return a new Uncertain object with the same type T. old_value is provided as
	 * a parameter for function overloading
	 */
	static public <T extends Number> Uncertain<T> newValue(Uncertain<T> old_value, Supplier<T> sampler) {
		return new BlackBox<T>(sampler);
	}

	static public int getValue(int value) {
		return value;
	}

	static public float getValue(float value) {
		return value;
	}

	static public <T> T getValue(Uncertain<T> value) {
		return value.sample(); // XXX return expected value rather than a sample?
	}

	protected UncertainWrapper() {
	}
}
