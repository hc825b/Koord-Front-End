package edu.illinois.mitra.cyphyhouse.objects;

import java.util.Random;
import java.util.function.Supplier;

public class UniformFloat extends BlackBox<Float> {

	protected static Supplier<Float> buildSampler(float lower, float upper) {
		if (lower >= upper)
			throw new IllegalArgumentException("Illegal bound: [" + lower + ',' + upper + "]");

		return () -> (new Random()).nextFloat() * (upper - lower) + lower;
	}

	public UniformFloat(float lower, float upper) {
		super(UniformFloat.buildSampler(lower, upper));
	}
}
