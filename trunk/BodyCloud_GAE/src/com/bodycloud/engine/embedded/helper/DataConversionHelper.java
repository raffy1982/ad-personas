package com.bodycloud.engine.embedded.helper;

import weka.core.DenseInstance;
import weka.core.Instance;

public class DataConversionHelper {
	
	public static double[] intToDouble(int[] input) {
		double[] out = new double[input.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = input[i];
		}
		return out;
	}
	
	public static Instance arrayToInstance(int[] input) {
		return new DenseInstance(1, intToDouble(input));
	}
	
	public static Instance arrayToInstance(double[] input) {
		return new DenseInstance(1, input);
	}
	
	public static int[] instanceToInt(Instance instance) {
		double[] input = instance.toDoubleArray();
		int[] out = new int[input.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = (int) input[i];
		}
		return out;
	}

}
