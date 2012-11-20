package com.barchart.algorithms.hashes;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestMPHF {
	
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testSmallVals() {
		
		final int[] ints = new int[21];
		
		int counter = 0;
		for(int i = -10; i < 11; i++) {
			ints[counter] = i;
			counter++;
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testMedVals() {
		
		final int[] ints = new int[21];
		
		int counter = 0;
		for(int i = -10; i < 11; i++) {
			ints[counter] = i * 1000;
			counter++;
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testLargeVals() {
		
		final int[] ints = new int[21];
		
		int counter = 0;
		for(int i = -10; i < 11; i++) {
			ints[counter] = i * 1000000;
			counter++;
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testLargeSet() {
		
		final int[] ints = new int[5000];
		
		for(int i = 0; i < 5000; i++) {
			ints[i] = i;
		}
		
		final MPHF func = new MPHF(ints);

		final long start = System.currentTimeMillis();
		
		assertTrue(testMPH(ints, func));
		
		System.out.println(System.currentTimeMillis() - start);
	}
	
	private boolean testMPH(final int[] ints, final MPHF func) {
		
		final int[] counter = new int[ints.length];
		
		for(int i = 0; i < ints.length; i++) {
			counter[i] = 0;
		}
		
		for(int i : ints) {
			counter[(int) func.getLong(i)]++;
		}
		
		for(int i : counter) {
			if(i != 1) {
				
				for(int j = 0; j < ints.length; j++) {
					System.out.println(ints[j] + " mapped " + counter[j] + " times");
				}
				
				return false;
			}
		}
		
		return true;
		
	}

}
