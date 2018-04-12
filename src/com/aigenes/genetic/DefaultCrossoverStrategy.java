package com.aigenes.genetic;

import java.util.Random;

/**
 * Default implementation of the genetic crossover strategy.
 * 
 * @author Vlad Shurupov
 * @version 1.01
 */
public class DefaultCrossoverStrategy implements ICrossoverStrategy {

  private final double crossoverRate;
  private final Random random = new Random();

  /**
   * Constructs this crossover strategy.
   * 
   * @param crossoverRate the crossover rate; cannot be less than zero.
   */
  public DefaultCrossoverStrategy(double crossoverRate) {
    if (crossoverRate < 0)
      throw new IllegalArgumentException(
        "crossover rate cannot be less" + " than zero");
    this.crossoverRate = crossoverRate;
  }

  /**
   * Performs a crossover on two given chromosomes
   * 
   * @param chromosome1 the first chromosome
   * @param chromosome2 the second chromosome
   * @return the resulting offspring
   */
  public ChromosomePair crossover(Chromosome chromosome1,
    Chromosome chromosome2) throws IncompatibleChromosomeException
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
