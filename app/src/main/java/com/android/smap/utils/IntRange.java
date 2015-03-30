package com.android.smap.utils;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

/**
 * 
 * Simple Iterable to loop through a range of integers
 * 
 * eg. Print numbers 1 to 10
 * 
 * for (int n : IntRange.between(1, 10)) {
 *     System.out.println(n);
 * }
 * 
 * @author christopheratkins
 *
 */
public class IntRange implements Iterable<Integer> {
	
	private final int startValue;
	private final int finalValue;
	
	
	public static IntRange between(int from, int to) {
		return new IntRange(from, to);
	}
	
	private IntRange(int startValue, int finalValue) {
		this.startValue = startValue;
		this.finalValue = finalValue;
		
		if (startValue > finalValue) {
			throw new IllegalArgumentException("Starting value must be smaller than final value");
		}
	}

	@Override
	public Iterator<Integer> iterator() {
		
		return new AbstractIterator<Integer>() {
			private int current = startValue;
			private int end = finalValue;
			
			@Override
			protected Integer computeNext() {
				
				if (current <= end) {
					int ret = current;
					++current;
					return ret;
				}
				
				return endOfData();
			}
		};
	}

}
