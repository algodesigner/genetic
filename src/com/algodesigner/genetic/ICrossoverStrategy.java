package com.algodesigner.genetic;

/**
 * Crossover Strategy Interface
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface ICrossoverStrategy {

  /**
   * Performs a crossover on two given chromosomes.
   * 
   * @param chromosome1 the first chromosome; cannot be {@code null}
   * @param chromosome2 the second chromosome; cannot be {@code null}
   * @return the resulting offspring
   */
  ChromosomePair crossover(Chromosome chromosome1, Chromosome chromosome2);
}
