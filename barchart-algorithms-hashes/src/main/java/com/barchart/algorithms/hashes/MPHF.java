package com.barchart.algorithms.hashes;

import java.math.BigInteger;

public class MPHF {

	// This is still broken, but increasing size of this hides it
	public static final int BITS_PER_BLOCK = 10240000;
	public static final int BITS_DIV = BITS_PER_BLOCK / Long.SIZE;
	
	private static final long ONES_STEP_4 = 0x1111111111111111L;
	private static final long ONES_STEP_8 = 0x0101010101010101L;
	
	/** The number of nonzero bit pairs up to a given block of {@link #BITS_PER_BLOCK} bits. */
	protected final long count[];
	
	private long globalseed;
	
	private int[] mph;
	private long[] words;
	
	private final HypergraphSort hgSorter;
	private final Jenkins keyHash;
	
	/**
	 * Only tested up to 5000 values.
	 * @param values
	 */
	public MPHF(final int[] values) {
		
		final int numElements = values.length;
		
		final Random rand = new Random();
		
		long seed = rand.nextLong();
		
		keyHash = new Jenkins(seed);
		
		final long[][] elementHashes = new long[numElements][3];
		
		for(int i = 0; i < numElements; i++) {
			elementHashes[i] = keyHash.hash(values[i]);
		}
		
		int listSize = (int)Math.ceil(numElements * HypergraphSort.GAMMA) + 4;
		
		mph = new int[listSize];
		for(int i = 0; i < listSize; i++) {
			mph[i] = 0;
		}
		
		hgSorter = new HypergraphSort(numElements);
		
		while(!hgSorter.generateAndSort(elementHashes, seed)) {
			seed = rand.nextLong();
		}
		
		int top = numElements, k, v = 0;
		final int[] stack   = hgSorter.stack;
		final int[] vertex1 = hgSorter.vertex1;
		final int[] vertex2 = hgSorter.vertex2;
		
		while(top > 0) {
			v = stack[--top];
			k = (v > vertex1[v] ? 1 : 0) + (v > vertex2[v] ? 1 : 0); 
			final int s = mph[vertex1[v]] + mph[vertex2[v]];
			final int value = (k - s + 9) % 3;
			mph[v] = value == 0 ? 3 : value;
		}
		
		globalseed = seed;
		
		long size = numElements;
		
		count = new long[(int)((2L * size + BITS_PER_BLOCK - 1) / BITS_PER_BLOCK)];
		long c = 0;
		
		final int numWords = (int)((2L * size + Long.SIZE - 1) / Long.SIZE);
		
		for(int i = 0; i < numWords; i++) {
			if((i & (BITS_DIV - 1)) == 0) {
				count[i / (BITS_DIV)] = c;
			}
			c += countNonzeroPairs(fromVals(i, values));
		}
		
		final int numW = (mph.length / 32) + 1;
		words = new long[numW];
		for(int i = 0; i < numW; i++) {
			words[i] = fromVals(i, mph);
		}
		
	}
	
	/* There is a bit shift way of doing this which i can do later */
	/* This is only called from the constructor so hash performance is not affected */
	private long fromVals(final int wordNumber, final int[] vals) {
		
		if(wordNumber * 32 > vals.length) {
			System.out.println("Wordnumber is too large for vals\t" + wordNumber + 
					"\t" + vals.length);
			return -1;
		}
		
		int last = (wordNumber + 1) * 32;
		if(last > vals.length) {
			last = vals.length;
		}
		
		StringBuilder bytes = new StringBuilder();
		for(int i = last -1; i >= wordNumber * 32; i--) {
			switch(vals[i] % 4) {
			case 0:
				bytes.append("00");
				break;
			case 1:
				bytes.append("01");
				break;
			case 2:
				bytes.append("10");
				break;
			case 3:
				bytes.append("11");
				break;
			}
		}
		
		return new BigInteger(bytes.toString(), 2).longValue();
	}
	
	public static int countNonzeroPairs(final long x) {
		long byteSums = (x | x >>> 1) & 0x5 * ONES_STEP_4;
		byteSums = (byteSums & 3 * ONES_STEP_4) + ((byteSums >>> 2) & 3 * ONES_STEP_4);
		byteSums = (byteSums + (byteSums >>> 4)) & 0x0f * ONES_STEP_8;
		return (int)(byteSums * ONES_STEP_8 >>> 56);
	}
	
	long max = 0;
	
	private long rank(long x) {
		
		x *= 2;
		final int word = (int)(x / Long.SIZE);
		
		long rank = count[word / (BITS_DIV)];
		int wordInBlock = word & ~((BITS_DIV) - 1);
		
		while(wordInBlock < word) {
			rank += countNonzeroPairs(words[wordInBlock++]);
		}

		final long from = words[word];
		
		return rank + countNonzeroPairs(from & (1L << x % Long.SIZE) - 1);
	}
	
	public int getLong(final int value) {
		
		final int[] e = new int[3];
		
		final long[] key = keyHash.hash(value);
		
		hgSorter.keyToEdge(key, globalseed, e);
		
		final long result = rank(e[(
				mph[(e[0])] + 
				mph[e[1]] + 
				mph[e[2]]) % 3]);

		return (int) result;
		
	}
	
}
