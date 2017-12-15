package edu.illinois.mitra.cyphyhouse.objects;

import java.util.function.Supplier;

public class BlackBox<T> extends Primitive<T> {
	public BlackBox(Supplier<T> sampler) {
		super(new LeafBlackBox<T>(sampler));
	}
}