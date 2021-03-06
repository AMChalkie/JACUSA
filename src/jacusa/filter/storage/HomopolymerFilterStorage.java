package jacusa.filter.storage;

import java.util.ArrayList;
import java.util.List;

import jacusa.cli.parameters.AbstractParameters;
import jacusa.cli.parameters.SampleParameters;
import jacusa.util.WindowCoordinates;
import net.sf.samtools.CigarElement;
import net.sf.samtools.SAMRecord;

public class HomopolymerFilterStorage extends AbstractWindowFilterStorage {
	
	private int minLength;

	private List<Integer> bases;
	private List<Integer> quals;
	private int windowPositionStart;
	private int readPositionStart;
	private int readPositionLast;
	
	/**
	 * 
	 * @param c
	 * @param distance
	 */
	public HomopolymerFilterStorage(
			final char c, 
			final int length, 
			final WindowCoordinates windowCoordinates,
			final SampleParameters sampleParameters,
			final AbstractParameters parameters) {
		super(c, windowCoordinates, sampleParameters, parameters);

		this.minLength = length;

		int n = minLength + 5;
		bases = new ArrayList<Integer>(n);
		quals = new ArrayList<Integer>(n);
	}

	@Override
	public void processAlignmentMatch(int windowPosition, int readPosition, int genomicPosition, CigarElement cigarElement, SAMRecord record, int base, int qual) {
		// check if record changed		
		if (this.record != record) {
			checkAndAdd2WindowCache();
			resetAndAdd(windowPosition, base, qual);
			readPositionStart = readPosition;
			readPositionLast = readPosition;
			this.record = record;
		// check if discontinued
		} else if(readPositionLast + 1 != readPosition) {
			checkAndAdd2WindowCache();
			resetAndAdd(windowPosition, base, qual);
			readPositionStart = readPosition;
			readPositionLast = readPosition;	
		} else {
			// check if the base changed
			if (bases.size() > 0 && bases.get(bases.size() - 1) != base) {
				checkAndAdd2WindowCache();
				resetAndAdd(windowPosition, base, qual);
				readPositionLast = readPosition;
			} else {
				bases.add(base);
				quals.add(qual);
				// check if we 
				if (readPosition - readPositionStart + 1 == cigarElement.getLength()) {
					checkAndAdd2WindowCache();

					// reset
					windowPositionStart = -1;
					readPositionStart = -1;
					bases.clear();
				}
				readPositionLast = readPosition;
			}
		}
	}

	private void checkAndAdd2WindowCache() {
		int coveredReadLength = bases.size();
		if (coveredReadLength >= minLength) {
			for (int i = 0; i < bases.size(); ++i) {
				if (windowPositionStart + i >= 0 && windowPositionStart + i < windowSize) {
					windowCache.addHighQualityBaseCall(windowPositionStart + i, bases.get(i), quals.get(i));
				} else {
					return;
				}
			}
		}
	}

	private void resetAndAdd(int windowPosition, int base, int qual) {
		windowPositionStart = windowPosition;
		bases.clear();
		bases.add(base);
		quals.add(qual);
	}
	
	/*
	@Override
	public void processRecord(int genomicWindowStart, SAMRecord record) {
		readPositionStart = readPosition;
		windowPositionStart = windowPosition;
		baseI = record.getReadBases()[readPositionStart];

		for (int i = 1; i < cigarElement.getLength(); ++i) {
			byte qual = record.getBaseQualities()[readPosition + i];
			// quick fix
			qual = (byte)Math.min(qual, Phred2Prob.MAX_Q - 1);
			
			if (baseI != record.getReadBases()[readPosition + i]) {
				// fill cache
				final int coveredReadLength = readPosition + i - readPositionStart; 
				if (coveredReadLength >= minLength) {
					parseRecord(windowPositionStart, coveredReadLength, readPositionStart, record);
				}

				// reset
				readPositionStart = readPosition + i;
				windowPositionStart = windowPosition + i;
				baseI = record.getReadBases()[readPositionStart];
			}
		}
	}
	*/

	public int getLength() {
		return minLength;
	}

	public int getDistance() {
		return minLength;
	}
	
}