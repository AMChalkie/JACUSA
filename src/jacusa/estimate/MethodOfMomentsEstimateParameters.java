package jacusa.estimate;

import jacusa.phred2prob.Phred2Prob;
import jacusa.pileup.Pileup;

import java.util.Arrays;

public class MethodOfMomentsEstimateParameters extends AbstractEstimateParameters {

	private final double initialAlphaNull;

	public MethodOfMomentsEstimateParameters(final double initialAlphaNull, final Phred2Prob phred2Prob) {
		super("mom", "Method of Moments", phred2Prob);
		this.initialAlphaNull = initialAlphaNull;
	}

	@Override
	public double[] estimateAlpha(int[] baseIs, Pileup[] pileups) {
		// use initial alpha to init
		final double[] alpha = new double[baseIs.length];
		if (initialAlphaNull > 0.0) {
			Arrays.fill(alpha, initialAlphaNull / (double)baseIs.length);
		} else {
			Arrays.fill(alpha, 0.0);
		}
		
		for (Pileup pileup : pileups) {
			double[] v = phred2Prob.colSumProb(baseIs, pileup);
			for(int baseI : baseIs) {
				alpha[baseI] += v[baseI];
			}
		}

		return alpha;
	}

	@Override
	public double[][] probabilityMatrix(int[] baseIs, Pileup[] pileups) {
		final double[][] probs = new double[pileups.length][baseIs.length];

		for(int pileupI = 0; pileupI < pileups.length; ++pileupI) {
			// sum the probabilities giving alpha 
			probs[pileupI] = phred2Prob.colMeanProb(baseIs, pileups[pileupI]);
		}

		return probs;
	}
}