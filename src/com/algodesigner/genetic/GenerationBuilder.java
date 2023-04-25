package com.algodesigner.genetic;

import java.util.ArrayList;
import java.util.List;

/**
 * Generation builder builds {@link Generation} objects based on chromosomes.
 * 
 * @author Vlad Shurupov
 * @version 0.1
 */
public class GenerationBuilder {

  private final List<Chromosome> chromosomes;

  /**
   * Constructs this builder.
   */
  public GenerationBuilder() {
    this.chromosomes = new ArrayList<>();
  }

  /**
   * Adds the specified number of the string representation of a chromosome.
   * 
   * @param instances the number of instances
   * @param geneString a string representation of a chromosome
   */
  public void addChromosomes(int instances, String geneString) {
    for (int i = 0; i < instances; i++)
      addChromosome(geneString);
  }

  /**
   * Adds a string representation of a chromosome.
   * 
   * @param geneString a string representation of a chromosome
   */
  public void addChromosome(String geneString) {
    chromosomes.add(new Chromosome(geneString));
  }

  /**
   * Builds a new instance of {@link Generation}.
   * 
   * @return a new instance of the Generation.
   */
  public Generation build() {
    Chromosome[] cs = chromosomes.toArray(new Chromosome[chromosomes.size()]);
    return new Generation(cs);
  }

  /**
   * Resets this builder by removing the known chromosomes if any.
   */
  public void reset() {
    chromosomes.clear();
  }
}
