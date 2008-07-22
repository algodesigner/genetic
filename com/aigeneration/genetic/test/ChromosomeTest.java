package com.aigeneration.genetic.test;

import com.aigeneration.genetic.Chromosome;

import junit.framework.TestCase;

public class ChromosomeTest extends TestCase {
  
  public void testConstruction() {

    new Chromosome("ABDBCAA", 0.5);
    new Chromosome("ABDBCAA", 0);
    
    try {
      new Chromosome((String)null, 0.5);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }

    try {
      new Chromosome(null, null, 0.7);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  public void testIllegalCrossoverRate() {
    try {
      new Chromosome(null, null, -0.1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  public void testToString() {
    assertEquals("ABDBCAA", new Chromosome("ABDBCAA", 0.6).toString());
    assertEquals("XXYYZZ", new Chromosome("XXYYZZ", 0.3).toString());
  }
  
}
