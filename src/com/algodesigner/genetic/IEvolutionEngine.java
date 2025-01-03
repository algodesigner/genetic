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
 * IEvolutionEngine Interface
 * 
 * @author Vlad Shurupov
 * @version 1.01
 */
public interface IEvolutionEngine {

  /**
   * Returns the current generation
   * 
   * @return the current generation
   */
  Generation getGeneration();

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
  int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria);

  /**
   * Makes a single evolutionary step
   * 
   * @throws IncompatibleChromosomeException if incompatible chromosomes are
   *         encountered
   */
  void step();

  /**
   * Makes a single evolutionary step.
   * 
   * @param fitnessTarget the fitness target
   * @return the index of the first chromosome that achieved the target fitness
   * @throws IncompatibleChromosomeException if incompatible chromosomes are
   *         encountered
   */
  int step(double fitnessTarget);

  /**
   * Returns the generation count.
   * 
   * @return the generation count
   */
  long getGenerationCount();

  /**
   * Returns the index of the fittest chromosome in the contained generation.
   * 
   * @return the index of the fittest chromosome.
   */
  int getBestIndex();

  /**
   * Returns the best fitness score (i.e. the score of the fittest chromosome)
   * in the contained generation.
   * 
   * @return the best fitness score.
   */
  double getBestFitnessScore();

  /**
   * Returns the chromosome with the best fitness score.
   * 
   * @return the chromosome with the best fitness score.
   */
  default Chromosome getBestChromosome() {
    return getGeneration().getChromosome(getBestIndex());
  }
}