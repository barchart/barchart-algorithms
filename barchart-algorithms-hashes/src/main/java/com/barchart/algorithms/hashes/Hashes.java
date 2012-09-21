package com.barchart.algorithms.hashes;

public class Hashes {

	public static HashKey jenkins(final HashKey triple, final long seed)  {
		
		long a, b, c;

		/* Set up the internal state */
		a = b = seed;
		c = 0x9e3779b97f4a7c13L; /* the golden ratio; an arbitrary value */

		a += triple.getFirst();
		b += triple.getSecond();
		c += triple.getThird();

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

		return new HashKey(a,b,c);
		
	}

	
}
