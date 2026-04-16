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
 * Defines crossover (recombination) strategies for combining genetic material
 * from parent chromosomes to create offspring in a genetic algorithm. Crossover
 * is a key operator that enables exploration of new solution combinations.
 * <p>
 * Crossover strategies determine how genes from two parents are mixed to
 * produce new chromosomes. Effective crossover balances inheritance of good
 * traits from parents with creation of novel combinations.
 * <p>
 * <strong>Common crossover techniques:</strong>
 * <ul>
 * <li><strong>Single-point crossover:</strong> Swap segments after a random
 * point</li>
 * <li><strong>Multi-point crossover:</strong> Swap multiple segments</li>
 * <li><strong>Uniform crossover:</strong> Randomly select genes from either
 * parent</li>
 * <li><strong>Arithmetic crossover:</strong> Combine gene values
 * mathematically</li>
 * <li><strong>Order-based crossover:</strong> Preserve relative ordering (for
 * permutations)</li>
 * </ul>
 * <p>
 * <strong>Example implementations:</strong>
 * 
 * <pre>
 * // Single-point crossover
 * ICrossoverStrategy singlePointCrossover = (parent1, parent2) -> {
 *   int crossoverPoint = random.nextInt(parent1.size());
 *   Gene[] child1Genes = combineGenes(parent1, parent2, 0, crossoverPoint);
 *   Gene[] child2Genes = combineGenes(parent2, parent1, 0, crossoverPoint);
 *   return new ChromosomePair(new Chromosome(child1Genes),
 *     new Chromosome(child2Genes));
 * };
 * 
 * // Uniform crossover (each gene independently from either parent)
 * ICrossoverStrategy uniformCrossover = (parent1, parent2) -> {
 *   Gene[] child1Genes = new Gene[parent1.size()];
 *   Gene[] child2Genes = new Gene[parent2.size()];
 *   for (int i = 0; i &lt; parent1.size(); i++) {
 *     if (random.nextBoolean()) {
 *       child1Genes[i] = parent1.getGene(i);
 *       child2Genes[i] = parent2.getGene(i);
 *     } else {
 *       child1Genes[i] = parent2.getGene(i);
 *       child2Genes[i] = parent1.getGene(i);
 *     }
 *   }
 *   return new ChromosomePair(new Chromosome(child1Genes),
 *     new Chromosome(child2Genes));
 * };
 * </pre>
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see ChromosomePair
 * @see Chromosome
 * @see DefaultCrossoverStrategy
 * @see EvolutionEngine
 */
public interface ICrossoverStrategy {

  /**
   * Combines two parent chromosomes to produce a pair of offspring chromosomes.
   * The crossover operation mixes genetic material from both parents, creating
   * new solutions that inherit traits from each.
   * <p>
   * Implementations should ensure that offspring chromosomes are valid for the
   * problem domain and maintain any required constraints (e.g., gene types,
   * chromosome length).
   * 
   * @param chromosome1 the first parent chromosome, never {@code null}
   * @param chromosome2 the second parent chromosome, never {@code null}
   * @return a pair of offspring chromosomes created by combining the parents,
   *         never {@code null}
   * @throws NullPointerException if either chromosome is {@code null}
   * @throws IncompatibleChromosomeException if chromosomes cannot be crossed
   *         over (e.g., different lengths, incompatible gene types)
   * @see ChromosomePair
   */
  ChromosomePair crossover(Chromosome chromosome1, Chromosome chromosome2);
}
