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

/**
 * An advanced evolution engine that implements the island model genetic
 * algorithm, maintaining multiple independent subpopulations (islands) that
 * evolve in parallel. This approach enhances genetic diversity and can escape
 * local optima more effectively than single-population approaches.
 * <p>
 * The composite engine creates multiple {@link EvolutionEngine} instances, each
 * operating on the same initial population but evolving independently. This
 * parallel evolution increases exploration of the solution space while
 * maintaining multiple evolutionary trajectories.
 * <p>
 * <strong>Island model benefits:</strong>
 * <ul>
 * <li><strong>Increased diversity:</strong> Independent islands maintain
 * distinct gene pools</li>
 * <li><strong>Parallel exploration:</strong> Multiple search directions
 * explored simultaneously</li>
 * <li><strong>Escape local optima:</strong> Different islands may converge to
 * different solutions</li>
 * <li><strong>Fault tolerance:</strong> Poor performance in one island doesn't
 * affect others</li>
 * </ul>
 * <p>
 * <strong>Example usage:</strong>
 * 
 * <pre>
 * // Create composite engine with 4 parallel subpopulations
 * CompositeEvolutionEngine engine =
 *   new CompositeEvolutionEngine(initialGeneration, 0.8, // Crossover rate
 *     0.01, // Mutation rate
 *     new MyFitnessFunction(), true, // Enable elitism
 *     4 // Number of parallel evolution engines
 *   );
 * 
 * // Run evolution - all subpopulations evolve in parallel
 * int solutionIndex = engine.findSolution(0.95, null);
 * </pre>
 * <p>
 * This implementation is particularly effective for complex optimisation
 * problems where maintaining population diversity is challenging.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see EvolutionEngine
 * @see IEvolutionEngine
 * @see Generation
 */
public class CompositeEvolutionEngine implements IEvolutionEngine {

  private final IEvolutionEngine[] engines;
  private Generation generation;
  private IEvolutionEngine bestEngine;

  /**
   * Constructs a composite evolution engine with fully customisable components
   * for each subpopulation. This constructor provides maximum control over the
   * evolutionary process across all islands.
   * <p>
   * Each sub-engine (island) is created with identical configuration but
   * evolves independently due to stochastic variations in selection and genetic
   * operations.
   * 
   * @param generation the initial generation shared by all sub-engines
   * @param selector the selection strategy for choosing parent chromosomes
   * @param crossoverStrategy the strategy for combining parent chromosomes
   * @param mutationStrategy the strategy for modifying offspring chromosomes
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @param elitismEnabled {@code true} to preserve best chromosomes in each
   *        island
   * @param numOfAgents the number of parallel evolution engines (islands) to
   *        create
   * @throws NullPointerException if any strategy parameter is {@code null}
   * @throws IllegalArgumentException if {@code numOfAgents} is less than 1
   * @see #CompositeEvolutionEngine(Generation, double, double,
   *      IFitnessFunction, boolean, int)
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
   * Constructs a composite evolution engine with default strategies for all
   * subpopulations. This is the simplest constructor for island model usage.
   * <p>
   * All sub-engines use identical default selection, crossover, and mutation
   * strategies with the specified rates. The engines evolve independently but
   * start from the same initial population.
   * 
   * @param generation the initial generation shared by all sub-engines
   * @param crossoverRate the probability of crossover occurring (0.0 to 1.0)
   * @param mutationRate the probability of mutation occurring (0.0 to 1.0)
   * @param fitnessFunction the function that evaluates chromosome fitness
   * @param elitismEnabled {@code true} to preserve best chromosomes in each
   *        island
   * @param numOfAgents the number of parallel evolution engines (islands) to
   *        create
   * @throws NullPointerException if any parameter is {@code null}
   * @throws IllegalArgumentException if {@code numOfAgents} is less than 1
   * @see DefaultSelector
   * @see DefaultCrossoverStrategy
   * @see DefaultMutationStrategy
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
    TerminationCriteria terminationCriteria)
  {
    double bestOfBestScores = -Double.MIN_VALUE;

    boolean completed = true;
    for (int i = 0; i < engines.length; i++) {
      completed &=
        engines[i].findSolution(fitnessTarget, terminationCriteria) > -1;
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
