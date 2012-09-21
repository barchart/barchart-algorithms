package com.barchart.algorithms.hashes;

public class Edge {

	private int first, second, third;
	
	public Edge() {
		first = 0;
		second = 0;
		third = 0;
	}
	
	public Edge(final int o, final int t, final int th) {
		first = o;
		second = t; 
		third = th;
	}

	public int get(final int i) {
		switch(i) {
		case 0:
			return first;
		case 1:
			return second;
		case 2:
			return third;
		default:
			throw new RuntimeException();
		}
	}
	
	public final int getFirst() {
		return first;
	}

	public final void setFirst(int first) {
		this.first = first;
	}

	public final int getSecond() {
		return second;
	}

	public final void setSecond(int second) {
		this.second = second;
	}

	public final int getThird() {
		return third;
	}

	public final void setThird(int third) {
		this.third = third;
	}
	
	@Override
	public String toString() {
		return first + " " + second + " " + third;
	}
	
	@Override
	public int hashCode() {
		int temp = 17;
		temp = temp * 37 + first;
		temp = temp * 37 + second;
		temp = temp * 37 + third;
		return temp;
	}
	
	@Override
	public boolean equals(final Object that) {
		
		if(!(that instanceof Edge)) {
			return false;
		} else {
			Edge e = (Edge) that;
			if(e.getFirst() == first && 
			   e.getSecond() == second && 
			   e.getThird() == third) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	public static void main(final String[] args) {
		
		Edge t1 = new Edge(0,2,5);
		Edge t2 = new Edge(0,3,5);
		Edge t3 = new Edge(1,3,5);
		Edge t4 = new Edge(0,2,4);
		
		System.out.println(t1.hashCode());
		System.out.println(t2.hashCode());
		System.out.println(t3.hashCode());
		System.out.println(t4.hashCode());
		
		
		
	}
	
}
