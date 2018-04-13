package com.aigenes.genetic;

/**
 * Fitness function interface definition.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface IFitnessFunction {

  /**
   * Returns the fitness value of the specified Chromosome.
   * 
   * @param chromosome the Chromosome that needs to be evaluated.
   * @return the fitness value.
   */
  public double apply(Chromosome chromosome);
}
