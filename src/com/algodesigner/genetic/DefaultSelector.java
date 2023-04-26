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
 * Default implementation of the chromosome selection strategy.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class DefaultSelector implements ISelector {

  private static final int MAX_SELECTIONS = 5;

  private final Random random;

  public DefaultSelector() {
    this(new Random());
  }

  /**
   * Constructs this chromosome pair selector.
   * 
   * @param random a pseudorandom number generator; cannot be {@code null}.
   */
  public DefaultSelector(Random random) {
    this.random = Objects.requireNonNull(random);
  }

  @Override
  public ChromosomePair select(Generation generation, double[] fitnessScores) {

    if (fitnessScores.length < 2)
      throw new IllegalArgumentException("fitnessScores array is too short");

    // Select the indices for the parents. The same parent cannot be selected
    // twice.
    int firstParentIndex = selectSingle(fitnessScores);
    int secondParentIndex = 0;

    for (int i = 0; i < MAX_SELECTIONS; i++) {
      secondParentIndex = selectSingle(fitnessScores);
      if (secondParentIndex != firstParentIndex)
        break;
    }

    // If the second index still collides, shift it
    if (secondParentIndex == firstParentIndex)
      secondParentIndex = secondParentIndex == fitnessScores.length - 1 ? 0
        : secondParentIndex + 1;

    return new ChromosomePair(generation.getChromosome(firstParentIndex),
      generation.getChromosome(secondParentIndex));
  }

  private int selectSingle(double[] fitnessScores) {

    // Calculate the total score
    double totalScore = 0;
    for (int i = 0; i < fitnessScores.length; i++)
      totalScore += fitnessScores[i];

    if (totalScore == 0)
      return random.nextInt(fitnessScores.length);

    // Slice the distribution
    double slice = random.nextDouble() * totalScore;

    // Identify the "winning" entry
    double runningScore = 0;
    for (int i = 0; i < fitnessScores.length; i++) {
      runningScore += fitnessScores[i];
      if (runningScore >= slice)
        return i;
    }
    throw new IllegalStateException("Selection failed");
  }
}
