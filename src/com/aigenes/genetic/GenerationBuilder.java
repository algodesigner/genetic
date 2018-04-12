package com.aigenes.genetic;

import java.util.ArrayList;
import java.util.List;

public class GenerationBuilder {

  private List<Chromosome> chromosomes;

  public GenerationBuilder() {
    this.chromosomes = new ArrayList<>();
  }

  public void addChromosomes(int instances, String geneString) {
    for (int i = 0; i < instances; i++)
      addChromosome(geneString);
  }

  public void addChromosome(String geneString) {
    chromosomes.add(new Chromosome(geneString));
  }

  public Generation build() {
    Chromosome[] cs = chromosomes.toArray(new Chromosome[chromosomes.size()]);
    return new Generation(cs);
  }

  public void reset() {
    chromosomes.clear();
  }
}
