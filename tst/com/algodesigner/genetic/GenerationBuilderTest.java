package com.algodesigner.genetic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GenerationBuilderTest {

  @Test
  public void testBehaviour() {
    GenerationBuilder builder = new GenerationBuilder();
    builder.addChromosome("ABCD");
    builder.addChromosome("DCBA");
    Generation generation = builder.build();
    assertEquals(2, generation.size());
  }

  @Test
  public void testReset() {
    GenerationBuilder builder = new GenerationBuilder();
    builder.addChromosome("ABCD");
    builder.addChromosome("DCBA");
    builder.reset();
    Generation generation = builder.build();
    assertEquals(0, generation.size());
    builder.addChromosome("ABCD");
  }
}
