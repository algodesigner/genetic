package com.aigeneration.genetic;

/**
 * @author Vlad Shurupov
 * @version 1.0
 */
public class IncompatibleChromosomeException extends Exception {

  private static final long serialVersionUID = 0;

  public IncompatibleChromosomeException(String message) {
    super(message); 
  }
}
