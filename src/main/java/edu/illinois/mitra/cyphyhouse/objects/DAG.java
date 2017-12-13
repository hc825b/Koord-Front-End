package edu.illinois.mitra.cyphyhouse.objects;

/**
 * Internal graphical representation of Bayesian Network
 * 
 * @author Chiao Hsieh
 */
abstract class DAG {
	abstract void initDAG();

	// TODO implement check for cycle??

	/**
	 * Evaluate the value of the expression this DAG represents.
	 */
	final Object eval() {
		// XXX Can we find a better way than return Object type?
		// XXX Current we will visit shared internal nodes repeatedly to avoid storing
		// computed result. Should we store intermediate result at internal nodes?
		Object ret = calc();
		return ret;
	}

	protected abstract Object calc();
}

abstract class LeafDAG<T> extends DAG {
	protected T _value;

	@Override
	protected Object calc() {
		if (!ready())
			read();
		return get();
	}

	abstract protected boolean ready();

	abstract protected void read();

	abstract protected T get();
}

class LeafPointMass<T> extends LeafDAG<T> {
	protected final T _value;

	LeafPointMass(T value) {
		_value = value;
	}

	@Override
	final void initDAG() {
		// Do nothing
	}

	@Override
	final protected boolean ready() {
		return true; // Always ready
	}

	@Override
	final protected void read() {
		// Do nothing
	}

	@Override
	final protected T get() {
		return _value;
	}
}

abstract class UnaryDAG extends DAG {
	protected DAG _subDAG;

	UnaryDAG(DAG subDAG) {
		super();
		_subDAG = subDAG;
	}

	@Override
	final void initDAG() {
		_subDAG.initDAG();
	}
}

class NotDAG extends UnaryDAG {
	NotDAG(DAG subDAG) {
		super(subDAG);
	}

	@Override
	final protected Object calc() {
		return ((Boolean) _subDAG.eval());
	}
}

class NegativeDAG extends UnaryDAG {
	NegativeDAG(DAG subDAG) {
		super(subDAG);
	}

	@Override
	final protected Object calc() {
		Object ret = _subDAG.eval();
		if (Integer.class.isInstance(ret))
			return -((Integer) ret);
		if (Float.class.isInstance(ret))
			return -((Float) ret);

		throw new UnsupportedOperationException("Negative sign over unsupported type");
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

	@Override
	final void initDAG() {
		_lSubDAG.initDAG();
		_rSubDAG.initDAG();
	}

	@Override
	final protected Object calc() {
		Object lhs = _lSubDAG.eval();
		Object rhs = _rSubDAG.eval();
		return calc(lhs, rhs);
	}

	abstract protected Object calc(Object lhs, Object rhs);
}

abstract class BinaryArithDAG extends BinaryDAG {
	BinaryArithDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Object calc(Object lhs, Object rhs) {
		if (lhs.getClass() != rhs.getClass())
			throw new RuntimeException("Arithmetic expression over incompatible type");

		if (Integer.class.isInstance(lhs) && Integer.class.isInstance(rhs))
			return applyOp((Integer) lhs, (Integer) rhs);
		if (Float.class.isInstance(lhs) && Float.class.isInstance(rhs))
			return applyOp((Float) lhs, (Float) rhs);

		throw new UnsupportedOperationException("Arithmetic expression over unsupported type");
	}

	abstract protected Integer applyOp(Integer lhs, Integer rhs);

	abstract protected Float applyOp(Float lhs, Float rhs);
}

class PlusDAG extends BinaryArithDAG {
	PlusDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Integer applyOp(Integer lhs, Integer rhs) {
		return lhs + rhs;
	}

	@Override
	final protected Float applyOp(Float lhs, Float rhs) {
		return lhs + rhs;
	}
}

class MinusDAG extends BinaryArithDAG {
	MinusDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Integer applyOp(Integer lhs, Integer rhs) {
		return lhs - rhs;
	}

	@Override
	final protected Float applyOp(Float lhs, Float rhs) {
		return lhs - rhs;
	}
}

class TimesDAG extends BinaryArithDAG {
	TimesDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Integer applyOp(Integer lhs, Integer rhs) {
		return lhs * rhs;
	}

	@Override
	final protected Float applyOp(Float lhs, Float rhs) {
		return lhs * rhs;
	}
}

class DivByDAG extends BinaryArithDAG {
	DivByDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Integer applyOp(Integer lhs, Integer rhs) {
		return lhs / rhs;
	}

	@Override
	final protected Float applyOp(Float lhs, Float rhs) {
		return lhs / rhs;
	}
}

abstract class RelationalDAG extends BinaryDAG {
	RelationalDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Object calc(Object lhs, Object rhs) {
		if (lhs.getClass() != rhs.getClass())
			throw new RuntimeException("Relational expression over incompatible type");

		if (Integer.class.isInstance(lhs) && Integer.class.isInstance(rhs))
			return applyOp((Integer) lhs, (Integer) rhs);
		if (Float.class.isInstance(lhs) && Float.class.isInstance(rhs))
			return applyOp((Float) lhs, (Float) rhs);

		throw new UnsupportedOperationException("Relational expression over unsupported type");
	}

	abstract protected boolean applyOp(Integer lhs, Integer rhs);

	abstract protected boolean applyOp(Float lhs, Float rhs);
}

class GEQDAG extends RelationalDAG {
	GEQDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(Integer lhs, Integer rhs) {
		return lhs >= rhs;
	}

	@Override
	final protected boolean applyOp(Float lhs, Float rhs) {
		return lhs >= rhs;
	}
}

class LEQDAG extends RelationalDAG {
	LEQDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(Integer lhs, Integer rhs) {
		return lhs <= rhs;
	}

	@Override
	final protected boolean applyOp(Float lhs, Float rhs) {
		return lhs <= rhs;
	}
}

class GTDAG extends RelationalDAG {
	GTDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(Integer lhs, Integer rhs) {
		return lhs > rhs;
	}

	@Override
	final protected boolean applyOp(Float lhs, Float rhs) {
		return lhs > rhs;
	}
}

class LTDAG extends RelationalDAG {
	LTDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(Integer lhs, Integer rhs) {
		return lhs < rhs;
	}

	@Override
	final protected boolean applyOp(Float lhs, Float rhs) {
		return lhs < rhs;
	}
}

abstract class BinaryLogicalDAG extends BinaryDAG {
	BinaryLogicalDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected Object calc(Object lhs, Object rhs) {
		assert (Boolean.class.isInstance(lhs) && Boolean.class.isInstance(rhs));

		return applyOp((Boolean) lhs, (Boolean) rhs);
	}

	abstract protected boolean applyOp(boolean lhs, boolean rhs);
}

class AndDAG extends BinaryLogicalDAG {
	AndDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(boolean lhs, boolean rhs) {
		return lhs && rhs;
	}
}

class OrDAG extends BinaryLogicalDAG {
	OrDAG(DAG lDAG, DAG rDAG) {
		super(lDAG, rDAG);
	}

	@Override
	final protected boolean applyOp(boolean lhs, boolean rhs) {
		return lhs || rhs;
	}
}