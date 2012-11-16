package com.barchart.algorithms.hashes;

import java.util.ArrayList;
import java.util.List;

public class Delete {
	
	public static void main(final String[] args) {
		
		final List<Integer> ints = new ArrayList<Integer>();
		
		for(int i = 0; i < 6; i++) {
			ints.add(i);
		}
		
		final MPHF func = new MPHF(ints);
		
		for(int i = 0; i < 6; i++) {
			System.out.println(i + " maps to " + func.getLong(i));
		}
		
	}

	
}
