package com.aigeneration.genetic.test;

import com.aigeneration.genetic.Chromosome;

import junit.framework.TestCase;

public class ChromosomeTest extends TestCase {
  
  public void testConstruction() {

    new Chromosome("ABDBCAA", 0.5);
    new Chromosome("ABDBCAA", 0);
    
    try {
      new Chromosome((String)null, 0.5);
    } catch (IllegalArgumentException e) {
    }
  }
}
