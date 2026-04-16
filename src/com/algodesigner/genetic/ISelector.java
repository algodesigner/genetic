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
 * Functional interface that defines selection strategies for choosing parent
 * chromosomes in a genetic algorithm. Selection is a critical component that
 * determines which chromosomes reproduce and pass their genes to the next
 * generation.
 * <p>
 * Selection strategies balance exploration (trying new solutions) and
 * exploitation (refining good solutions). Common approaches include:
 * <ul>
 * <li><strong>Fitness-proportionate selection:</strong> Probability
 * proportional to fitness</li>
 * <li><strong>Tournament selection:</strong> Choose best from random
 * subset</li>
 * <li><strong>Rank-based selection:</strong> Probability based on rank rather
 * than raw fitness</li>
 * <li><strong>Truncation selection:</strong> Choose only from top
 * performers</li>
 * </ul>
 * <p>
 * <strong>Example implementations:</strong>
 * 
 * <pre>
 * // Tournament selection: pick best of random k chromosomes
 * ISelector tournamentSelector = (generation, fitnessScores) -> {
 *   int k = 3;
 *   int best1 = randomTournamentWinner(generation, fitnessScores, k);
 *   int best2 = randomTournamentWinner(generation, fitnessScores, k);
 *   return new ChromosomePair(generation.getChromosome(best1),
 *     generation.getChromosome(best2));
 * };
 * 
 * // Roulette wheel selection: probability proportional to fitness
 * ISelector rouletteSelector = (generation, fitnessScores) -> {
 *   int parent1 = selectByRouletteWheel(fitnessScores);
 *   int parent2 = selectByRouletteWheel(fitnessScores);
 *   return new ChromosomePair(generation.getChromosome(parent1),
 *     generation.getChromosome(parent2));
 * };
 * </pre>
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see ChromosomePair
 * @see Generation
 * @see DefaultSelector
 * @see EvolutionEngine
 */
@FunctionalInterface
public interface ISelector {

  /**
   * Selects a pair of parent chromosomes from the current generation for
   * reproduction. The selection should favour fitter chromosomes while
   * maintaining sufficient diversity in the population.
   * <p>
   * The method receives fitness scores for all chromosomes in the generation,
   * typically ordered with the best chromosome first. Implementations should
   * use these scores to make selection decisions.
   * 
   * @param generation the current generation containing candidate chromosomes
   * @param fitnessScores fitness scores for all chromosomes in the generation,
   *        where {@code fitnessScores[i]} corresponds to
   *        {@code generation.getChromosome(i)}
   * @return a pair of selected parent chromosomes, never {@code null}
   * @throws NullPointerException if either parameter is {@code null}
   * @throws IllegalArgumentException if array sizes don't match or scores are
   *         invalid
   * @see ChromosomePair
   */
  ChromosomePair select(Generation generation, double[] fitnessScores);
}
