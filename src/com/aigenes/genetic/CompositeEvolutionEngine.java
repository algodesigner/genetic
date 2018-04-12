package com.aigenes.genetic;

public class CompositeEvolutionEngine implements IEvolutionEngine {
  
  private final IEvolutionEngine[] engines;
  private Generation generation;
  private IEvolutionEngine bestEngine;
  
  /**
   * Constructs this composite Evolution Engine.
   * 
   * @param generation the initial generation.
   * @param selector the chromosome selection strategy.
   * @param crossoverStrategy the crossover strategy.
   * @param mutationStrategy the chromosome mutation strategy.
   * @param fitnessEvaluator the fitness evaluator (a.k.a. fitness function)
   * @param elitismEnabled <code>true</code> if elitism should be employed by
   *        this instance.
   * @param numOfAgents the number of sub-engines the work is delegated to.
   */
  public CompositeEvolutionEngine(Generation generation, ISelector selector,
    ICrossoverStrategy crossoverStrategy, IMutationStrategy mutationStrategy,
    IFitnessEvaluator fitnessEvaluator, boolean elitismEnabled,
    int numOfAgents)
  {
    this.generation = generation;
    this.engines = new IEvolutionEngine[numOfAgents];
    for (int i = 0; i < numOfAgents; i++) {
      this.engines[i] = createEngine(generation, selector, crossoverStrategy,
        mutationStrategy, fitnessEvaluator, elitismEnabled);
    }
  }
  
  /**
   * Constructs this composite Evolution Engine.
   * 
   * @param generation the initial generation.
   * @param crossoverRate the crossover rate.
   * @param mutationRate the chromosome mutation rate.
   * @param fitnessEvaluator the fitness evaluator (a.k.a. fitness function)
   * @param elitismEnabled <code>true</code> if elitism should be employed by
   *        this instance.
   * @param numOfAgents the number of sub-engines the work is delegated to.
   */
  public CompositeEvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessEvaluator fitnessEvaluator,
    boolean elitismEnabled, int numOfAgents)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessEvaluator,
      elitismEnabled, numOfAgents);
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#getGeneration()
   */
  @Override
  public Generation getGeneration() {
    return bestEngine != null ? bestEngine.getGeneration() : generation;
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#findSolution(double, com.aigenes.genetic.TerminationCriteria)
   */
  @Override
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria)
    throws IncompatibleChromosomeException, TerminationException
  {
    // TODO Avoid object creation here. Create a new member...
    TerminationException[] exceptions = new TerminationException[engines.length];
    double bestOfBestScores = -Double.MIN_VALUE;
    int bestEngineIndex = 0;
    
    for (int i = 0; i < engines.length; i++) {
      try {
        engines[i].findSolution(fitnessTarget, terminationCriteria);
      } catch (TerminationException e) {
        exceptions[i] = e;
      }
      final double bestScore = engines[i].getBestFitnessScore();
      if (bestScore > bestOfBestScores) {
        bestOfBestScores = bestScore;
        bestEngine = engines[i];
        bestEngineIndex = i;
      }
    }
    // If a engine with the best results has not been identified, return
    // "null" index
    if (bestEngine == null)
      return -1;
    // If the engine with the best results threw a TerminationException, we
    // need to re-throw it
    if (exceptions[bestEngineIndex] != null)
      throw exceptions[bestEngineIndex];
    return bestEngine.getBestIndex();
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#step()
   */
  @Override
  public void step() throws IncompatibleChromosomeException {
    for (int i = 0; i < engines.length; i++)
      engines[i].step();
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#step(double)
   */
  @Override
  public int step(double fitnessTarget) throws IncompatibleChromosomeException {
    for (int i = 0; i < engines.length; i++)
      engines[i].step(fitnessTarget);
    return 0;
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#getGenerationCount()
   */
  @Override
  public long getGenerationCount() {
    return bestEngine != null ? bestEngine.getGenerationCount() : 0;
  }

  /**
   * @see com.aigenes.genetic.IEvolutionEngine#getBestIndex()
   */
  @Override
  public int getBestIndex() {
    // TODO Legalise -1 return value in the interface
    return bestEngine != null ? bestEngine.getBestIndex() : -1;
  }
  
  /**
   * @see com.aigenes.genetic.IEvolutionEngine#getBestFitnessScore()
   */
  @Override
  public double getBestFitnessScore() {
    // TODO Legalise Double.NaN return value in the interface definition
    return bestEngine != null ? bestEngine.getBestFitnessScore() : Double.NaN;
  }    
  
  private static IEvolutionEngine createEngine(Generation generation,
    ISelector selector, ICrossoverStrategy crossoverStrategy,
    IMutationStrategy mutationStrategy, IFitnessEvaluator fitnessEvaluator,
    boolean elitismEnabled)
  {
    return new EvolutionEngine(generation, selector, crossoverStrategy,
      mutationStrategy, fitnessEvaluator, elitismEnabled);
  }
}
