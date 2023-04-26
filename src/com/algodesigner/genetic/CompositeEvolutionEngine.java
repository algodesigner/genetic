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
