package com.algodesigner.genetic;

/**
 * This exception is expect to be thrown when a chromosome is not compatible
 * with another chromosome for the purposes of crossover or related operations.
 * 
 * @author Vlad Shurupov
 * @version 1.01
 */
public class IncompatibleChromosomeException extends RuntimeException {

  private static final long serialVersionUID = 0;

  public IncompatibleChromosomeException(String message) {
    super(message);
  }
}
