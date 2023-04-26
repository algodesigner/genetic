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

import java.util.ArrayList;
import java.util.List;

/**
 * Generation builder builds {@link Generation} objects based on chromosomes.
 * 
 * @author Vlad Shurupov
 * @version 0.1
 */
public class GenerationBuilder {

  private final List<Chromosome> chromosomes;

  /**
   * Constructs this builder.
   */
  public GenerationBuilder() {
    this.chromosomes = new ArrayList<>();
  }

  /**
   * Adds the specified number of the string representation of a chromosome.
   * 
   * @param instances the number of instances
   * @param geneString a string representation of a chromosome
   */
  public void addChromosomes(int instances, String geneString) {
    for (int i = 0; i < instances; i++)
      addChromosome(geneString);
  }

  /**
   * Adds a string representation of a chromosome.
   * 
   * @param geneString a string representation of a chromosome
   */
  public void addChromosome(String geneString) {
    chromosomes.add(new Chromosome(geneString));
  }

  /**
   * Builds a new instance of {@link Generation}.
   * 
   * @return a new instance of the Generation.
   */
  public Generation build() {
    Chromosome[] cs = chromosomes.toArray(new Chromosome[chromosomes.size()]);
    return new Generation(cs);
  }

  /**
   * Resets this builder by removing the known chromosomes if any.
   */
  public void reset() {
    chromosomes.clear();
  }
}
