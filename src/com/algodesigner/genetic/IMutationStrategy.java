package com.algodesigner.genetic;

/**
 * Mutation Strategy interface definition.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface IMutationStrategy {

  /**
   * Mutates the specified chromosome.
   * 
   * @param offspring a chromosome to be mutated.
   * @return returns a new mutated instance of the chromosome.
   */
  Chromosome mutate(Chromosome offspring);
}
