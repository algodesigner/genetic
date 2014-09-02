package com.aigeneration.genetic;

import java.util.ArrayList;
import java.util.List;

public class GenerationBuilder {
  
  private double crossoverRate;
  boolean crossoverRateSet;
  private List<Chromosome> chromosomes;
  
  public GenerationBuilder() {
    this.chromosomes = new ArrayList<>();
  }

  public void setCrossoverRate(double crossoverRate) {
    this.crossoverRate = crossoverRate;
    crossoverRateSet = true;
  }
  
  public void addChromosome(String geneString) {
    if (!crossoverRateSet)
      throw new IllegalStateException("crossoverRate is not set");
    chromosomes.add(new Chromosome(geneString, crossoverRate));
  }
  
  public Generation build() {
    Chromosome[] cs = chromosomes.toArray(new Chromosome[chromosomes.size()]);
    return new Generation(cs);
  }
  
  public void reset() {
    crossoverRateSet = false;
    chromosomes.clear();
  }
}
