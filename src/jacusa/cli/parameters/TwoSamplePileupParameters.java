package jacusa.cli.parameters;

public class TwoSamplePileupParameters extends AbstractParameters implements hasSample2 {

	private SampleParameters sample2;

	public TwoSamplePileupParameters() {
		super();

		sample2	= new SampleParameters();
	}

	@Override
	public SampleParameters getSample2() {
		return sample2;
	}

	public int[] getReplicates() {
		int[] replicates = new int[2];
		replicates[0] = getSample1().getPathnames().length;
		replicates[1] = getSample2().getPathnames().length;

		return replicates;
	}
}