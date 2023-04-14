package com.aigenes.genetic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class ChromosomePairTest {

  @Test
  public void testConstruction() {
    new ChromosomePair(new Chromosome("ABC"), new Chromosome("DEF"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullFirstArg() {
    new ChromosomePair(null, new Chromosome("DEF"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSecondArg() {
    new ChromosomePair(new Chromosome("ABC"), null);
  }
  
  @Test
  public void testBehaviour() {
    Chromosome c = new Chromosome("ABC");
    Chromosome c2 = new Chromosome("DEF");
    ChromosomePair pair = new ChromosomePair(c, c2);
    assertEquals(c, pair.getFirst());
    assertEquals(c2, pair.getSecond());
  }
}
