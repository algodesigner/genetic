package com.aigeneration.genetic;

/**
 * Generation class implementation
 * @author Vlad Shurupov
 * @version 1.0
 */
public class Generation {

  private Chromosome[] chromosomes;

  public Generation(Chromosome[] chromosomes) {

    if (chromosomes == null)
      throw new IllegalArgumentException("Constructor does not accept null");

    if (chromosomes.length % 2 != 0)
      throw new IllegalArgumentException("The size of the generation must" +
        "be even");

    this.chromosomes = chromosomes;
  }

  public Chromosome getChromosome(int index) {
    return chromosomes[index];
  }

  /**
   * Returns the number of chromosomes in the generation
   * @return the number of chromosomes
   */
  public int size() {
    return chromosomes.length;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < chromosomes.length; i++) {
      stringBuffer.append(chromosomes[i]);
      stringBuffer.append("\n");
    }
    return stringBuffer.toString();
  }
}
