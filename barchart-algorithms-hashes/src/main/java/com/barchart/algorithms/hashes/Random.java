/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.algorithms.hashes;

public class Random {
	
	private final long seedUniquifier = 0x9E3779B97F4A7C16L;
	
	private long val;
	
	public Random() {
		
		long temp = seedUniquifier + System.nanoTime();
		
		temp ^= temp >>> 33;
		temp *= 0xff51afd7ed558ccdL;
		temp ^= temp >>> 33;
		temp *= 0xc4ceb9fe1a85ec53L;
		temp ^= temp >>> 33;
		
		val = temp;
		
	}
	
	public long nextLong() {
		
		val ^= val << 23;
		val ^= val >>> 52;
		return 2685821657736338717L * (val ^= (val >>> 17));
		
	}

}
