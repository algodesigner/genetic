package com.aigeneration.genetic;

/**
 * Chromosome implementation
 * @author Vlad Shurupov
 * @version 1.0
 */
public class Chromosome {

  private final Gene[] genes;
  private final ICrossoverStrategy crossoverStrategy;
  private final double crossoverRate;

  /**
   * Constructs a Chromosome object based on an array of Genes. Default
   * crossover strategy is applied.
   * @param genes the array of genes
   */
  public Chromosome(Gene[] genes, double crossoverRate) {
    this(genes, DefaultCrossoverStrategy.getInstance(), crossoverRate);
  }

  public Chromosome(String geneString, double crossoverRate) {
    char[] geneValues = geneString.toCharArray();
    genes = new Gene[geneValues.length];
    for (int i = 0; i < geneValues.length; i++)
      genes[i] = new Gene(geneValues[i]);
    this.crossoverStrategy = DefaultCrossoverStrategy.getInstance();
    this.crossoverRate = crossoverRate;
  }

  /**
   * Constructs a Chromosome object based on an array of Genea and a custom
   * crossover strategy
   * @param genes the array of genes
   * @param crossoverStrategy a custom crossover strategy
   */
  public Chromosome(Gene[] genes, ICrossoverStrategy crossoverStrategy,
    double crossoverRate)
  {
    this.genes = genes;
    this.crossoverStrategy = crossoverStrategy;
    this.crossoverRate = crossoverRate;
  }

  /**
   * Returns the length of the chromosome.
   * @return the length of the chromosome
   */
  public int length() {
    return genes.length;
  }

  /**
   * Returns the gene at a specific index.
   * @param index the index of the gene
   * @return the gene
   */
  public Gene getGene(int index) {
    return genes[index];
  }

  /**
   * Returns the genes contained in this chromosome.
   * @return an array of genes
   */
  public Gene[] getGenes() {
    Gene[] geneCopies = new Gene[genes.length];
    System.arraycopy(genes, 0, geneCopies, 0, genes.length);
    return geneCopies;
  }

  /**
   * Performs a crossover of this chromosome with a given chromosome without a
   * mutation.
   * @param chromosome the chromosome to cross over with
   * @return the new offspring
   */
  public ChromosomePair crossover(Chromosome chromosome)
    throws IncompatibleChromosomeException
  {
    return crossoverStrategy.crossover(this, chromosome);
  }

  /**
   * Returns the crossover rate for this chromosome.
   * @return double the crossover rate
   */
  public double getCrossoverRate() {
    return crossoverRate;
  }
  
  /**
   * Tests if this chromosome is compatible with a given chromosome.
   * @param chromosome the chromosome to test compatibility against
   * @return true, if compatible
   */
  public boolean compatible(Chromosome chromosome) {
    if (chromosome == null)
      return false;
    return length() == chromosome.length() && 
      getCrossoverRate() == chromosome.getCrossoverRate();      
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < genes.length; i++)
      stringBuffer.append(genes[i].toString());
    return stringBuffer.toString();    
  }
}
