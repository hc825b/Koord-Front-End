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
			return 0.0F;
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

	public T sample() {
		// TODO do sampling
		return null;
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

/**
 * Internal graphical representation of Bayesian Network
 * @author Chiao Hsieh
 */
abstract class DAG {
	// TODO implement visitor to traverse DAG in reverse topological order.
	// TODO implement check for cycle??
}

abstract class LeafDAG extends DAG {

}

class LeafPointMass<T> extends LeafDAG {
	protected final T _value;

	LeafPointMass(T value) {
		_value = value;
	}
}

class LeafUncertain<T extends Number> extends LeafDAG {
	protected final T _mu;
	protected final T _stdev;

	LeafUncertain(T mu, T stdev) {
		_mu = mu;
		_stdev = stdev;
	}
}

abstract class UnaryDAG extends DAG {
	protected DAG _subDAG;

	UnaryDAG(DAG subDAG) {
		super();
		_subDAG = subDAG;
	}
}

class NotDAG extends UnaryDAG {
	NotDAG(DAG subDAG) {
		super(subDAG);
	}
}

class NegativeDAG extends UnaryDAG {
	NegativeDAG(DAG subDAG) {
		super(subDAG);
	}
}

abstract class BinaryDAG extends DAG {
	protected DAG _lSubDAG;
	protected DAG _rSubDAG;

	BinaryDAG(DAG lDAG, DAG rDAG) {
		super();
		_lSubDAG = lDAG;
		_rSubDAG = rDAG;
	}
}

abstract class BinaryArithDAG extends BinaryDAG {
	BinaryArithDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class PlusDAG extends BinaryArithDAG {
	PlusDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class MinusDAG extends BinaryArithDAG {
	MinusDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class TimesDAG extends BinaryArithDAG {
	TimesDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class DivByDAG extends BinaryArithDAG {
	DivByDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

abstract class RelationalDAG extends BinaryDAG {
	RelationalDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class GEQDAG extends RelationalDAG {
	GEQDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class LEQDAG extends RelationalDAG {
	LEQDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class GTDAG extends RelationalDAG {
	GTDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class LTDAG extends RelationalDAG {
	LTDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

abstract class BinaryLogicalDAG extends BinaryDAG {
	BinaryLogicalDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class AndDAG extends BinaryLogicalDAG {
	AndDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}

class OrDAG extends BinaryLogicalDAG {
	OrDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}
}