/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2008-2023, Vlad Shurupov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.algodesigner.genetic;

import java.util.Objects;
import java.util.Random;

/**
 * The core engine that drives the genetic optimisation process, implementing
 * the complete evolutionary cycle of selection, crossover, mutation, and
 * replacement. This class orchestrates the interaction between chromosomes,
 * generations, and genetic operators to evolve solutions towards optimal
 * fitness.
 * <p>
 * The evolution engine follows the standard genetic algorithm workflow:
 * <ol>
 * <li><strong>Initialisation:</strong> Create an initial population of
 * chromosomes</li>
 * <li><strong>Evaluation:</strong> Calculate fitness for each chromosome</li>
 * <li><strong>Selection:</strong> Choose parent chromosomes based on
 * fitness</li>
 * <li><strong>Crossover:</strong> Combine parent chromosomes to create
 * offspring</li>
 * <li><strong>Mutation:</strong> Randomly modify offspring chromosomes</li>
 * <li><strong>Replacement:</strong> Form new generation from offspring</li>
 * <li><strong>Termination:</strong> Check stopping criteria and repeat if
 * needed</li>
 * </ol>
 * <p>
 * <strong>Example usage:</strong>
 * 
 * <pre>
 * // Create initial generation
 * Generation initialGeneration = new Generation(initialChromosomes);
 * 
 * // Configure evolution engine
 * EvolutionEngine engine = new EvolutionEngine(initialGeneration,
 *   0.8,  // Crossover rate
 *   0.01, // Mutation rate
 *   new MyFitnessFunction(), true // Enable elitism
 * );
 * 
 * // Run evolution until target fitness is reached
 * int solutionIndex = engine.findSolution(0.95, null);
 * 
 * // Or run step-by-step
 * for (int i = 0; i < 100; i++) {
 *   engine.step();
 *   System.out.println("Generation " + engine.getGenerationCount()
 *     + ": Best fitness = " + engine.getBestFitnessScore());
 * }
 * </pre>
 * <p>
 * This implementation supports customisable selection, crossover, and mutation
 * strategies, allowing fine-grained control over the evolutionary process.
 * 
 * @author Vlad Shurupov
 * @version 1.03
 * @see Generation
 * @see Chromosome
 * @see ISelector
 * @see ICrossoverStrategy
 * @see IMutationStrategy
 * @see IFitnessFunction
 * @see CompositeEvolutionEngine
 */
public class EvolutionEngine implements IEvolutionEngine {

  private final ISelector selector;
  private final ICrossoverStrategy crossoverStrategy;
  private final IMutationStrategy mutationStrategy;
  private final IFitnessFunction fitnessFunction;
  private final boolean elitismEnabled;
  private TerminationEvaluator terminationEvaluator;

  private Generation generation;
  private long generationCount;
  private int bestIndex;
  private double bestFitnessScore;

  /**
   * Constructs a new evolution engine with default selection, crossover, and
   * mutation strategies. This is the simplest constructor for basic genetic
   * algorithm usage.
   * <p>
   * The crossover rate controls how frequently parent chromosomes are combined
   * to create offspring (typically 0.6-0.9). The mutation rate controls how
   * frequently genes are randomly modified (typically 0.001-0.1).
   * <p>
   * Elitism is disabled by default, meaning the best chromosomes from each
   * generation are not automatically preserved.
   * 
   * @param generation the initial generation containing the starting population
   * @param crossoverRate the probability of crossover occurring (0.0 to 1.0)
   * @param mutationRate the probability of mutation occurring (0.0 to 1.0)
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @throws NullPointerException if any parameter is {@code null}
   * @see #EvolutionEngine(Generation, double, double, IFitnessFunction,
   *      boolean)
   * @see DefaultSelector
   * @see DefaultCrossoverStrategy
   * @see DefaultMutationStrategy
   */
  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessFunction, false);
  }

  /**
   * Constructs a new evolution engine with default strategies and configurable
   * elitism. Elitism preserves the best chromosomes from each generation
   * unchanged, preventing loss of good solutions during evolution.
   * <p>
   * When elitism is enabled, the two best chromosomes are copied directly to
   * the next generation without modification. This can accelerate convergence
   * but may reduce population diversity if overused.
   * 
   * @param generation the initial generation containing the starting population
   * @param crossoverRate the probability of crossover occurring (0.0 to 1.0)
   * @param mutationRate the probability of mutation occurring (0.0 to 1.0)
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @param elitismEnabled {@code true} to preserve best chromosomes,
   *        {@code false} otherwise
   * @throws NullPointerException if any parameter is {@code null}
   * @see #EvolutionEngine(Generation, double, double, IFitnessFunction,
   *      boolean, Random)
   */
  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction,
    boolean elitismEnabled)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessFunction,
      elitismEnabled);
  }

  /**
   * Constructs a new evolution engine with default strategies, configurable
   * elitism, and a custom random number generator. This constructor is useful
   * for:
   * <ul>
   * <li>Reproducible experiments (using seeded Random)</li>
   * <li>Performance optimisation (using faster Random implementations)</li>
   * <li>Testing with controlled randomness</li>
   * </ul>
   * <p>
   * The random number generator is used by selection, crossover, and mutation
   * strategies for all stochastic decisions in the evolutionary process.
   * 
   * @param generation the initial generation containing the starting population
   * @param crossoverRate the probability of crossover occurring (0.0 to 1.0)
   * @param mutationRate the probability of mutation occurring (0.0 to 1.0)
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @param elitismEnabled {@code true} to preserve best chromosomes,
   *        {@code false} otherwise
   * @param random the random number generator to use for stochastic operations
   * @throws NullPointerException if any parameter is {@code null}
   * @see #EvolutionEngine(Generation, ISelector, ICrossoverStrategy,
   *      IMutationStrategy, IFitnessFunction, boolean)
   */
  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction,
    boolean elitismEnabled, Random random)
  {
    this(generation, new DefaultSelector(random),
      new DefaultCrossoverStrategy(crossoverRate, random),
      new DefaultMutationStrategy(mutationRate, random), fitnessFunction,
      elitismEnabled);
  }

  /**
   * Constructs a new evolution engine with fully customisable components. This
   * constructor provides maximum flexibility for advanced genetic algorithm
   * configurations.
   * <p>
   * Each component can be customised:
   * <ul>
   * <li><strong>Selector:</strong> Controls how parents are chosen (e.g.,
   * tournament, roulette)</li>
   * <li><strong>Crossover Strategy:</strong> Defines how parent chromosomes are
   * combined</li>
   * <li><strong>Mutation Strategy:</strong> Specifies how offspring chromosomes
   * are modified</li>
   * <li><strong>Fitness Function:</strong> Evaluates how good each solution
   * is</li>
   * </ul>
   * <p>
   * This constructor is ideal for implementing specialised genetic algorithms
   * or experimenting with novel evolutionary approaches.
   * 
   * @param generation the initial generation containing the starting population
   * @param selector the selection strategy for choosing parent chromosomes
   * @param crossoverStrategy the strategy for combining parent chromosomes
   * @param mutationStrategy the strategy for modifying offspring chromosomes
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @param elitismEnabled {@code true} to preserve best chromosomes,
   *        {@code false} otherwise
   * @throws NullPointerException if any parameter is {@code null}
   * @see ISelector
   * @see ICrossoverStrategy
   * @see IMutationStrategy
   * @see IFitnessFunction
   */
  public EvolutionEngine(Generation generation, ISelector selector,
    ICrossoverStrategy crossoverStrategy, IMutationStrategy mutationStrategy,
    IFitnessFunction fitnessFunction, boolean elitismEnabled)
  {
    this.generation = Objects.requireNonNull(generation);
    this.selector = Objects.requireNonNull(selector);
    this.crossoverStrategy = Objects.requireNonNull(crossoverStrategy);
    this.mutationStrategy = Objects.requireNonNull(mutationStrategy);
    this.fitnessFunction = Objects.requireNonNull(fitnessFunction);
    this.elitismEnabled = elitismEnabled;
    this.terminationEvaluator = new TerminationEvaluator(this);
  }

  /**
   * Returns the current generation being evolved. The generation represents the
   * population at the current point in the evolutionary process.
   * <p>
   * After each call to {@link #step()} or {@link #step(double)}, the generation
   * is replaced with a new one containing evolved chromosomes. The returned
   * generation is typically ordered by fitness (best first).
   * 
   * @return the current generation, never {@code null}
   * @see #step()
   * @see #step(double)
   * @see #getGenerationCount()
   */
  @Override
  public Generation getGeneration() {
    return generation;
  }

  /**
   * Evolves the population until a chromosome achieves the target fitness or
   * termination criteria are met. This method repeatedly calls
   * {@link #step(double)} until a solution is found or evolution stops.
   * <p>
   * The method returns when:
   * <ul>
   * <li>A chromosome achieves fitness ≥ {@code fitnessTarget}</li>
   * <li>Termination criteria are met (e.g., maximum generations reached)</li>
   * <li>An error occurs during evolution</li>
   * </ul>
   * <p>
   * If termination criteria are provided and met before the target fitness is
   * achieved, the method returns {@code -1} indicating no solution was found.
   * 
   * @param fitnessTarget the minimum fitness value required for a solution
   * @param terminationCriteria criteria for stopping evolution if no solution
   *        is found, or {@code null} to continue indefinitely
   * @return the index of the first chromosome that achieved the target fitness,
   *         or {@code -1} if termination criteria were met without finding a
   *         solution
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if a chromosome produces NaN fitness
   * @see #step(double)
   * @see TerminationCriteria
   * @see TerminationEvaluator
   */
  @Override
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria)
  {
    int index;
    do {
      if (terminationCriteria != null
        && !terminationEvaluator.evaluate(terminationCriteria, bestIndex))
        return -1;
      index = step(fitnessTarget);
    } while (index == -1);
    return index;
  }

  /**
   * Performs one complete evolutionary cycle: evaluation, selection, crossover,
   * mutation, and replacement. This advances the population by one generation.
   * <p>
   * The step includes:
   * <ol>
   * <li>Evaluate fitness of all chromosomes</li>
   * <li>Select parent pairs based on fitness</li>
   * <li>Create offspring through crossover</li>
   * <li>Apply mutation to offspring</li>
   * <li>Form new generation (with elitism if enabled)</li>
   * </ol>
   * <p>
   * Use this method for manual control of the evolutionary process or when
   * implementing custom termination logic.
   * 
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if a chromosome produces NaN fitness
   * @see #step(double)
   * @see #findSolution(double, TerminationCriteria)
   */
  @Override
  public void step() {
    step(-1);
  }

  /**
   * Performs one evolutionary cycle and checks if any chromosome achieves the
   * specified fitness target. This method combines evolution with immediate
   * solution checking.
   * <p>
   * If a chromosome achieves fitness ≥ {@code fitnessTarget}, the method
   * returns immediately with the chromosome's index. Otherwise, it returns
   * {@code -1} and continues with the next generation.
   * <p>
   * This method is useful for implementing early termination when a
   * satisfactory solution is found, avoiding unnecessary computation.
   * 
   * @param fitnessTarget the fitness value to check against, or {@code -1} to
   *        skip checking (equivalent to {@link #step()})
   * @return the index of the first chromosome that achieved
   *         {@code fitnessTarget}, or {@code -1} if no chromosome reached the
   *         target
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if a chromosome produces NaN fitness
   * @see #step()
   * @see #findSolution(double, TerminationCriteria)
   */
  @Override
  public int step(double fitnessTarget) {

    // Fitness: Evaluate fitness of each individual chromosome
    double[] fitnessScores = new double[generation.size()];
    bestIndex = -1;
    bestFitnessScore = 0;
    for (int i = 0; i < fitnessScores.length; i++) {
      fitnessScores[i] = fitnessFunction.apply(generation.getChromosome(i));
      if (Double.isNaN(fitnessScores[i]))
        throw new IllegalStateException(
          "Invalid score (NaN) for chromosome: " + generation.getChromosome(i));
      if (bestIndex == -1 || fitnessScores[i] > fitnessScores[bestIndex]) {
        bestIndex = i;
        bestFitnessScore = fitnessScores[i];
      }
      if (fitnessScores[i] >= fitnessTarget - 1e-8) {
        return i;
      }
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
        crossoverStrategy.crossover(pair.getFirst(), pair.getSecond());

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
   * {@inheritDoc}
   * <p>
   * This implementation returns the count of completed evolutionary cycles
   * since this engine was constructed. The count is incremented after each
   * successful call to {@link #step()} or {@link #step(double)}.
   */
  @Override
  public long getGenerationCount() {
    return generationCount;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation returns the index of the chromosome with the highest
   * fitness score from the most recent fitness evaluation. The index is updated
   * during each call to {@link #step()} or {@link #step(double)}.
   */
  @Override
  public int getBestIndex() {
    return bestIndex;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This implementation returns the fitness score of the best chromosome from
   * the most recent fitness evaluation. The score is updated during each call
   * to {@link #step()} or {@link #step(double)}.
   */
  @Override
  public double getBestFitnessScore() {
    return bestFitnessScore;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(256);
    sb.append(getClass().getSimpleName() + "\n");
    int size = generation.size();
    for (int i = 0; i < size; i++) {
      Chromosome c = generation.getChromosome(i);
      sb.append(c.toString());
      sb.append(' ');
      sb.append(fitnessFunction.apply(c));
      sb.append('\n');
    }
    sb.append("Generation #: " + getGenerationCount());
    sb.append('\n');
    sb.append("Best index: " + getBestIndex());
    sb.append('\n');
    sb.append("Best Chromosome:\n" + getBestChromosome() + '\n');
    sb.append("Best fitness: " + fitnessFunction.apply(getBestChromosome()));
    return sb.toString();
  }
}
