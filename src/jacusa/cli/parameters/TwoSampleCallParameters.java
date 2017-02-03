package jacusa.cli.parameters;

import jacusa.io.format.AbstractOutputFormat;
import jacusa.method.call.statistic.dirmult.DirichletMultinomialRobustCompoundError;

public class TwoSampleCallParameters extends AbstractParameters implements hasSample2, hasStatisticCalculator {
	private SampleParameters sample2;
	private StatisticParameters statisticParameters;

	public TwoSampleCallParameters() {
		super();

		sample2				= new SampleParameters();
		statisticParameters = new StatisticParameters();
		statisticParameters.setStatisticCalculator(new DirichletMultinomialRobustCompoundError(getBaseConfig(), statisticParameters));
	}

	@Override
	public SampleParameters getSample2() {
		return sample2;
	}

	@Override
	public StatisticParameters getStatisticParameters() {
		return statisticParameters;
	}

	@Override
	public AbstractOutputFormat getFormat() {
		return (AbstractOutputFormat)super.getFormat();
	}

	public int[] getReplicates() {
		int[] replicates = new int[2];
		replicates[0] = getSample1().getPathnames().length;
		replicates[1] = getSample2().getPathnames().length;

		return replicates;
	}
	
}