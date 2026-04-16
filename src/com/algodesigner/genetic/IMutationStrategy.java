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
 * Functional interface that defines mutation strategies for randomly modifying
 * chromosomes in a genetic algorithm. Mutation introduces new genetic material
 * and helps maintain population diversity.
 * <p>
 * Mutation is a source of innovation in evolutionary algorithms, allowing
 * exploration of new regions in the solution space that might not be reachable
 * through crossover alone. Effective mutation balances small, incremental
 * changes with occasional larger jumps.
 * <p>
 * <strong>Common mutation techniques:</strong>
 * <ul>
 * <li><strong>Bit-flip mutation:</strong> Invert bits in binary
 * representations</li>
 * <li><strong>Gaussian mutation:</strong> Add small random noise to numerical
 * values</li>
 * <li><strong>Swap mutation:</strong> Exchange two genes in a chromosome</li>
 * <li><strong>Scramble mutation:</strong> Randomly reorder a segment of
 * genes</li>
 * <li><strong>Boundary mutation:</strong> Set gene to minimum or maximum
 * value</li>
 * </ul>
 * <p>
 * <strong>Example implementations:</strong>
 * 
 * <pre>
 * // Gaussian mutation for numerical genes
 * IMutationStrategy gaussianMutation = chromosome -> {
 *   Gene[] mutatedGenes = new Gene[chromosome.size()];
 *   for (int i = 0; i &lt; chromosome.size(); i++) {
 *     double value = ((Number)chromosome.getGene(i).getValue()).doubleValue();
 *     double mutatedValue = value + random.nextGaussian() * mutationStrength;
 *     mutatedGenes[i] = new Gene(mutatedValue);
 *   }
 *   return new Chromosome(mutatedGenes);
 * };
 * 
 * // Swap mutation (for permutation problems)
 * IMutationStrategy swapMutation = chromosome -> {
 *   Gene[] genes = chromosome.getGenes();
 *   int i = random.nextInt(genes.length);
 *   int j = random.nextInt(genes.length);
 *   // Swap genes at positions i and j
 *   Gene temp = genes[i];
 *   genes[i] = genes[j];
 *   genes[j] = temp;
 *   return new Chromosome(genes);
 * };
 * </pre>
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see Chromosome
 * @see DefaultMutationStrategy
 * @see EvolutionEngine
 */
@FunctionalInterface
public interface IMutationStrategy {

  /**
   * Applies mutation to a chromosome, creating a modified version with
   * potentially improved or novel characteristics. Mutation should introduce
   * small, random changes while preserving most of the chromosome's structure.
   * <p>
   * The mutation rate (probability of mutation) is typically controlled by the
   * strategy implementation or configuration. Mutations should be reversible in
   * principle, allowing the algorithm to explore the solution space
   * effectively.
   * 
   * @param offspring the chromosome to mutate, never {@code null}
   * @return a new chromosome with mutations applied, never {@code null}
   * @throws NullPointerException if {@code offspring} is {@code null}
   * @throws ClassCastException if chromosome genes cannot be mutated as
   *         expected
   */
  Chromosome mutate(Chromosome offspring);
}
