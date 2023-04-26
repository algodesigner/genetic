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

import java.util.Objects;
import java.util.Random;

/**
 * Default implementation of the genetic crossover strategy.
 * 
 * @author Vlad Shurupov
 * @version 1.01
 */
public class DefaultCrossoverStrategy implements ICrossoverStrategy {

  private final double crossoverRate;
  private final Random random;

  /**
   * Constructs this crossover strategy.
   * 
   * @param crossoverRate the crossover rate; cannot be less than zero.
   * @param random a pseudorandom number generator; cannot be code {@code null}.
   */
  public DefaultCrossoverStrategy(double crossoverRate) {
    this(crossoverRate, new Random());
  }

  /**
   * Constructs this crossover strategy.
   * 
   * @param crossoverRate the crossover rate; cannot be less than zero.
   * @param random a pseudorandom number generator; cannot be code {@code null}.
   */
  public DefaultCrossoverStrategy(double crossoverRate, Random random) {
    if (crossoverRate < 0)
      throw new IllegalArgumentException(
        "crossover rate cannot be less than zero");
    this.crossoverRate = crossoverRate;
    this.random = Objects.requireNonNull(random);
  }

  /**
   * Performs a crossover on two given chromosomes
   * 
   * @param chromosome1 the first chromosome
   * @param chromosome2 the second chromosome
   * @return the resulting offspring
   */
  public ChromosomePair crossover(Chromosome chromosome1,
    Chromosome chromosome2)
  {
    if (random.nextDouble() < crossoverRate) {

      if (!chromosome1.isCompatible(chromosome2))
        throw new IncompatibleChromosomeException(
          "Crossover cannot be " + "as parent chromosomes are not compatible");

      int position = random.nextInt(chromosome1.length());
      Gene[] geneSet1 = chromosome1.getGenes();
      Gene[] geneSet2 = chromosome2.getGenes();
      for (int i = position; i < chromosome1.length(); i++) {
        Gene temp = geneSet1[i];
        geneSet1[i] = geneSet2[i];
        geneSet2[i] = temp;
      }
      return new ChromosomePair(new Chromosome(geneSet1),
        new Chromosome(geneSet2));
    } else {
      return new ChromosomePair(chromosome1, chromosome2);
    }
  }
}
