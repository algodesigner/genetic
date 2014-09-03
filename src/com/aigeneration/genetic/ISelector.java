package com.aigeneration.genetic;

/**
 * Selects chromosomes for reproduction.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface ISelector {

  /**
   * Selects a pair of chromosomes for reproduction.
   * @param generation a generation from which a pair of chromosomes is to be
   *        selectted.
   * @param fitnessScores a set of fitness scores used by the selection
   *        process.
   * @return a selected pair of chromosomes (cannot be {@code null}).
   */
  public ChromosomePair select(Generation generation, double[] fitnessScores);
}
