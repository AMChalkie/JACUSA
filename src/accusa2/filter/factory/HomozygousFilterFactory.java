package accusa2.filter.factory;

import accusa2.filter.process.AbstractPileupBuilderFilter;
import accusa2.filter.process.HomozygousParallelPileupFilter;

public class HomozygousFilterFactory extends AbstractFilterFactory {

	private int sample;

	public HomozygousFilterFactory() {
		super('H', "Filter non-homozygous pileup/BAM (1 or 2). Default: none");
		sample = 0;
	}

	@Override
	public HomozygousParallelPileupFilter getParallelPileupFilterInstance() {
		return new HomozygousParallelPileupFilter(getC(), sample);
	}

	@Override
	public AbstractPileupBuilderFilter getPileupBuilderFilterInstance() {
		return null;
	}
	
	@Override
	public void processCLI(String line) throws IllegalArgumentException {
		if(line.length() == 1) {
			throw new IllegalArgumentException("Invalid argument " + line);
		}

		String[] s = line.split(Character.toString(AbstractFilterFactory.SEP));
		int sample = Integer.valueOf(s[1]);
		if(sample == 1 || sample == 2) {
			setSample(sample);
			return;
		}
		throw new IllegalArgumentException("Invalid argument " + sample);
	}

	public final void setSample(int sample) {
		this.sample = sample;
	}

	public final int getSample() {
		return sample;
	}

}
