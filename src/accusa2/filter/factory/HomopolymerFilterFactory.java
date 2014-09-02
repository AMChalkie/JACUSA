package accusa2.filter.factory;

import accusa2.cli.parameters.AbstractParameters;
import accusa2.filter.AbstractParallelPileupFilter;
import accusa2.filter.CountBasedFilter;
import accusa2.filter.cache.AbstractPileupBuilderFilterCount;
import accusa2.filter.cache.HomopolymerFilterCount;

public class HomopolymerFilterFactory extends AbstractFilterFactory {

	private int length;
	private int distance;
	private AbstractParameters parameters;
	
	public HomopolymerFilterFactory(AbstractParameters parameters) {
		super('Y', "Filter wrong variant calls in the vicinity of homopolymers. Format: length:distance");
		this.parameters = parameters;
	}

	@Override
	public AbstractParallelPileupFilter getFilterInstance() {
		return new CountBasedFilter(getC(), 1, null, null); // TODO
	}

	@Override
	public AbstractPileupBuilderFilterCount getFilterCountInstance() {
		return new HomopolymerFilterCount(getC(), length, distance, parameters);
	}

	@Override
	public void processCLI(String line) throws IllegalArgumentException {
		if(line.length() == 1) {
			throw new IllegalArgumentException("Invalid argument " + line);
		}

		String[] s = line.split(Character.toString(AbstractFilterFactory.SEP));
		// format Y:length:distance 
		for(int i = 1; i < s.length; ++i) {
			int value = Integer.valueOf(s[i]);

			switch(i) {
			case 1:
				setLength(value);
				break;

			case 2:
				setDistance(value);
				break;

			default:
				throw new IllegalArgumentException("Invalid argument " + length);
			}
		}
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public final int getLength() {
		return length;
	}

	public final void setDistance(int distance) {
		this.distance = distance;
	}

	public final int getDistance() {
		return distance;
	}

}
