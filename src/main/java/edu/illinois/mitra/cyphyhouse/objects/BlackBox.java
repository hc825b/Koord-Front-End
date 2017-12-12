package edu.illinois.mitra.cyphyhouse.objects;

import java.util.function.Supplier;

public class BlackBox<T> extends Primitive<T> {
	public BlackBox(Supplier<T> sampler) {
		super(new LeafBlackBox<T>(sampler));
	}
}

class LeafBlackBox<T> extends LeafDAG<T> {
	protected final Supplier<T> _sampler;

	LeafBlackBox(Supplier<T> sampler) {
		_sampler = sampler;
	}

	@Override
	void initDAG() {
		_value = null;
	}

	@Override
	protected boolean ready() {
		return _value == null;
	}

	@Override
	final protected void read() {
		_value = _sampler.get();
	}

	@Override
	final protected T get() {
		return _value;
	}
}