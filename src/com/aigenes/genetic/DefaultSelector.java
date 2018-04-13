package com.aigenes.genetic;

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
