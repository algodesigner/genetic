package com.aigeneration.genetic;

/**
 * Chromosome implementation
 * @author Vlad Shurupov
 * @version 1.01
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
    this(genes, new DefaultCrossoverStrategy(), crossoverRate);
  }

  public Chromosome(String geneString, double crossoverRate) {
    this(toGenes(geneString), crossoverRate);
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
    if (genes == null || crossoverStrategy == null)
      throw new IllegalArgumentException("null");
    if (crossoverRate < 0)
      throw new IllegalArgumentException("crossover rate cannot be less" +
        " than zero");

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

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < genes.length; i++)
      stringBuffer.append(genes[i].toString());
    return stringBuffer.toString();    
  }
  
  /**
   * Converts a string into a gene sequence.
   * @param geneString a string with each character represeting a gene.
   * @return a gene sequence.
   */
  private static Gene[] toGenes(String geneString) {
    if (geneString == null)
      throw new IllegalArgumentException("null");
    Gene[] genes = new Gene[geneString.length()];
    for (int i = 0; i < genes.length; i++)
      genes[i] = new Gene(geneString.charAt(i));
    return genes;
  }
}
