/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
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
	public long[] hash(final int value) {
		
		final long[] key = new long[3];
		
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
		
		key[0] = a;
		key[1] = b;
		key[2] = c;
		
		return key;
		
	}
	
	/**
	 * Re-hash an existing key
	 * 
	 * @param hashKey
	 * @return the new hash key.
	 */
	public long[] hash(final long[] hashKey) {
		
		final long[] hash = new long[3];
		
		long a, b, c;
		
		a = b = seed;
		c = 0x9e3779b97f4a7c13L; /* the golden ratio; an arbitrary value */

		a += hashKey[0];
		b += hashKey[1];
		c += hashKey[2];
		
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
		
		hash[0] = a;
		hash[1] = b;
		hash[2] = c;
		
		return hash;
		
	}
	
}
