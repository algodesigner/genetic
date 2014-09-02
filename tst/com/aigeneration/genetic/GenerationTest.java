package com.aigeneration.genetic;

import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.Generation;

public class GenerationTest {

  private static final double CROSSOVER_RATE = 0.7;

  @Test(expected = IllegalArgumentException.class)
  public void testNullChromosomes() {
    new Generation(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testUnevenGenerationSize() {
    Chromosome c = new Chromosome("abc", CROSSOVER_RATE);
    new Generation(new Chromosome[] { c });
  }
  
  @Test
  public void testConstruction() {
    Chromosome c = new Chromosome("abc", CROSSOVER_RATE);
    new Generation(new Chromosome[] { c, c });
  }

}
