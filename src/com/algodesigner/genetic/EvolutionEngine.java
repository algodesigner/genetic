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
 * Evolution engine class.
 * 
 * @author Vlad Shurupov
 * @version 1.03
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
   * Constructs this evolution engine.
   * 
   * @param generation the initial generation.
   * @param crossoverRate the crossover rate.
   * @param mutationRate the mutation rate.
   * @param fitnessFunction the fitness function.
   */
  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessFunction, false);
  }

  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction,
    boolean elitismEnabled)
  {
    this(generation, new DefaultSelector(),
      new DefaultCrossoverStrategy(crossoverRate),
      new DefaultMutationStrategy(mutationRate), fitnessFunction,
      elitismEnabled);
  }

  public EvolutionEngine(Generation generation, double crossoverRate,
    double mutationRate, IFitnessFunction fitnessFunction,
    boolean elitismEnabled, Random random)
  {
    this(generation, new DefaultSelector(random),
      new DefaultCrossoverStrategy(crossoverRate, random),
      new DefaultMutationStrategy(mutationRate, random), fitnessFunction,
      elitismEnabled);
  }

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
   * Returns the current generation
   * 
   * @return the current generation
   */
  @Override
  public Generation getGeneration() {
    return generation;
  }

  /**
   * Attempts to find a solution through a series of evolutionary steps.
   * 
   * @param fitnessTarget the fitness target
   * @param terminationCriteria the termination criteria ({@code null} if not
   *        applicable)
   * @return the index of the first chromosome that achieved the target fitness
   * @throws IncompatibleChromosomeException if incompatible chromosomes are
   *         encountered
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
   * Makes a single evolutionary step
   * 
   * @throws IncompatibleChromosomeException if incompatible chromosomes are
   *         encountered
   */
  @Override
  public void step() {
    step(-1);
  }

  /**
   * Makes a single evolutionary step.
   * 
   * @param fitnessTarget the fitness target
   * @return the index of the first chromosome that achieved the target fitness
   * @throws IncompatibleChromosomeException if incompatible chromosomes are
   *         encountered
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

  @Override
  public long getGenerationCount() {
    return generationCount;
  }

  @Override
  public int getBestIndex() {
    return bestIndex;
  }

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
