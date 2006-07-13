package com.aigeneration.genetic;

/**
 * Termination evaluator helper class.
 * @author Vlad Shurupov
 * @version 1.0
 */
public class TerminationEvaluator {

  private IEvolutionEngine evolutionEngine;
  private long startTime = -1;

  public TerminationEvaluator(IEvolutionEngine evolutionEngine) {
    if (evolutionEngine == null)
      throw new IllegalArgumentException("null");

    this.evolutionEngine = evolutionEngine; 
  }
  
  public void evaluate(TerminationCriteria criteria, int bestIndex)
    throws TerminationException
  {
    // If the timer is not initialised, set it
    if (startTime == -1)
      startTime = System.currentTimeMillis();
    else {
      // We only check the maximum time criteria if the timer was initialised
      // and the time criteria is actually set (i.e. not equal -1)
      if (criteria.getMaxTime() != -1 &&
        criteria.getMaxTime() <= System.currentTimeMillis() - startTime)
      {
        reset();
        throw new TerminationException("Time over", bestIndex);
      }
    }
    // In any event, check the max generations criteria as long as it is
    // set (i.e. not equal -1)
    if (criteria.getMaxGenerations() != -1 &&
      evolutionEngine.getGenerationCount() >= criteria.getMaxGenerations())
    {
      reset();
      throw new TerminationException("Max generations reached", bestIndex);
    }
  }

  /**
   * Resets the object state for further use.
   */
  private void reset() {
    startTime = -1;
  }
}
