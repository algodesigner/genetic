package com.aigeneration.genetic;

/**
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface ISelector {

  /**
   * @param generation
   * @param fitnessScores
   * @return
   */
  public ChromosomePair select(Generation generation, double[] fitnessScores);
}
