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
 * Represents a single gene in a genetic algorithm, which is the fundamental
 * unit of information in a chromosome. A gene holds a value that contributes to
 * the overall solution representation and can be any {@link Object} type,
 * allowing for flexible problem modelling.
 * <p>
 * In genetic optimisation, genes are analogous to biological genes that carry
 * hereditary information. They form the building blocks of chromosomes
 * (solutions) and are subject to genetic operations such as mutation and
 * crossover during the evolutionary process.
 * <p>
 * <strong>Example usage:</strong>
 * 
 * <pre>
 * // Create genes representing different solution components
 * Gene objectGene = new Gene("Polygon");
 * Gene numberGene = new Gene(42);
 * Gene coordinateGene = new Gene(new Point(10, 20));
 * 
 * // Genes can be combined into a chromosome
 * Chromosome chromosome =
 *   new Chromosome(objectGene, numberGene, coordinateGene);
 * </pre>
 * <p>
 * This implementation enforces non-null values for genes to maintain data
 * integrity throughout the evolutionary process. Gene equality is based on
 * value equality, not reference equality.
 * 
 * @author Vlad Shurupov
 * @version 1.01
 * @see Chromosome
 * @see Generation
 * @see EvolutionEngine
 */
public class Gene {

  private final Object value;

  /**
   * Constructs a new {@code Gene} with the specified value. The value
   * represents the information carried by this gene and can be any non-null
   * object type.
   * <p>
   * Gene values should be chosen to appropriately represent the problem domain.
   * For example:
   * <ul>
   * <li>Integer or Double values for numerical optimisation problems</li>
   * <li>String values for scheduling or routing problems</li>
   * <li>Custom objects for complex solution representations</li>
   * </ul>
   * <p>
   * The value is stored as-is and equality comparisons use
   * {@link Object#equals(Object)}.
   * 
   * @param value the gene value, must not be {@code null}
   * @throws IllegalArgumentException if {@code value} is {@code null}
   * @see #getValue()
   */
  public Gene(Object value) {
    if (value == null)
      throw new IllegalArgumentException("Gene value cannot be null");
    this.value = value;
  }

  /**
   * Returns the value stored in this gene. The returned value is the same
   * object that was passed to the constructor, allowing direct access to the
   * gene's information.
   * <p>
   * This method provides read-only access to the gene value. To modify a gene's
   * value, create a new {@code Gene} instance with the desired value.
   * 
   * @return the value of this gene, never {@code null}
   * @see #Gene(Object)
   */
  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * Compares this gene to the specified object for equality.
   * 
   * @param object the object to compare with
   * @return {@code true} if the objects are equal, {@code false} otherwise
   * @see #hashCode()
   */
  @Override
  public boolean equals(Object object) {
    if (object == null)
      return false;
    if (object == this)
      return true;
    if (!object.getClass().equals(getClass()))
      return false;
    Gene gene = (Gene)object;
    return value.equals(gene.getValue());
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
