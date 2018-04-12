package com.aigenes.genetic;

import org.junit.Test;

import com.aigenes.genetic.Chromosome;
import com.aigenes.genetic.Generation;

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

}
