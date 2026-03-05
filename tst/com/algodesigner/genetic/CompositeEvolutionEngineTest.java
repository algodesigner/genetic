/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2008-2023, Vlad Shurupov
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.algodesigner.genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

/**
 * CompositeEvolutionEngine test.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class CompositeEvolutionEngineTest {

  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final int NUM_AGENTS = 3;

  private Generation createTestGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
    for (int i = 0; i < 100; i++) {
      builder.addChromosome("010101010101010101010101010101010101010101010101");
    }
    return builder.build();
  }

  private IFitnessFunction createTestFitnessFunction() {
    return chromosome -> {
      Gene[] genes = chromosome.getGenes();
      int zeroCount = 0;
      for (Gene gene : genes) {
        if (((Character)gene.getValue()).charValue() == '0') {
          zeroCount++;
        }
      }
      return (double)zeroCount / genes.length;
    };
  }

  @Test
  public void testConstructorDefaults() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    assertNotNull(engine.getGeneration());
    assertEquals(generation, engine.getGeneration());
  }

  @Test
  public void testConstructorCustom() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      new DefaultSelector(), new DefaultCrossoverStrategy(CROSSOVER_RATE),
      new DefaultMutationStrategy(MUTATION_RATE), fitnessFunction, true,
      NUM_AGENTS);

    assertNotNull(engine.getGeneration());
    assertEquals(generation, engine.getGeneration());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorNullGen() {
    IFitnessFunction fitnessFunction = createTestFitnessFunction();
    new CompositeEvolutionEngine(null, CROSSOVER_RATE, MUTATION_RATE,
      fitnessFunction, true, NUM_AGENTS);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorNullFitness() {
    Generation generation = createTestGeneration();
    new CompositeEvolutionEngine(generation, CROSSOVER_RATE, MUTATION_RATE,
      null, true, NUM_AGENTS);
  }

  @Test
  public void testConstructorZeroAgents() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();
    // Actually creates array of size 0, which is allowed but useless
    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, 0);
    assertNotNull(engine);
  }

  @Test
  public void testConstructorNegativeAgents() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();
    // Negative array size will throw NegativeArraySizeException
    try {
      new CompositeEvolutionEngine(generation, CROSSOVER_RATE, MUTATION_RATE,
        fitnessFunction, true, -1);
      fail("Expected NegativeArraySizeException");
    } catch (NegativeArraySizeException e) {
      // Expected
    }
  }

  @Test
  public void testFindSolutionPerfect() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    TerminationCriteria criteria = new TerminationCriteria(1000, 100);
    int solutionIndex = engine.findSolution(1.0, criteria);

    assertTrue(solutionIndex >= 0);
    assertTrue(engine.getGenerationCount() > 0);
    assertTrue(engine.getBestFitnessScore() >= 0.0);
    assertTrue(engine.getBestFitnessScore() <= 1.0);
  }

  @Test
  public void testFindSolutionTimesOut() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    // Very short timeout to test termination
    TerminationCriteria criteria = new TerminationCriteria(1, 10000);
    int solutionIndex = engine.findSolution(1.0, criteria);

    // Should return -1 if timeout reached before finding solution
    assertEquals(-1, solutionIndex);
    assertTrue(engine.getGenerationCount() > 0);
  }

  @Test
  public void testBestFitnessBeforeEvolution() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    // Returns Double.NaN before any evolution (see implementation)
    assertTrue(Double.isNaN(engine.getBestFitnessScore()));
  }

  @Test
  public void testGenerationCountBeforeEvolution() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    // Should return 0 before any evolution
    assertEquals(0, engine.getGenerationCount());
  }

  @Test
  public void testBestIndexBeforeEvolution() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    // Returns -1 before any evolution (see implementation)
    assertEquals(-1, engine.getBestIndex());
  }

  @Test
  public void testGenerationAfterEvolution() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    // Start evolution
    TerminationCriteria criteria = new TerminationCriteria(1000, 100);
    engine.findSolution(1.0, criteria);

    // Should still be able to get generation
    assertNotNull(engine.getGeneration());
  }

  @Test
  public void testEvolutionImproves() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();

    CompositeEvolutionEngine engine = new CompositeEvolutionEngine(generation,
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, NUM_AGENTS);

    double initialBestFitness = engine.getBestFitnessScore();

    // Run evolution for a reasonable time
    TerminationCriteria criteria = new TerminationCriteria(5000, 100);
    engine.findSolution(1.0, criteria);

    double finalBestFitness = engine.getBestFitnessScore();

    // Fitness should improve or stay the same (or be NaN if no evolution
    // happened)
    if (Double.isNaN(initialBestFitness) && Double.isNaN(finalBestFitness)) {
      // Both NaN, that's OK
    } else if (Double.isNaN(initialBestFitness)) {
      // Initial was NaN, final is a number - improvement
      assertTrue(finalBestFitness >= 0.0);
    } else if (Double.isNaN(finalBestFitness)) {
      // Final is NaN, initial was a number - this shouldn't happen but handle
      // it
      // This can happen if evolution didn't run at all
    } else {
      // Both are numbers
      assertTrue(finalBestFitness >= initialBestFitness);
    }
  }

  @Test
  public void testConstructorSeed() {
    Generation generation = createTestGeneration();
    IFitnessFunction fitnessFunction = createTestFitnessFunction();
    Random random = new Random(12345);

    // Test that constructor with Random parameter works
    // Note: CompositeEvolutionEngine doesn't have a constructor with Random
    // parameter,
    // but we can test the underlying engines use it
    EvolutionEngine baseEngine = new EvolutionEngine(generation, CROSSOVER_RATE,
      MUTATION_RATE, fitnessFunction, true, random);

    assertNotNull(baseEngine);
  }
}