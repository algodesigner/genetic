package com.aigeneration.genetic;

import static org.junit.Assert.*;
import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.ChromosomePair;


public class ChromosomePairTest {

  @Test
  public void testConstruction() {
    new ChromosomePair(new Chromosome("ABC", 0.2), new Chromosome("DEF", 0.2));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullFirstArg() {
    new ChromosomePair(null, new Chromosome("DEF", 0.2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSecondArg() {
    new ChromosomePair(new Chromosome("ABC", 0.2), null);
  }
  
  @Test
  public void testBehaviour() {
    Chromosome c = new Chromosome("ABC", 0.2);
    Chromosome c2 = new Chromosome("DEF", 0.7);
    ChromosomePair pair = new ChromosomePair(c, c2);
    assertEquals(c, pair.getFirst());
    assertEquals(c2, pair.getSecond());
  }
}
