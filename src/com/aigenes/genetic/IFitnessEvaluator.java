package com.aigenes.genetic;

/**
 * Fitness Evaluator Interface
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface IFitnessEvaluator {
  
  /**
   * Returns the fitness value of the specified Chromosome.
   * @param chromosome the Chromosome that needs to be evaluated.
   * @return the fitness value.
   */
  public double evaluate(Chromosome chromosome);
}
