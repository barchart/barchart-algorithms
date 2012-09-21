package com.barchart.algorithms.hashes;

public class HashKey {

	private long first;
	private long second;
	private long third;
	
	public HashKey() {
		first = 0;
		second = 0;
		third = 0;
	}
	
	public HashKey(final long first, final long second, final long third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public long getFirst() {
		return first;
	}

	public void setFirst(long first) {
		this.first = first;
	}

	public long getSecond() {
		return second;
	}

	public void setSecond(long second) {
		this.second = second;
	}

	public long getThird() {
		return third;
	}

	public void setThird(long third) {
		this.third = third;
	}
	
	@Override
	public String toString() {
		return first + "\t" + second + "\t" + third;
	}
	
	
	
}
