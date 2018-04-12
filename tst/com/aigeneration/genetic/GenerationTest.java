package com.aigeneration.genetic;

import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.Generation;

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
