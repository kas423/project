
package edu.dhbw.andar;


public class GenericFunctions {
	/**
	 * tests if an integer is power of two
	 * source: http://www.devmaster.net/forums/showthread.php?t=1728
	 * @param value
	 * @return
	 */
	static final boolean isPowerOfTwo (int value)	{
		if(value != 0) {
			return (value & -value) == value;
		} else {
			return false;
		}
	}
	
	/**
	 * returns the smallest power of two that is greater than
	 * or equal to the absolute value of x
	 * @param x
	 * @return
	 */
	static final public int nextPowerOfTwo(int x) {
		double val = (double) x;
		return (int) Math.pow(2, Math.ceil(log2(val)));
	}
	
	/**
	 * return the log of x base 2
	 * @param x
	 * @return
	 */
	static final public double log2(double x) {
		return Math.log(x)/Math.log(2);
	}
}