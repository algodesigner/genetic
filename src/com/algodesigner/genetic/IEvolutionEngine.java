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
 * Defines the contract for evolution engines that implement genetic
 * optimisation algorithms. This interface provides a standard API for evolving
 * populations of chromosomes towards optimal solutions.
 * <p>
 * Implementations of this interface manage the complete evolutionary process,
 * including fitness evaluation, selection, crossover, mutation, and generation
 * replacement. The interface supports both automatic solution finding and
 * manual step-by-step evolution control.
 * <p>
 * <strong>Key responsibilities:</strong>
 * <ul>
 * <li>Maintain the current generation of chromosomes</li>
 * <li>Apply genetic operators to evolve the population</li>
 * <li>Track evolution progress and best solutions</li>
 * <li>Support termination criteria for automated evolution</li>
 * </ul>
 * <p>
 * This interface enables polymorphism and custom implementations while
 * maintaining compatibility with the standard {@link EvolutionEngine}.
 * 
 * @author Vlad Shurupov
 * @version 1.01
 * @see EvolutionEngine
 * @see CompositeEvolutionEngine
 * @see Generation
 * @see Chromosome
 */
public interface IEvolutionEngine {

  /**
   * Returns the current generation being evolved by this engine. The generation
   * represents the population at the current evolutionary stage.
   * 
   * @return the current generation, never {@code null}
   * @see #step()
   * @see #getGenerationCount()
   */
  Generation getGeneration();

  /**
   * Evolves the population until a solution meeting the fitness target is found
   * or termination criteria are met. This method automates the evolutionary
   * process.
   * 
   * @param fitnessTarget the minimum fitness value required for a solution
   * @param terminationCriteria criteria for stopping evolution if no solution
   *        is found, or {@code null} to continue indefinitely
   * @return the index of the first chromosome that achieved
   *         {@code fitnessTarget}, or {@code -1} if termination criteria were
   *         met without finding a solution
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if invalid fitness values are produced
   * @see #step(double)
   * @see TerminationCriteria
   */
  int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria);

  /**
   * Performs one complete evolutionary cycle, advancing the population by one
   * generation. This includes evaluation, selection, crossover, mutation, and
   * replacement operations.
   * 
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if invalid fitness values are produced
   * @see #step(double)
   * @see #findSolution(double, TerminationCriteria)
   */
  void step();

  /**
   * Performs one evolutionary cycle and checks if any chromosome achieves the
   * specified fitness target. Returns immediately if a solution is found.
   * 
   * @param fitnessTarget the fitness value to check against, or {@code -1} to
   *        skip checking (equivalent to {@link #step()})
   * @return the index of the first chromosome that achieved
   *         {@code fitnessTarget}, or {@code -1} if no chromosome reached the
   *         target
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over
   * @throws IllegalStateException if invalid fitness values are produced
   * @see #step()
   * @see #findSolution(double, TerminationCriteria)
   */
  int step(double fitnessTarget);

  /**
   * Returns the number of evolutionary cycles completed since this engine was
   * created. This represents how many generations have been processed.
   * 
   * @return the number of completed generations (≥ 0)
   * @see #getGeneration()
   */
  long getGenerationCount();

  /**
   * Returns the index of the chromosome with the highest fitness score in the
   * current generation. The index refers to the position within the generation
   * returned by {@link #getGeneration()}.
   * 
   * @return the index of the fittest chromosome (0-based), or {@code -1} if no
   *         fitness evaluation has been performed
   * @see #getBestFitnessScore()
   * @see #getBestChromosome()
   */
  int getBestIndex();

  /**
   * Returns the highest fitness score achieved by any chromosome in the current
   * generation. This represents the quality of the best solution found so far.
   * 
   * @return the best fitness score, or {@code 0.0} if no fitness evaluation has
   *         been performed
   * @see #getBestIndex()
   * @see #getBestChromosome()
   */
  double getBestFitnessScore();

  /**
   * Returns the chromosome with the highest fitness score in the current
   * generation. This is a convenience method that combines
   * {@link #getBestIndex()} and {@link #getGeneration()}.
   * <p>
   * The default implementation retrieves the chromosome from the current
   * generation using the best index. Implementations may override this for
   * performance or caching purposes.
   * 
   * @return the fittest chromosome in the current generation, or {@code null}
   *         if no fitness evaluation has been performed
   * @see #getBestIndex()
   * @see #getBestFitnessScore()
   */
  default Chromosome getBestChromosome() {
    return getGeneration().getChromosome(getBestIndex());
  }
}