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
   * @param fitnessFunction the fitness function (a.k.a. fitness function)
   * @param elitismEnabled <code>true</code> if elitism should be employed by
   *        this instance.
   * @param numOfAgents the number of sub-engines the work is delegated to.
   */
  public CompositeEvolutionEngine(Generation generation, ISelector selector,
    ICrossoverStrategy crossoverStrategy, IMutationStrategy mutationStrategy,
    IFitnessFunction fitnessFunction, boolean elitismEnabled, int numOfAgents)
  {
    this.generation = generation;
    this.engines = new IEvolutionEngine[numOfAgents];
    for (int i = 0; i < numOfAgents; i++) {
      this.engines[i] = createEngine(generation, selector, crossoverStrategy,
        mutationStrategy, fitnessFunction, elitismEnabled);
    }
  }

  /**
   * Constructs this composite Evolution Engine.
   * 
   * @param generation the initial generation.
   * @param crossoverRate the crossover rate.
   * @param mutationRate the chromosome mutation rate.
   * @param fitnessFunction the fitness function.
   * @param elitismEnabled <code>true</code> if elitism should be employed by
   *        this instance.
   * @param numOfAgents the number of sub-engines the work is delegated to.
   */
  public CompositeEvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction,
    boolean elitismEnabled, int numOfAgents)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessFunction,
      elitismEnabled, numOfAgents);
  }

  @Override
  public Generation getGeneration() {
    return bestEngine != null ? bestEngine.getGeneration() : generation;
  }

  @Override
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria) {
    double bestOfBestScores = -Double.MIN_VALUE;

    boolean completed = true;
    for (int i = 0; i < engines.length; i++) {
      completed &= engines[i].findSolution(fitnessTarget, terminationCriteria) > -1;
      final double bestScore = engines[i].getBestFitnessScore();
      if (bestScore > bestOfBestScores) {
        bestOfBestScores = bestScore;
        bestEngine = engines[i];
      }
    }
    // If a engine with the best results has not been identified, return
    // "null" index
    return bestEngine != null && completed ? bestEngine.getBestIndex() : -1;
  }

  @Override
  public void step() {
    for (int i = 0; i < engines.length; i++)
      engines[i].step();
  }

  @Override
  public int step(double fitnessTarget) {
    for (int i = 0; i < engines.length; i++)
      engines[i].step(fitnessTarget);
    return 0;
  }

  @Override
  public long getGenerationCount() {
    return bestEngine != null ? bestEngine.getGenerationCount() : 0;
  }

  @Override
  public int getBestIndex() {
    // TODO Legalise -1 return value in the interface
    return bestEngine != null ? bestEngine.getBestIndex() : -1;
  }

  @Override
  public double getBestFitnessScore() {
    // TODO Legalise Double.NaN return value in the interface definition
    return bestEngine != null ? bestEngine.getBestFitnessScore() : Double.NaN;
  }

  private static IEvolutionEngine createEngine(Generation generation,
    ISelector selector, ICrossoverStrategy crossoverStrategy,
    IMutationStrategy mutationStrategy, IFitnessFunction fitnessFunction,
    boolean elitismEnabled)
  {
    return new EvolutionEngine(generation, selector, crossoverStrategy,
      mutationStrategy, fitnessFunction, elitismEnabled);
  }
}
