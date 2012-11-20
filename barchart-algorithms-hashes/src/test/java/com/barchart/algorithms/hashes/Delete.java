package com.barchart.algorithms.hashes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Delete {
	
	public static void main(final String[] args) {
		
		final List<Integer> ints = new ArrayList<Integer>();
		final int size = 414;
		
		for(int i = 0; i < size; i++) {
			ints.add(i);
		}
		
		final MPHF func = new MPHF(ints);
		
		for(int i = 0; i < size; i++) {
			System.out.println(i + " maps to " + func.getLong(i));
		}
		
	}
	
	public static byte[] bArray(int data) {
		return new byte[] {
			(byte)((data >> 24) & 0xff),
			(byte)((data >> 16) & 0xff),
			(byte)((data >> 8) & 0xff),
			(byte)((data >> 0) & 0xff),
		};
	}

}
