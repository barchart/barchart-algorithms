package com.barchart.algorithms.hashes;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestMPHF {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testSmallVals() {
		
		final List<Integer> ints = new ArrayList<Integer>();
		
		for(int i = -10; i < 11; i++) {
			ints.add(i);
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testMedVals() {
		
		final List<Integer> ints = new ArrayList<Integer>();
		
		for(int i = -10; i < 11; i++) {
			ints.add(i * 100);
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testLargeVals() {
		
		final List<Integer> ints = new ArrayList<Integer>();
		
		for(int i = -10; i < 11; i++) {
			ints.add(i * 100000);
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	@Test
	public void testLargeSet() {
		
		final List<Integer> ints = new ArrayList<Integer>();
		
		for(int i = 0; i < 5000; i++) {
			ints.add(i*10);
		}
		
		final MPHF func = new MPHF(ints);
		
		assertTrue(testMPH(ints, func));
		
	}
	
	private boolean testMPH(final List<Integer> ints, final MPHF func) {
		
		final Map<Long, AtomicInteger> hashCount = new
				TreeMap<Long, AtomicInteger>();
		
		for(long i = 0; i < ints.size(); i++) {
			hashCount.put(i, new AtomicInteger(0));
		}
		
		for(final Integer i : ints) {
			hashCount.get(func.getLong(i)).getAndIncrement();
		}

		for(final Entry<Long, AtomicInteger> e : hashCount.entrySet()) {
			if(e.getValue().get() != 1) {
				
				for(final Entry<Long, AtomicInteger> ex : hashCount.entrySet()) {
					System.out.println(ex.getKey() + " mapped " + ex.getValue().get() + " times");
				}
				return false;
			}
		}
		
		return true;
	}

}
