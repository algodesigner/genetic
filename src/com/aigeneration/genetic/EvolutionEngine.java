package com.aigeneration.genetic;

/**
 * Evolution engine class.
 * @author Vlad Shurupov
 * @version 1.01
 */
public class EvolutionEngine implements IEvolutionEngine {

  private Generation generation;
  private final ISelector selector;
  private final IMutationStrategy mutationStrategy;
  private final IFitnessEvaluator fitnessEvaluator;
  private final boolean elitismEnabled;
  private TerminationEvaluator terminationEvaluator =
    new TerminationEvaluator(this);

  private long generationCount;
  private int bestIndex;

  
  public EvolutionEngine(Generation generation, double mutationRate,
    IFitnessEvaluator fitnessEvaluator)
  {
    this(generation, new DefaultSelector(),
      new DefaultMutationStrategy(mutationRate), fitnessEvaluator, false);
  }
  
  public EvolutionEngine(Generation generation, double mutationRate,
    IFitnessEvaluator fitnessEvaluator, boolean elitismEnabled)
  {
    this(generation, new DefaultSelector(),
      new DefaultMutationStrategy(mutationRate), fitnessEvaluator,
      elitismEnabled);
  }
  
  public EvolutionEngine(Generation generation, ISelector selector,
    IMutationStrategy mutationStrategy, IFitnessEvaluator fitnessEvaluator,
    boolean elitismEnabled)
  {
    this.generation = generation;
    this.selector = selector;
    this.mutationStrategy = mutationStrategy;
    this.fitnessEvaluator = fitnessEvaluator;
    this.elitismEnabled = elitismEnabled;
  }

  /**
   * Returns the current generation
   * @return the current generation
   */
  public Generation getGeneration() {
    return generation;
  }

  /**
   * Attempts to find a solution through a series of evolutionary steps.
   * @param fitnessTarget the fitness target
   * @param terminationCriteria the termination criteria (<tt>null</tt> if
   *                            not applicable)
   * @return the index of the first chromosome that achieved the target
   *         fitness
   * @throws IncompatibleChromosomeException
   * @throws TerminationException
   */
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria)
    throws IncompatibleChromosomeException, TerminationException
  {
    int index;
    do {
      if (terminationCriteria != null)
        terminationEvaluator.evaluate(terminationCriteria, bestIndex);
      index = step(fitnessTarget);
    } while (index == -1);
    return index;
  }

  /**
   * Makes a single evolutionary step
   * @throws IncompatibleChromosomeException
   */
  public void step() throws IncompatibleChromosomeException {
    step(-1);    
  }

  /**
   * Makes a single evolutionary step.
   * @param fitnessTarget the fitness target
   * @return the index of the first chromosome that achieved the target
   *         fitness
   * @throws IncompatibleChromosomeException
   */
  public int step(double fitnessTarget) throws IncompatibleChromosomeException {

    // Fitness: Evaluate fitness of each individual chromosome
    double[] fitnessScores = new double[generation.size()];
    bestIndex = 0;
    for (int i = 0; i < fitnessScores.length; i++) {
      fitnessScores[i] =
        fitnessEvaluator.evaluate(generation.getChromosome(i));
      if (fitnessScores[i] == fitnessTarget) {
        return i;
      }
      if (fitnessScores[i] > fitnessScores[bestIndex])
        bestIndex = i;
    }
    
    // New population: Produce offsprings that form a new generation
    Chromosome[] offspring = new Chromosome[generation.size()];

    int i = 0;
    if (elitismEnabled) {
      offspring[i++] = generation.getChromosome(bestIndex);
      offspring[i++] = generation.getChromosome(bestIndex);
    }
    for (; i < offspring.length; i += 2) {

      // Selection: Select a parent pair
      ChromosomePair pair = selector.select(generation, fitnessScores);

      // Crossover: Cross over two parents to form a new offspring
      ChromosomePair offspringPair =
        pair.getFirst().crossover(pair.getSecond());

      // Mutation: Mutate new offspring
      offspring[i] = mutationStrategy.mutate(offspringPair.getFirst());
      offspring[i + 1] = mutationStrategy.mutate(offspringPair.getSecond());
    }
    // Replace: Replace the existing generation with a new one
    generation = new Generation(offspring);
    generationCount++;
    return -1;
  }
 
  /**
   * Returns the generation count.
   * @return the generation count
   */
  public long getGenerationCount() {
    return generationCount;
  }
  
  public int getBestIndex() {
    return bestIndex;
  }
}
