package com.aigeneration.genetic;

/**
 * TerminationException would normally be thrown by <tt>TerminationEvaluator
 * </tt> when the termination criteria is met.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class TerminationException extends Exception {

  private static final long serialVersionUID = 0;
  
  private final int bestIndex;

  public TerminationException(String message, int bestIndex) {
    super(message);
    if (bestIndex < 0)
      throw new IllegalArgumentException("best index cannot be less than 0");
    this.bestIndex = bestIndex;
  }
  
  public int getBestIndex() {
    return bestIndex;
  }
}
