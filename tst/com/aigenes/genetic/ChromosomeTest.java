package com.aigenes.genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.aigenes.genetic.Chromosome;
import com.aigenes.genetic.Gene;

public class ChromosomeTest {

  @Test
  public void testConstruction() {

    new Chromosome("ABDBCAA");
    new Chromosome("ABDBCAB");
    
    try {
      new Chromosome((String)null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }
  
  @Test
  public void testGetGene() {
    Chromosome c = new Chromosome("ABDBCAA");
    assertEquals(new Gene('D'), c.getGene(2));
  }
  
  @Test
  public void testIsCompatible() {
    Chromosome c = new Chromosome("ABDBCAA");
    assertFalse(c.isCompatible(null));
    
    Chromosome c2 = new Chromosome("ABDBCAAA");
    assertFalse(c.isCompatible(c2));
    assertFalse(c2.isCompatible(c));

    Chromosome c3 = new Chromosome("ZZZZZZZ");
    assertTrue(c.isCompatible(c3));
    assertTrue(c3.isCompatible(c));
    
    Chromosome c4 = new Chromosome("ABDBCAA");
    assertTrue(c.isCompatible(c4));
    assertTrue(c4.isCompatible(c));
  }
  
  
  @Test
  public void testToString() {
    assertEquals("ABDBCAA", new Chromosome("ABDBCAA").toString());
    assertEquals("XXYYZZ", new Chromosome("XXYYZZ").toString());
  }
}
