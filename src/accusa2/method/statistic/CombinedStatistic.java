package accusa2.method.statistic;

import accusa2.cli.Parameters;

/**
 * 
 * @author michael
 *
 * Calculates test-statistic based on number of alleles per parallel pileup.
 * 
 */

public class CombinedStatistic extends AbstractCombinedStatistic {


	public CombinedStatistic(Parameters parameters, StatisticCalculator ho_he, StatisticCalculator he_he, String name, String description) {
		super(parameters, ho_he, he_he, name, description);
	}

	@Override
	public StatisticCalculator newInstance() {
		return new CombinedStatistic(parameters, ho_he, he_he, name, description);
	}

	@Override
	public boolean filter(double value) {
		return ho_he.filter(value);
	}

}