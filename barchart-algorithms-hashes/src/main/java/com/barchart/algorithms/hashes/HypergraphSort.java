/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.algorithms.hashes;

import java.util.ArrayList;
import java.util.List;


public class HypergraphSort {

	/** The mythical threshold (or better, a very closed upper bound of): random 3-hypergraphs
	 * are acyclic with high probability if the ratio vertices/edges exceeds this constant. */
	public static final double GAMMA = 1.23;
	
	/** For each vertex, the XOR of the values of the smallest other vertex in each incident 3-hyperedge. */
	public final int[] vertex1;
	/** For each vertex, the XOR of the values of the largest other vertex in each incident 3-hyperedge. */
	public final int[] vertex2;
	
	/** The edge stack. At the end of a successful sorting phase, it contains the hinges in reverse order. */
	public final int[] stack;
	/** The degree of each vertex of the intermediate 3-hypergraph. */
	private final int[] d;
	/** Initial top of the edge stack. */
	private int top;
	/** The stack used for peeling the graph. */
	private final List<Integer> visitStack;
	
	public final int numEdges, numVerts;
	
	public HypergraphSort(final int numEdges) {
		this.numEdges = numEdges;
		
		/* The theoretically sufficient number of vertices */
		final int m = (int)Math.ceil(GAMMA * numEdges) + 1;
		
		/* This guarantees that the number of vertices is a multiple of 3 */
		numVerts = m + ( 3 - m % 3 ) % 3;
		
		vertex1 = new int[numVerts];
		vertex2 = new int[numVerts];
		
		stack = new int[numEdges];
		d = new int[numVerts];
		
		visitStack = new ArrayList<Integer>(1024);
		
	}
	
	public boolean generateAndSort(final long[][] keys, final long seed) {
		
		/* Clean */
		for(int i = 0; i < numVerts; i++) {
			d[i] = 0;
			vertex1[i] = 0;
			vertex2[i] = 0;
		}
		
		final int[] e = new int[3];
		
		for(long[] key : keys) {
			
			keyToEdge(key, seed, e);
			
			/* Store the edge */
			xorEdge(e[0], e[1], e[2], false);
			
			d[e[0]]++;
			d[e[1]]++;
			d[e[2]]++;
			
		}
		
		return sort();
	}
	
	public void keyToEdge(final long[] triple, final long seed, final int e[] ) {
		final int partSize = (int)(numVerts * 0xAAAAAAABL >>> 33);
		final Jenkins hasher = new Jenkins(seed);
		final long[] hash = hasher.hash(triple);
		e[0] = (int)((hash[0] & 0x7FFFFFFFFFFFFFFFL) % partSize);
		e[1] = (int)(partSize + (hash[1] & 0x7FFFFFFFFFFFFFFFL) % partSize);
		e[2] = (int)((partSize << 1) + (hash[2] & 0x7FFFFFFFFFFFFFFFL) % partSize);
	}
	
	private boolean sort() {
		
		top = 0;
		for(int i = 0; i < numVerts; i++) {
			
			if(d[i] == 1) {
				peel(i);
			}
			
		}
		
		if(top != numEdges) {
			return false;
		}
		
		return true;
	}
	
	private void peel(final int x) {
		
		int v;
		visitStack.clear();
		visitStack.add(x);
		
		while(!visitStack.isEmpty()) {
			
			v = visitStack.remove(visitStack.size() - 1);
			if(d[v] == 1) {
				stack[top++] = v;
				--d[v];
				
				xorEdge(v, vertex1[v], vertex2[v], true);
				
				if(--d[vertex1[v]] == 1) {
					visitStack.add(vertex1[v]);
				}
				
				if(--d[vertex2[v]] == 1) {
					visitStack.add(vertex2[v]);				
				}
				
			}
			
		}
		
	}
	
	private final void xorEdge(final int x, final int y, final int z, boolean partial) {
		
		if(!partial) {
			if(y < z) {
				vertex1[x] ^= y;
				vertex2[x] ^= z;
			} else {
				vertex1[x] ^= z;
				vertex2[x] ^= y;
			}
		}

		if(x < z) {
			vertex1[y] ^= x;
			vertex2[y] ^= z;
		} else {
			vertex1[y] ^= z;
			vertex2[y] ^= x;
		}
		
		if(x < y) {
			vertex1[z] ^= x;
			vertex2[z] ^= y;
		} else {
			vertex1[z] ^= y;
			vertex2[z] ^= x;
		}
		
	}
	
}




