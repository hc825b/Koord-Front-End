package edu.illinois.mitra.cyphyhouse.objects;

/**
 * Uncertain
 *
 */

public abstract class Uncertain<T> {

	/**
	 * The class is a group of static functions for operations on Uncertain<T>
	 * objects
	 * 
	 * @author Chiao Hsieh
	 *
	 */
	public static class Utils {
		static public Uncertain<Boolean> newConstant(boolean b) {
			return new Constant<Boolean>(b);
		}

		static public Uncertain<Integer> newConstant(int n) {
			return new Constant<Integer>(n);
		}

		static public Uncertain<Float> newConstant(float f) {
			return new Constant<Float>(f);
		}

		static public <N extends Number> Uncertain<N> opPlus(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<N>(new PlusDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<N> opNegative(Uncertain<N> value) {
			return new Compound<N>(new NegativeDAG(value._dagRoot));
		}

		static public <N extends Number> Uncertain<N> opMinus(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<N>(new MinusDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<N> opTimes(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<N>(new TimesDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<N> opDivBy(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<N>(new DivByDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<Boolean> opGEQ(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<Boolean>(new GEQDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<Boolean> opLEQ(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<Boolean>(new LEQDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<Boolean> opGT(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<Boolean>(new GTDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public <N extends Number> Uncertain<Boolean> opLT(Uncertain<N> lhs, Uncertain<N> rhs) {
			return new Compound<Boolean>(new LTDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public Uncertain<Boolean> opAnd(Uncertain<Boolean> lhs, Uncertain<Boolean> rhs) {
			return new Compound<Boolean>(new AndDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public Uncertain<Boolean> opOr(Uncertain<Boolean> lhs, Uncertain<Boolean> rhs) {
			return new Compound<Boolean>(new OrDAG(lhs._dagRoot, rhs._dagRoot));
		}

		static public Uncertain<Boolean> opNot(Uncertain<Boolean> value) {
			return new Compound<Boolean>(new NotDAG(value._dagRoot));
		}

		/**
		 * Return probability that a given predicate is True.
		 * 
		 * @param cond
		 *            Given Uncertain< Boolean > predicate
		 * @return The probability that the predicate is True
		 */
		static public Float probability(Uncertain<Boolean> cond) {
			// TODO
			return null;
		}

		static public boolean conditional(Uncertain<Boolean> cond) {
			return conditional(cond, 0.5F);
		}

		static public boolean conditional(Uncertain<Boolean> cond, float threshold) {
			return probability(cond) > threshold;
		}

		/**
		 * Protected constructor to avoid creating any instance by accident but still
		 * allow inheritance for extending static methods
		 */
		protected Utils() {
		}
	}

	/**
	 * Internal class for creating instance from a non-leaf DAG
	 *
	 */
	protected static class Compound<T> extends Uncertain<T> {
		protected Compound(DAG dag) {
			super(dag);
		}

	}

	protected DAG _dagRoot;

	/**
	 * Construct a new instance from a DAG. Only used internally.
	 * 
	 * @param dag
	 */
	protected Uncertain(DAG dag) {
		_dagRoot = dag;
	}

	@SuppressWarnings("unchecked")
	final public T sample() {
		_dagRoot.initDAG(); // Initialize the DAG so that the sample is a new one

		return ((T) _dagRoot.eval()); // XXX Better way to ensure type casting
	}
}

/**
 * Abstract class for primitive distributions
 *
 */
abstract class Primitive<T> extends Uncertain<T> {
	protected Primitive(DAG dag) {
		super(dag);
	}
}

class Constant<T> extends Primitive<T> {
	protected Constant(T value) {
		super(new LeafPointMass<T>(value));
	}
}

// interface IDAGVisitor {
//
// default <T> T traverse(DAG dagRoot) {
// // Initialize color
// dagRoot.initDAG();
// return dagRoot.eval(this);
// }
//
// abstract <T> T traverse(LeafDAG<T> dagRoot);
//
// abstract <T> T traverse(UnaryDAG dagRoot);
//
// abstract <T> T traverse(BinaryDAG dagRoot);
//
// }
//
// class EvalVisitor implements IDAGVisitor {
//
// @Override
// public <T> T traverse(LeafDAG<T> dagRoot) {
// if (dagRoot.isWhite()) {
// dagRoot.markBlack();
// dagRoot.sample();
// return dagRoot.get();
// }
// return null;
// }
//
// @Override
// public <T> T traverse(UnaryDAG dagRoot) {
// // TODO Auto-generated method stub
// return null;
// }
//
// @Override
// public <T> T traverse(BinaryDAG dagRoot) {
// // TODO Auto-generated method stub
// return null;
// }
// }
