package com.aigeneration.genetic;

import java.util.Random;

/**
 * Default mutation strategy implementation
 * @author Vlad Shurupov
 * @version 1.0
 */
public class DefaultMutationStrategy implements IMutationStrategy {

  private double mutationRate;
  private Random random = new Random();

  public DefaultMutationStrategy(double mutationRate) {
    this.mutationRate = mutationRate;
  }

  /**
   * @see com.aigeneration.genetic.IMutationStrategy#mutate(com.aigeneration.genetic.Chromosome)
   */
  public Chromosome mutate(Chromosome offspring) {
    Gene[] genes = offspring.getGenes();
    if (random.nextDouble() < mutationRate) {    
      int index1 = random.nextInt(genes.length);
      int index2 = random.nextInt(genes.length);
      Gene temp = genes[index1];
      genes[index1] = genes[index2];
      genes[index2] = temp;
      return new Chromosome(genes, offspring.getCrossoverRate());
    } else {
      return offspring;
    }
  }
}
