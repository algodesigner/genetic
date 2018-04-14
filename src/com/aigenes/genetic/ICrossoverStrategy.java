package com.aigenes.genetic;

/**
 * Crossover Strategy Interface
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface ICrossoverStrategy {

  /**
   * Performs a crossover on two given chromosomes
   * 
   * @param chromosome1 the first chromosome
   * @param chromosome2 the second chromosome
   * @return the resulting offspring
   */
  public ChromosomePair crossover(Chromosome chromosome1,
    Chromosome chromosome2);
}
