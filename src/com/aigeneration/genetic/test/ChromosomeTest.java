package com.aigeneration.genetic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.aigeneration.genetic.Chromosome;

public class ChromosomeTest {

  @Test
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
  
  @Test
  public void testIllegalCrossoverRate() {
    try {
      new Chromosome(null, null, -0.1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  @Test
  public void testToString() {
    assertEquals("ABDBCAA", new Chromosome("ABDBCAA", 0.6).toString());
    assertEquals("XXYYZZ", new Chromosome("XXYYZZ", 0.3).toString());
  }
}
