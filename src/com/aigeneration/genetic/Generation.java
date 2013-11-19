package com.aigeneration.genetic;

/**
 * Generation class is a collection of species represented by Chromosomes.
 * @author Vlad Shurupov
 * @version 1.0
 */
public class Generation {

  private final Chromosome[] chromosomes;

  public Generation(Chromosome[] chromosomes) {

    if (chromosomes == null)
      throw new IllegalArgumentException("chromosomes cannot be null");
    if (chromosomes.length % 2 != 0)
      throw new IllegalArgumentException("The size of the generation must" +
        "be even");

    this.chromosomes = chromosomes;
  }

  /**
   * Retrieves a Chromosome by its index.
   * @param index the index of the Chromosome.
   * @return Chromosome with the specified index.
   */
  public Chromosome getChromosome(int index) {
    return chromosomes[index];
  }

  /**
   * Returns the number of Chromosomes in this generation
   * @return the number of Chromosomes
   */
  public int size() {
    return chromosomes.length;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < chromosomes.length; i++) {
      builder.append(chromosomes[i]);
      builder.append("\n");
    }
    return builder.toString();
  }
}
