package edu.illinois.mitra.cyphyhouse.objects;

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
		// TODO
		return null;
	}

	static public <N extends Number> Uncertain<N> opPlus(N lhs, Uncertain<N> rhs) {
		// TODO
		return null;
	}

	static public int opMinus(int lhs, int rhs) {
		return lhs - rhs;
	}

	static public float opMinus(float lhs, float rhs) {
		return lhs - rhs;
	}

	static public <N extends Number> Uncertain<N> opMinus(Uncertain<N> lhs, N rhs) {
		// TODO
		return null;
	}

	static public <N extends Number> Uncertain<N> opMinus(N lhs, Uncertain<N> rhs) {
		// TODO
		return null;
	}

	static public int opTimes(int lhs, int rhs) {
		return lhs * rhs;
	}

	static public float opTimes(float lhs, float rhs) {
		return lhs * rhs;
	}

	static public <N extends Number> Uncertain<N> opTimes(Uncertain<N> lhs, N rhs) {
		// TODO
		return null;
	}

	static public <N extends Number> Uncertain<N> opTimes(N lhs, Uncertain<N> rhs) {
		// TODO
		return null;
	}

	static public int opDivBy(int lhs, int rhs) {
		return lhs / rhs;
	}

	static public float opDivBy(float lhs, float rhs) {
		return lhs / rhs;
	}

	static public <N extends Number> Uncertain<N> opDivBy(Uncertain<N> lhs, N rhs) {
		// TODO
		return null;
	}

	static public <N extends Number> Uncertain<N> opDivBy(N lhs, Uncertain<N> rhs) {
		// TODO
		return null;
	}

	static public boolean opGEQ(int lhs, int rhs) {
		return lhs >= rhs;
	}

	static public boolean opGEQ(float lhs, float rhs) {
		return lhs >= rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opGEQ(Uncertain<N> lhs, N rhs) {
		return null; // TODO
	}

	static public <N extends Number> Uncertain<Boolean> opGEQ(N lhs, Uncertain<N> rhs) {
		return null; // TODO
	}
	
	static public boolean opLEQ(int lhs, int rhs) {
		return lhs <= rhs;
	}

	static public boolean opLEQ(float lhs, float rhs) {
		return lhs <= rhs;
	}


	static public <N extends Number> Uncertain<Boolean> opLEQ(Uncertain<N> lhs, N rhs) {
		return null; // TODO
	}

	static public <N extends Number> Uncertain<Boolean> opLEQ(N lhs, Uncertain<N> rhs) {
		return null; // TODO
	}
	
	static public boolean opGT(int lhs, int rhs) {
		return lhs > rhs;
	}

	static public boolean opGT(float lhs, float rhs) {
		return lhs > rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opGT(Uncertain<N> lhs, N rhs) {
		return null; // TODO
	}

	static public <N extends Number> Uncertain<Boolean> opGT(N lhs, Uncertain<N> rhs) {
		return null; // TODO
	}

	static public boolean opLT(int lhs, int rhs) {
		return lhs < rhs;
	}

	static public boolean opLT(float lhs, float rhs) {
		return lhs < rhs;
	}

	static public <N extends Number> Uncertain<Boolean> opLT(Uncertain<N> lhs, N rhs) {
		return null; // TODO
	}

	static public <N extends Number> Uncertain<Boolean> opLT(N lhs, Uncertain<N> rhs) {
		return null; // TODO
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

	static public int newValue(int old_value, int new_value) {
		return new_value;
	}

	static public float newValue(float old_value, float new_value) {
		return new_value;
	}

	static public Uncertain<Integer> newValue(Uncertain<Integer> old_value, int new_value) {
		// TODO call the same constructor for old_value but initialize with new_value
		return null;
	}

	static public Uncertain<Float> newValue(Uncertain<Float> old_value, float new_value) {
		// TODO call the same constructor for old_value but initialize with new_value
		return null;
	}

	static public int getValue(int value) {
		return value;
	}

	static public float getValue(float value) {
		return value;
	}

	static public <T> T getValue(Uncertain<T> value) {
		return value.sample();
	}

	protected UncertainWrapper() {
	}
}
