package com.algodesigner.genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

/**
 * EvolutionEngine intergration test.
 * 
 * @author Vlad Shurupov
 * @version 1.02
 */
@Ignore
public class ExtendedEvolutionEngineTest {

  private static final double CROSSOVER_RATE = 0.8;
  private static final double MUTATION_RATE = 0.25;
  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 60 * 10, 10000);
  private static final long SEED = 314159265;
  
  private static final byte[] PERFECT_SEQ = "/'|.".getBytes();
  private static final Chromosome PERFECT_CHROMOSOME = new Chromosome("/'|./'|./'|./'|./'|./'|./'|./'|./'|./'|./'|./'|.");
  private static final double PERFECT_SCORE;
  
  /*
   * This fitness function favours one dominating gene, be it '0' or '1'. The
   * '.' gene is considered useless.
   */
  private static final IFitnessFunction fitnessFunction = chromosome -> {
	double score = 0;
	byte[] g = chromosome.toString().getBytes();
	for (int i = 0; i < g.length; i++) {
		boolean matched = true;
		int j = i;
		for (; j < g.length && (j - i) < PERFECT_SEQ.length; j++) {
			if (g[j] != PERFECT_SEQ[j - i]) {
				matched = false;
				break;
			}
		}
		if (j - i == PERFECT_SEQ.length && matched)
			score += 1;
	}
	return score;
  };
  
  static {
	  PERFECT_SCORE = fitnessFunction.apply(PERFECT_CHROMOSOME);
	  System.out.println("Perfect score: " + PERFECT_SCORE);
  }

  @Test
  public void testEvolution() {
//    checkEvolution(true);
    checkEvolution(false);
  }

  private static void checkEvolution(boolean elitism) {
    // Seed the pseudorandom number generator to achieve predictable results
    Random random = new Random();
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, elitism, random);    

    System.out.println(engine);

    // This is an optional step to ensure this call does not break anything
//    engine.step();

//    int bestIndex = engine.findSolution(PERFECT_SCORE, TERMINATION_CRITERIA);
//    assertTrue(bestIndex > -1);
    
    System.out
      .println("Problem Solved! Generation: " + engine.getGenerationCount());
    System.out.println("Best score: " + engine.getBestFitnessScore());
    
    assertTrue(engine.getGenerationCount() > 0);
    assertTrue(engine.getBestIndex() < engine.getGeneration().size());

    System.out.println(engine);
    System.out.println("Best index: " + engine.getBestIndex());

    Chromosome bestChromosome =
      engine.getGeneration().getChromosome(engine.getBestIndex());
    System.out.println(bestChromosome);
    assertEquals((double)1, engine.getBestFitnessScore(), 1e-8);

  }

  @Test(expected = IllegalStateException.class)
  public void testNaNScore() {
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      CROSSOVER_RATE, MUTATION_RATE, $ -> Double.NaN);
    engine.findSolution(1, TERMINATION_CRITERIA);
  }

  @Test
  public void testWithoutTerminationCriteria() {
    // Seed the pseudorandom number generator to achieve predictable results
    Random random = new Random(SEED);
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      CROSSOVER_RATE, MUTATION_RATE, fitnessFunction, true, random);    
    engine.findSolution(1, null);
    assertEquals((double)1, engine.getBestFitnessScore(), 1e-8);
  }

  private static Generation createInitialGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
    
	builder.addChromosome("/'|.||||||||||||||||||||||||||||||||||||||||||||");
	builder.addChromosome("////////////////////////////////////////////////");
	builder.addChromosome("''''''''''''''''''''''''''''''''''''''''''''''''");
	builder.addChromosome("................................................");
	builder.addChromosome("////////////////////////////////////////////////");
	builder.addChromosome("||||||||||||||||||||||||||||||||||||||||||||||||");
	builder.addChromosome("''''''''''''''''''''''''''''''''''''/'|.''''''''");
	builder.addChromosome("................................................");
	builder.addChromosome("////////////////////////////////////////////////");
	builder.addChromosome("||||||||||||||||||||||||||||||||||||||||||||||||");
	builder.addChromosome("''''''''''''''''''''''''''''''''''''''''''''''''");
	builder.addChromosome("................................................");
	builder.addChromosome("////////////'|./////////////////////////////////");
	builder.addChromosome("||||||||||||||||||||||||||||||||||||||||||||||||");
	builder.addChromosome("''''''''''''''''''''''''''''''''''''''''''''''''");
	builder.addChromosome("................................................");

    return builder.build();
  }
}
