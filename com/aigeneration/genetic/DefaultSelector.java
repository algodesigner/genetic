package com.aigeneration.genetic;

import java.util.Random;

/**
 * @author Vlad Shurupov
 * @version 1.0
 */
public class DefaultSelector implements ISelector {

  private Random random = new Random();

  public DefaultSelector() {
  }

  /**
   * @see com.aigeneration.genetic.ISelector#select(com.aigeneration.genetic.Generation, double[])
   */
  public ChromosomePair select(Generation generation, double[] fitnessScores) {

    // Select the indices for the parents. The same parent cannot be selected
    // twice.
    int firstParentIndex = selectSingle(fitnessScores);
    int secondParentIndex = 0;
    do {
      secondParentIndex = selectSingle(fitnessScores);      
    } while (secondParentIndex == firstParentIndex);

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
