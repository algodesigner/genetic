package com.aigeneration.genetic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.DefaultCrossoverStrategy;
import com.aigeneration.genetic.Gene;

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

    try {
      new Chromosome(Chromosome.toGenes("ABDDEF"), null, 0.7);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  @Test
  public void testIllegalCrossoverRate() {
    try {
      new Chromosome(Chromosome.toGenes("ABDDEF"),
        new DefaultCrossoverStrategy(), -0.1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  @Test
  public void testGetGene() {
    Chromosome c = new Chromosome("ABDBCAA", 0.5);
    assertEquals(new Gene('D'), c.getGene(2));
  }
  
  @Test
  public void testCompatible() {
    Chromosome c = new Chromosome("ABDBCAA", 0.5);
    assertFalse(c.compatible(null));
    
    Chromosome c2 = new Chromosome("ABDBCAAA", 0.5);
    assertFalse(c.compatible(c2));
    assertFalse(c2.compatible(c));

    Chromosome c3 = new Chromosome("ZZZZZZZ", 0.5);
    assertTrue(c.compatible(c3));
    assertTrue(c3.compatible(c));
    
    Chromosome c4 = new Chromosome("ABDBCAA", 0.2);
    assertFalse(c.compatible(c4));
    assertFalse(c4.compatible(c));
  }
  
  
  @Test
  public void testToString() {
    assertEquals("ABDBCAA", new Chromosome("ABDBCAA", 0.6).toString());
    assertEquals("XXYYZZ", new Chromosome("XXYYZZ", 0.3).toString());
  }
}
