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
 * Represents a chromosome in a genetic algorithm, consisting of an immutable
 * sequence of {@link Gene} objects.
 * 
 * <p>
 * A chromosome is the fundamental unit of a genetic algorithm solution,
 * encoding potential solutions to optimisation problems as sequences of genes.
 * Each gene represents a component or parameter of the solution.
 * </p>
 * 
 * <p>
 * Chromosomes support genetic operations including:
 * <ul>
 * <li><strong>Crossover</strong>: Combining genetic material with another
 * chromosome to produce offspring, delegated to {@link ICrossoverStrategy}</li>
 * <li><strong>Compatibility checking</strong>: Determining if two chromosomes
 * can undergo crossover based on structural constraints</li>
 * <li><strong>Fitness evaluation</strong>: Being assessed by an
 * {@link IFitnessFunction} to determine solution quality</li>
 * </ul>
 * </p>
 * 
 * <p>
 * In genetic algorithm terminology, a chromosome corresponds to an individual
 * in the population, while genes represent the individual's traits or
 * characteristics. The chromosome's fitness determines its likelihood of being
 * selected for reproduction in subsequent generations.
 * </p>
 * 
 * <p>
 * <strong>Example usage:</strong>
 * 
 * <pre>{@code
 * // Create a chromosome from a binary string
 * Chromosome chromosome = new Chromosome("0101010101");
 * 
 * // Check compatibility with another chromosome
 * Chromosome other = new Chromosome("1010101010");
 * boolean compatible = chromosome.isCompatible(other);
 * 
 * // Access individual genes
 * Gene gene = chromosome.getGene(0);
 * }</pre>
 * </p>
 * 
 * @author Vlad Shurupov
 * @version 1.02
 * @see Gene
 * @see ChromosomePair
 * @see ICrossoverStrategy
 * @see IFitnessFunction
 */
public class Chromosome {

  private final Gene[] genes;

  /**
   * Constructs a Chromosome from a string representation where each character
   * corresponds to a gene value.
   * 
   * <p>
   * Each character in the string is converted to a {@link Gene} object with the
   * character as its value. This is commonly used for binary or character-based
   * genetic representations.
   * </p>
   * 
   * <p>
   * <strong>Example:</strong>
   * 
   * <pre>{@code
   * // Binary chromosome
   * Chromosome binaryChromosome = new Chromosome("010101");
   * 
   * // Character-based chromosome
   * Chromosome charChromosome = new Chromosome("ABCDEF");
   * }</pre>
   * </p>
   * 
   * @param geneString string representation of genes, where each character
   *        becomes a gene value
   * @throws IllegalArgumentException if {@code geneString} is {@code null}
   * @see #toGenes(String)
   */
  public Chromosome(String geneString) {
    this(toGenes(geneString));
  }

  /**
   * Constructs a Chromosome from an array of {@link Gene} objects.
   * 
   * <p>
   * This constructor creates a chromosome that directly references the provided
   * gene array. The chromosome assumes ownership of the array and will use it
   * directly without copying.
   * </p>
   * 
   * <p>
   * <strong>Note:</strong> The chromosome does not create a defensive copy of
   * the gene array. Callers should ensure the array is not modified externally
   * after chromosome creation to maintain immutability.
   * </p>
   * 
   * @param genes the array of genes that constitute this chromosome
   * @throws IllegalArgumentException if {@code genes} is {@code null}
   */
  public Chromosome(Gene[] genes) {
    if (genes == null)
      throw new IllegalArgumentException("null");
    this.genes = genes;
  }

  /**
   * Returns the length of the chromosome.
   * 
   * @return the length of the chromosome
   */
  public int length() {
    return genes.length;
  }

  /**
   * Returns the gene at a specific index.
   * 
   * @param index the index of the gene
   * @return the gene
   */
  public Gene getGene(int index) {
    return genes[index];
  }

  /**
   * Returns the genes contained in this chromosome.
   * 
   * @return an array of genes
   */
  public Gene[] getGenes() {
    Gene[] geneCopies = new Gene[genes.length];
    System.arraycopy(genes, 0, geneCopies, 0, genes.length);
    return geneCopies;
  }

  /**
   * Tests if this chromosome is compatible with a given chromosome.
   * 
   * @param chromosome the chromosome to test compatibility against
   * @return true, if compatible
   */
  public boolean isCompatible(Chromosome chromosome) {
    return chromosome != null && length() == chromosome.length();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(32);
    for (int i = 0; i < genes.length; i++)
      builder.append(genes[i].toString());
    return builder.toString();
  }

  /**
   * Converts a string into a gene sequence.
   * 
   * @param geneString a string with each character representing a gene.
   * @return a gene sequence.
   */
  public static Gene[] toGenes(String geneString) {
    if (geneString == null)
      throw new IllegalArgumentException("null");
    Gene[] genes = new Gene[geneString.length()];
    for (int i = 0; i < genes.length; i++)
      genes[i] = new Gene(geneString.charAt(i));
    return genes;
  }
}
