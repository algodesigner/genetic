package com.aigeneration.genetic;

/**
 * @author Vlad Shurupov
 * @version 1.0
 */
public class ChromosomePair {

  private Chromosome first;
  private Chromosome second;

  ChromosomePair(Chromosome first, Chromosome second) {
    this.first = first;
    this.second = second;
  }

  public Chromosome getFirst() {
    return first;
  }

  public Chromosome getSecond() {
    return second;
  }
}
