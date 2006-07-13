package com.aigeneration.genetic;

/**
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface IMutationStrategy {
  public Chromosome mutate(Chromosome offspring);
}
