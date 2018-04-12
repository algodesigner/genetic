package com.aigenes.genetic;

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

  /**
   * Constructs this Exception.
   * @param message the Exception message.
   * @param bestIndex the index of the fittest Chromosome.
   */
  public TerminationException(String message, int bestIndex) {
    super(message);
    if (bestIndex < 0)
      throw new IllegalArgumentException("best index cannot be less than 0");
    this.bestIndex = bestIndex;
  }

  /**
   * Returns the index of the fittest Chromosome.
   * @return the index of the fittest Chromosome.
   */
  public int getBestIndex() {
    return bestIndex;
  }
}
