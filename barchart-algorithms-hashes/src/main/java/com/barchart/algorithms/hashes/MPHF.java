package com.barchart.algorithms.hashes;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MPHF {

	public static final int BITS_PER_BLOCK = 1024;
	private static final long ONES_STEP_4 = 0x1111111111111111L;
	private static final long ONES_STEP_8 = 0x0101010101010101L;
	
	/** The number of nonzero bit pairs up to a given block of {@link #BITS_PER_BLOCK} bits. */
	protected final long count[];
	
	private long globalseed;
	
	private List<Integer> mph;
	
	private final HypergraphSort hgSorter;
	private final Jenkins keyHash;
	
	public MPHF(final List<Integer> values) {
		
		final int numElements = values.size();
		
		final Random rand = new Random();
		
		long seed = rand.nextLong();
		
		keyHash = new Jenkins(seed);
		
		final List<HashKey> elementHashes = new ArrayList<HashKey>();
		
		for(Integer i : values) {
			elementHashes.add(keyHash.hash(i));
		}
		
		int listSize = (int)Math.ceil(values.size() * HypergraphSort.GAMMA) + 4;
		
		mph = new ArrayList<Integer>(listSize);
		for(int i = 0; i < listSize; i++) {
			mph.add(0);
		}
		
		hgSorter = new HypergraphSort(numElements);
		
		while(!hgSorter.generateAndSort(elementHashes, seed)) {
			seed = rand.nextLong();
		}
		
		int top = numElements, k, v = 0;
		final int[] stack = hgSorter.stack;
		final int[] vertex1 = hgSorter.vertex1;
		final int[] vertex2 = hgSorter.vertex2;
		
		while(top > 0) {
			v = stack[--top];
			k = (v > vertex1[v] ? 1 : 0) + (v > vertex2[v] ? 1 : 0); 
			final int s = mph.get(vertex1[v]) + mph.get(vertex2[v]);
			final int value = (k - s + 9) % 3;
			mph.set(v, value == 0 ? 3 : value);
		}
		
		globalseed = seed;
		
		long size = values.size();
		
		final int numBlocks = (int) Math.ceil((2L * size + BITS_PER_BLOCK - 1) / (double) BITS_PER_BLOCK);
		
		count = new long[numBlocks];
		long c = 0;
		
		final int numWords = (int)((2L * size + Long.SIZE - 1) / Long.SIZE);
		
		for(int i = 0; i < numWords; i++) {
			if((i & (BITS_PER_BLOCK / Long.SIZE - 1)) == 0) {
				count[i / (BITS_PER_BLOCK / Long.SIZE)] = c;
			}
			c += countNonzeroPairs(fromVals(i, values));
		}
		
	}
	
	/* There is a bit shift way of doing this which i can do later */
	private long fromVals(final int wordNumber, final List<Integer> vals) {
		
		if(wordNumber * 32 > vals.size()) {
			System.out.println("Wordnumber is too large for vals\t" + wordNumber + 
					"\t" + vals.size());
			return -1;
		}
		
		int last = (wordNumber + 1) * 32;
		if(last > vals.size()) {
			last = vals.size();
		}
		
		String bytes = "";
		for(int i = last -1; i >= wordNumber * 32; i--) {
			switch(vals.get(i) % 4) {
			case 0:
				bytes = bytes + "00";
				break;
			case 1:
				bytes = bytes + "01";
				break;
			case 2:
				bytes = bytes + "10";
				break;
			case 3:
				bytes = bytes + "11";
				break;
			}
		}
		
		return new BigInteger(bytes, 2).longValue();
	}
	
	public static int countNonzeroPairs(final long x) {
		long byteSums = (x | x >>> 1) & 0x5 * ONES_STEP_4;
		byteSums = (byteSums & 3 * ONES_STEP_4) + ((byteSums >>> 2) & 3 * ONES_STEP_4);
		byteSums = (byteSums + (byteSums >>> 4)) & 0x0f * ONES_STEP_8;
		return (int)(byteSums * ONES_STEP_8 >>> 56);
	}
	
	private long rank(long x) {
		x *= 2;
		final int word = (int)(x / Long.SIZE);
		long rank = count[word / (BITS_PER_BLOCK / Long.SIZE)];
		int wordInBlock = word & ~((BITS_PER_BLOCK / Long.SIZE) - 1);
		while(wordInBlock < word) {
			rank += countNonzeroPairs(fromVals(wordInBlock++, mph));
		}

		long from = fromVals(word, mph);
		
		return rank + countNonzeroPairs(from & (1L << x % Long.SIZE) - 1);
	}
	
	public long getLong(final int value) {
		
		final int[] e = new int[3];
		
		final HashKey key = keyHash.hash(value);
		
		hgSorter.keyToEdge(key, globalseed, e);
		
		final long result = rank(e[(int)(mph.get(e[0]) + 
				 mph.get(e[1]) + 
				 mph.get(e[2])) % 3]);

		return result;
		
	}
	
	public static void main(final String[] args) {
		
		final List<Integer> vals = new ArrayList<Integer>();
		
		vals.add(1);
		vals.add(13);
		vals.add(4001);
		vals.add(906);
		
		final MPHF mphf = new MPHF(vals);
		
		for(Integer i : vals) {
			System.out.println(mphf.getLong(i));
		}
		
	}
	
}
