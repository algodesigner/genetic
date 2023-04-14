package com.algodesigner.genetic;

/**
 * IEvolutionEngine Interface
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public interface IEvolutionEngine {

  /**
   * Returns the current generation
   * 
   * @return the current generation
   */
  public Generation getGeneration();

  /**
   * Attempts to find a solution through a series of evolutionary steps.
   * 
   * @param fitnessTarget the fitness target
   * @param terminationCriteria the termination criteria (<tt>null</tt> if not
   *        applicable)
   * @return the index of the first chromosome that achieved the target fitness
   * @throws IncompatibleChromosomeException
   */
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria);

  /**
   * Makes a single evolutionary step
   * 
   * @throws IncompatibleChromosomeException
   */
  public void step();

  /**
   * Makes a single evolutionary step.
   * 
   * @param fitnessTarget the fitness target
   * @return the index of the first chromosome that achieved the target fitness
   * @throws IncompatibleChromosomeException
   */
  public int step(double fitnessTarget);

  /**
   * Returns the generation count.
   * 
   * @return the generation count
   */
  public long getGenerationCount();

  /**
   * Returns the index of the fittest chromosome in the contained generation.
   * 
   * @return the index of the fittest chromosome.
   */
  public int getBestIndex();

  /**
   * Returns the best fitness score (i.e. the score of the fittest chromosome)
   * in the contained generation.
   * 
   * @return the best fitness score.
   */
  public double getBestFitnessScore();

}