package com.aigeneration.genetic;

import java.util.Random;

/**
 * @author Vlad Shurupov
 * @version 1.01
 */
public class DefaultCrossoverStrategy implements ICrossoverStrategy {

  private Random random = new Random();

  public DefaultCrossoverStrategy() {
  }

  /**
   * Performs a crossover on two given chromosomes
   * @param chromosome1 the first chromosome
   * @param chromosome2 the second chromosome
   * @return the resulting offspring
   */
  public ChromosomePair crossover(Chromosome chromosome1,
    Chromosome chromosome2) throws IncompatibleChromosomeException
  {
    if (random.nextDouble() < chromosome1.getCrossoverRate()) {

    if (!chromosome1.isCompatible(chromosome2))
      throw new IncompatibleChromosomeException("Crossover cannot be " +
        "as parent chromosomes are not compatible");

      int position = random.nextInt(chromosome1.length());
      Gene[] geneSet1 = chromosome1.getGenes();
      Gene[] geneSet2 = chromosome2.getGenes();
      for (int i = position; i < chromosome1.length(); i++) {
        Gene temp = geneSet1[i];
        geneSet1[i] = geneSet2[i];
        geneSet2[i] = temp;
      }
      return new ChromosomePair(
        new Chromosome(geneSet1, chromosome1.getCrossoverRate()),
        new Chromosome(geneSet2, chromosome1.getCrossoverRate()));
    } else {
      return new ChromosomePair(chromosome1, chromosome2);
    }
  }
}
