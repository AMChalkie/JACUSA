package jacusa.cli.options;

import jacusa.cli.parameters.StatisticParameters;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class StatisticFilterOption  extends AbstractACOption {

	private int replicates;
	private StatisticParameters parameters;

	private final String AUTO_RDD_THRESHOLD = "auto-rdd";
	private final String AUTO_RRD_THRESHOLD = "auto-rrd";
	
	
	private final double RDD_threshold = 1.15;
	private final double[] RRD_threshold = {1.22, 1.56, 2.11, 2.32, 2.78};

	public StatisticFilterOption(int replicates, StatisticParameters parameters) {
		this.replicates = replicates;
		this.parameters = parameters;
		opt = "T";
		longOpt = "threshold";
	}

	@SuppressWarnings("static-access")
	@Override
	public Option getOption() {
		return OptionBuilder.withLongOpt(longOpt)
			.withArgName(longOpt.toUpperCase())
			.hasArg(true)
	        .withDescription("Filter positions based on test-statistic " + longOpt.toUpperCase() + "\n default: DO NOT FILTER")
	        .create(opt);
	}

	@Override
	public void process(CommandLine line) throws Exception {
		if (line.hasOption(opt)) {
		    String value = line.getOptionValue(opt);
		    double threshold = Double.NaN;
		    if (value.equals(AUTO_RDD_THRESHOLD)) {
		    	threshold = calculateRDDThreshold(replicates);
		    } else if (value.equals(AUTO_RRD_THRESHOLD)) {
		    	threshold = calculateRRDThreshold(replicates);
		    } else {
		    	threshold = Double.parseDouble(value);
		    }
		    
	    	if (threshold == Double.NaN || threshold < 0) {
	    		throw new Exception("Invalid value for " + longOpt.toUpperCase() + ". Allowed values are 0 <= " + longOpt.toUpperCase() + " or " + AUTO_RDD_THRESHOLD + " and " + AUTO_RRD_THRESHOLD);
	    	}
	    	parameters.setThreshold(threshold);
		}
	}

	private double calculateRRDThreshold(int replicates) {
		// see JACUSA paper supplement Figure 9
		if (replicates > 5) {
			return 0.3881 * (double)replicates + 0.8314;
		}
		
		return RRD_threshold[replicates - 1];
	}

	private double calculateRDDThreshold(int replicates) {
		return RDD_threshold;
	}
}