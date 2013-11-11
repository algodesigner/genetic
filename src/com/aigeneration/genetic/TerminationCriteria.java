package com.aigeneration.genetic;

/**
 * TerminationCriteria class contains criteria for termination of a genetic
 * search. The class is intended to be used by <tt>EvolutionEngine</tt>.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class TerminationCriteria {

  private long maxTime;
  private long maxGenerations;

  /**
   * Constructs a TerminationCriteria object.
   * @param maxTime The maximum time allowed for a genetic search. Use -1 if
   *                time constraint is not to be applied
   * @param maxGenerations The maximum number of generations allowed in a
   *                       genetic search. Use -1 if not this limit is not to be
   *                       applied
   */
  public TerminationCriteria(long maxTime, long maxGenerations) {
    this.maxTime = maxTime;
    this.maxGenerations = maxGenerations;
  }

  /**
   * Returns the maximum time allowed for a genetic search
   * @return the maximum time allowed for a genetic search
   */
  public long getMaxTime() {
    return maxTime;
  }

  /**
   * Returns the maximum number of generations allowed in a genetic search
   * @return the maximum number of generations
   */
  public long getMaxGenerations() {
    return maxGenerations;
  }
}