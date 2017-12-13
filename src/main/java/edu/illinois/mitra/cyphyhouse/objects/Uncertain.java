package edu.illinois.mitra.cyphyhouse.objects;

import java.util.function.Supplier;

/**
 * Implementation of Uncertain< T > approach in paper, "Uncertain< T >: A
 * First-Order Type for Uncertain Data", ASPLOS'2014
 *
 * @author Chiao Hsieh
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
		static public <T> Uncertain<T> newConstant(T value) {
			return new Constant<T>(value);
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
		 * The class implements Sequential Probability Ratio Test
		 */
		static class SPRT {

			final Supplier<Boolean> getObservation;
			final float L, H;
			final float pd, pf;

			protected int succNum, failNum;

			public SPRT(Supplier<Boolean> getObservation, float L, float H) {
				this.getObservation = getObservation;
				assert (0 <= L && L <= 1 && 0 <= H && H <= 1 && L < H);
				this.L = L;
				this.H = H;
				this.pd = 0.99F;
				this.pf = 0.01F;
				this.succNum = 0;
				this.failNum = 0;
			}

			public SPRT(Supplier<Boolean> getObservation, float L, float H, float pd, float pf) {
				this.getObservation = getObservation;
				assert (0 <= L && L <= 1 && 0 <= H && H <= 1 && L < H);
				this.L = L;
				this.H = H;
				assert (0 < pd + pf && pd + pf <= 1);
				this.pd = pd;
				this.pf = pf;
				this.succNum = 0;
				this.failNum = 0;
			}

			protected boolean WaldStep() {
				float beta = pd;
				float alpha = pf;
				int succNum0 = succNum;
				int failNum0 = failNum;

				boolean sample = getObservation.get();
				succNum = sample ? succNum0 + 1 : succNum0;
				failNum = sample ? failNum0 : failNum0 + 1;

				double logLRatio = succNum * Math.log(H) + failNum * Math.log(1 - H) - succNum * Math.log(L)
						- failNum * Math.log(1 - L);

				if (logLRatio > Math.log(beta) - Math.log(alpha))
					return true; // report p > H
				else if (logLRatio < Math.log(1 - beta) - Math.log(1 - alpha))
					return false; // report p < L
				else
					return WaldStep(); // XXX tail recursion may be changed to while-loop
			}

			boolean WaldTest() {
				// Initialize before every test
				succNum = 0;
				failNum = 0;
				return WaldStep();
			}
		}

		/**
		 * Return the probability that a given predicate is True is greater than certain
		 * threshold.
		 * 
		 * 
		 * @param cond
		 *            Given Uncertain < Boolean > predicate
		 * @param threshold
		 *            User specified threshold
		 * 
		 * @return Whether the probability is higher than the threshold
		 */
		static public boolean conditional(Uncertain<Boolean> cond, float threshold) {

			Supplier<Boolean> getObservation = () -> (cond.sample());

			// XXX decide better L, H ,pD, pF
			SPRT sprt = new SPRT(getObservation, threshold - 0.05F, threshold);
			return sprt.WaldTest();
		}

		static public boolean conditional(Uncertain<Boolean> cond) {
			return conditional(cond, 0.5F);
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
