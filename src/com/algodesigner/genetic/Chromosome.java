package com.algodesigner.genetic;

/**
 * Chromosome consists of genes and is immutable.
 * <p>
 * It can be crossed over with another chromosome producing a
 * {@link ChromosomePair}. The crossover operation is delegated to
 * {@link ICrossoverStrategy}.
 * 
 * <p>
 * The chromosome provides a method to test its compatibility with another
 * chromosome ({@link #isCompatible(Chromosome)}).
 * 
 * @author Vlad Shurupov
 * @version 1.02
 */
public class Chromosome {

  private final Gene[] genes;

  public Chromosome(String geneString) {
    this(toGenes(geneString));
  }

  /**
   * Constructs a Chromosome object based on an array of Genea and a custom
   * crossover strategy
   * @param genes the array of genes
   */
  public Chromosome(Gene[] genes)
  {
    if (genes == null)
      throw new IllegalArgumentException("null");
    this.genes = genes;
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
   * Tests if this chromosome is compatible with a given chromosome.
   * @param chromosome the chromosome to test compatibility against
   * @return true, if compatible
   */
  public boolean isCompatible(Chromosome chromosome) {
    return chromosome != null && length() == chromosome.length();      
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder(32);
    for (int i = 0; i < genes.length; i++)
      builder.append(genes[i].toString());
    return builder.toString();    
  }
  
  /**
   * Converts a string into a gene sequence.
   * @param geneString a string with each character representing a gene.
   * @return a gene sequence.
   */
  public static Gene[] toGenes(String geneString) {
    if (geneString == null)
      throw new IllegalArgumentException("null");
    Gene[] genes = new Gene[geneString.length()];
    for (int i = 0; i < genes.length; i++)
      genes[i] = new Gene(geneString.charAt(i));
    return genes;
  }
}
