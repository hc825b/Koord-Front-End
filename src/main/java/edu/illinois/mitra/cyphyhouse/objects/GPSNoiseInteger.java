package edu.illinois.mitra.cyphyhouse.objects;

import java.util.Random;
import java.util.function.Supplier;

public class GPSNoiseInteger extends BlackBox<Integer> {
	
	/**
	 * Generate Rayleigh distribution from uniform distribution using inverse transform sampling 
	 * @param epsilon
	 * @return a sample from Rayleigh distribution
	 */
	protected static double nextRayleigh(double epsilon) {
		return epsilon * Math.sqrt(-2 * Math.log((new Random()).nextDouble()));
	}
	
	/**
	 * 
	 */
	protected static Supplier<Integer> buildSampler(int position, double accuracy) {
		double sign = (new Random()).nextBoolean() ? 1 : -1;
		return () -> position + (int) (sign * nextRayleigh(accuracy / Math.sqrt(Math.log(400))));
	}

	public GPSNoiseInteger(int position, double accuracy) {
		super(buildSampler(position, accuracy));
	}
}
