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
 * Default mutation strategy implementation
 * 
 * @author Vlad Shurupov
 * @version 1.1
 */
public class DefaultMutationStrategy implements IMutationStrategy {

  private final double mutationRate;
  private final Random random;

  /**
   * Constructs this mutation strategy.
   * 
   * @param mutationRate the mutation rate; cannot be less than zero.
   */
  public DefaultMutationStrategy(double mutationRate) {
    this(mutationRate, new Random());
  }

  /**
   * Constructs this mutation strategy.
   * 
   * @param mutationRate the mutation rate; cannot be less than zero.
   * @param random a pseudorandom number generator; cannot be {@code null}.
   */
  public DefaultMutationStrategy(double mutationRate, Random random) {
    if (mutationRate < 0)
      throw new IllegalArgumentException(
        "crossover rate cannot be less than zero");
    this.mutationRate = mutationRate;
    this.random = Objects.requireNonNull(random);
  }

  @Override
  public Chromosome mutate(Chromosome offspring) {
    Gene[] genes = offspring.getGenes();
    if (random.nextDouble() < mutationRate) {
      int index1 = random.nextInt(genes.length);
      int index2 = random.nextInt(genes.length);
      Gene temp = genes[index1];
      genes[index1] = genes[index2];
      genes[index2] = temp;
      return new Chromosome(genes);
    } else {
      return offspring;
    }
  }
}
