package com.algodesigner.genetic;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GenerationTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullChromosomes() {
    new Generation(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUnevenGenerationSize() {
    Chromosome c = new Chromosome("abc");
    new Generation(new Chromosome[] { c });
  }

  @Test
  public void testConstruction() {
    Chromosome c = new Chromosome("abc");
    new Generation(new Chromosome[] { c, c });
  }

  @Test
  public void testToString() {
    Chromosome c = new Chromosome("abc");
    Generation generation = new Generation(new Chromosome[] { c, c });
    assertTrue(generation.toString().contains("abc"));
  }
}
