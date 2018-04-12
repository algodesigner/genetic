package com.aigenes.genetic;

/**
 * A pair of Chromosomes.
 * @author Vlad Shurupov
 * @version 1.01
 */
public class ChromosomePair {

  private Chromosome first;
  private Chromosome second;

  /**
   * Constructs this chromosome pair.
   * @param first the first chromosome (cannot be <code>null</code>).
   * @param second the second chromosome (cannot be <code>null</code>).
   */
  public ChromosomePair(Chromosome first, Chromosome second) {
    if (first == null || second == null)
      throw new IllegalArgumentException("null");
    this.first = first;
    this.second = second;
  }

  /**
   * Returns the first chromosome.
   * @return the first chromosome.
   */
  public Chromosome getFirst() {
    return first;
  }

  /**
   * Returns the second chromosome.
   * @return the second chromosome.
   */
  public Chromosome getSecond() {
    return second;
  }
}
