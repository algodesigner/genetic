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
 * Generation class is a collection of species represented by Chromosomes.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class Generation {

  private final Chromosome[] chromosomes;

  public Generation(Chromosome[] chromosomes) {

    if (chromosomes == null)
      throw new IllegalArgumentException("chromosomes cannot be null");
    if (chromosomes.length % 2 != 0)
      throw new IllegalArgumentException(
        "The size of the generation must be even");

    this.chromosomes = chromosomes;
  }

  /**
   * Retrieves a Chromosome by its index.
   * 
   * @param index the index of the Chromosome.
   * @return Chromosome with the specified index.
   */
  public Chromosome getChromosome(int index) {
    return chromosomes[index];
  }

  /**
   * Returns the number of Chromosomes in this generation
   * 
   * @return the number of Chromosomes
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
