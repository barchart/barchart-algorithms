package com.barchart.algorithms.hashes;

/**
 * See http://www.burtleburtle.net/bob/c/lookup3.c 
 * http://en.wikipedia.org/wiki/Jenkins_hash_function
 * 
 * @author Gavin M Litchfield
 *
 */
public class Jenkins {

	private final long seed;
	
	public Jenkins(final long seed) {
		this.seed = seed;
	}
	
	/**
	 * Returns a TripleLong for an integer value, used initially.
	 * 
	 * @param value
	 * @return a new hash key.
	 */
	public HashKey hash(final int value) {
		
		final HashKey key = new HashKey();
		
		long a = value + seed;
		long b = value + seed;
		long c = 0x9e3779b97f4a7c13L; /* the golden ratio; an arbitrary value */
		
		a -= b; a -= c; a ^= (c >>> 43);
		b -= c; b -= a; b ^= (a << 9);
		c -= a; c -= b; c ^= (b >>> 8);
		a -= b; a -= c; a ^= (c >>> 38);
		b -= c; b -= a; b ^= (a << 23);
		c -= a; c -= b; c ^= (b >>> 5);
		a -= b; a -= c; a ^= (c >>> 35);
		b -= c; b -= a; b ^= (a << 49);
		c -= a; c -= b; c ^= (b >>> 11);
		a -= b; a -= c; a ^= (c >>> 12);
		b -= c; b -= a; b ^= (a << 18);
		c -= a; c -= b; c ^= (b >>> 22);
		
		key.setFirst(a);
		key.setSecond(b);
		key.setThird(c);
		
		return key;
		
	}
	
	/**
	 * Re-hash an existing key
	 * 
	 * @param hashKey
	 * @return the new hash key.
	 */
	public HashKey hash(final HashKey hashKey) {
		
		final HashKey key = new HashKey();
		
		long a, b, c;
		
		a = b = seed;
		c = 0x9e3779b97f4a7c13L; /* the golden ratio; an arbitrary value */

		a += hashKey.getFirst();
		b += hashKey.getSecond();
		c += hashKey.getThird();
		
		a -= b; a -= c; a ^= (c >>> 43);
		b -= c; b -= a; b ^= (a << 9);
		c -= a; c -= b; c ^= (b >>> 8);
		a -= b; a -= c; a ^= (c >>> 38);
		b -= c; b -= a; b ^= (a << 23);
		c -= a; c -= b; c ^= (b >>> 5);
		a -= b; a -= c; a ^= (c >>> 35);
		b -= c; b -= a; b ^= (a << 49);
		c -= a; c -= b; c ^= (b >>> 11);
		a -= b; a -= c; a ^= (c >>> 12);
		b -= c; b -= a; b ^= (a << 18);
		c -= a; c -= b; c ^= (b >>> 22);
		
		key.setFirst(a);
		key.setSecond(b);
		key.setThird(c);
		
		return key;
		
	}
	
}
