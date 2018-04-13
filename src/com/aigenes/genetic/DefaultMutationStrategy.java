package com.aigenes.genetic;

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
