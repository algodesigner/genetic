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
 * Represents a single generation in a genetic algorithm, containing a
 * population of {@link Chromosome} instances that compete and evolve over time.
 * Each generation represents one iteration of the evolutionary cycle in the
 * optimisation process.
 * <p>
 * In genetic optimisation, generations are analogous to biological generations
 * where populations evolve through selection, crossover, and mutation. The size
 * of each generation must be even to facilitate pairing for crossover
 * operations.
 * <p>
 * <strong>Example usage:</strong>
 * 
 * <pre>
 * // Create chromosomes for a population
 * Chromosome[] population =
 *   new Chromosome[]
 *   { new Chromosome(gene1, gene2, gene3),
 *     new Chromosome(gene4, gene5, gene6),
 *     new Chromosome(gene7, gene8, gene9),
 *     new Chromosome(gene10, gene11, gene12) };
 * 
 * // Create a generation from the population
 * Generation generation = new Generation(population);
 * 
 * // Access individual chromosomes
 * Chromosome bestChromosome = generation.getChromosome(0);
 * int populationSize = generation.size();
 * </pre>
 * <p>
 * This class provides read-only access to chromosomes and enforces an even
 * population size to ensure proper pairing during genetic operations.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see Chromosome
 * @see EvolutionEngine
 * @see GenerationBuilder
 */
public class Generation {

  private final Chromosome[] chromosomes;

  /**
   * Constructs a new {@code Generation} from the specified array of
   * chromosomes. The generation represents a population at a specific point in
   * the evolutionary process and must contain an even number of chromosomes to
   * facilitate pairing for crossover operations.
   * <p>
   * The chromosomes array is stored internally and can be accessed via
   * {@link #getChromosome(int)}. The array is not copied, so modifications to
   * the original array will affect the generation.
   * 
   * @param chromosomes array of chromosomes representing the population, must
   *        not be {@code null} and must have even length
   * @throws IllegalArgumentException if {@code chromosomes} is {@code null} or
   *         has odd length
   * @see #getChromosome(int)
   * @see #size()
   */
  public Generation(Chromosome[] chromosomes) {

    if (chromosomes == null)
      throw new IllegalArgumentException("chromosomes cannot be null");
    if (chromosomes.length % 2 != 0)
      throw new IllegalArgumentException(
        "The size of the generation must be even");

    this.chromosomes = chromosomes;
  }

  /**
   * Returns the chromosome at the specified position in this generation.
   * Chromosomes are typically ordered by fitness (best first) after evaluation,
   * allowing easy access to the most promising solutions.
   * <p>
   * The index must be within the range {@code 0} to {@code size()-1}. Accessing
   * chromosomes by index is useful for selection algorithms and fitness
   * analysis.
   * 
   * @param index the index of the chromosome to retrieve (0-based)
   * @return the chromosome at the specified index
   * @throws ArrayIndexOutOfBoundsException if the index is out of range
   * @see #size()
   */
  public Chromosome getChromosome(int index) {
    return chromosomes[index];
  }

  /**
   * Returns the number of chromosomes in this generation. The size is always
   * even to ensure proper pairing for crossover operations.
   * <p>
   * The population size is a critical parameter in genetic algorithms that
   * affects diversity, convergence speed, and computational requirements.
   * Larger populations maintain more diversity but require more computation per
   * generation.
   * 
   * @return the number of chromosomes in this generation (always even)
   * @see #getChromosome(int)
   */
  public int size() {
    return chromosomes.length;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < chromosomes.length; i++) {
      builder.append(chromosomes[i]);
      builder.append("\n");
    }
    return builder.toString();
  }
}
