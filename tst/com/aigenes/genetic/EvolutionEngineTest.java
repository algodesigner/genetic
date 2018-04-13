package com.aigenes.genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

/**
 * EvolutionEngine intergration test.
 * 
 * @author Vlad Shurupov
 * @version 1.02
 */
public class EvolutionEngineTest {

  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 60 * 3, 10000);
  private static final long SEED = 314159265;
  
  /*
   * This fitness function favours one dominating gene, be it '0' or '1'. The
   * '.' gene is considered useless.
   */
  private static final IFitnessFunction fitnessFunction = chromosome -> {
    Gene[] genes = chromosome.getGenes();
    int zeroCount = 0;
    int oneCount = 0;
    for (int i = 0; i < genes.length; i++) {
      char c = ((Character)genes[i].getValue()).charValue();
      if (c == '0')
        zeroCount++;
      else if (c == '1')
        oneCount++;
    }
    return (double)Math.max(zeroCount, oneCount) / (double)genes.length;
  };

  @Test
  public void testEvolution()
    throws IncompatibleChromosomeException, TerminationException
  {
    checkEvolution(true);
    checkEvolution(false);
  }

  private static void checkEvolution(boolean elitism)
    throws IncompatibleChromosomeException, TerminationException
  {
    // Seed the pseudorandom number generator to achieve predictable results
    Random random = new Random(SEED);
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      new DefaultSelector(random),
      new DefaultCrossoverStrategy(CROSSOVER_RATE, random),
      new DefaultMutationStrategy(MUTATION_RATE, random), fitnessFunction,
      elitism);

    System.out.println(engine.getGeneration());

    // This is an optional step to ensure this call does not break anything
    engine.step();

    System.out.println(engine.findSolution(1, TERMINATION_CRITERIA));
    System.out
      .println("Problem Solved! Generation: " + engine.getGenerationCount());

    assertTrue(engine.getGenerationCount() > 0);
    assertTrue(engine.getBestIndex() < engine.getGeneration().size());

    System.out.println(engine.getGeneration());
    System.out.println("Best index: " + engine.getBestIndex());

    Chromosome bestChromosome =
      engine.getGeneration().getChromosome(engine.getBestIndex());
    System.out.println(bestChromosome);
    assertEquals((double)1, engine.getBestFitnessScore(), 1e-8);

  }

  @Test(expected = IllegalStateException.class)
  public void testNaNScore()
    throws IncompatibleChromosomeException, TerminationException
  {
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      CROSSOVER_RATE, MUTATION_RATE, $ -> Double.NaN);
    engine.findSolution(1, TERMINATION_CRITERIA);
  }

  @Test
  public void testWithoutTerminationCriteria()
    throws IncompatibleChromosomeException, TerminationException
  {
    // Seed the pseudorandom number generator to achieve predictable results
    Random random = new Random(SEED);
    EvolutionEngine engine = new EvolutionEngine(createInitialGeneration(),
      new DefaultSelector(random),
      new DefaultCrossoverStrategy(CROSSOVER_RATE, random),
      new DefaultMutationStrategy(MUTATION_RATE, random), fitnessFunction,
      true);
    engine.findSolution(1, null);
    assertEquals((double)1, engine.getBestFitnessScore(), 1e-8);
  }

  private static Generation createInitialGeneration() {
    GenerationBuilder builder = new GenerationBuilder();

    builder.addChromosome("010101010101010101010101010101010101010101010101");
    builder.addChromosome("10101010101010101010101.....01010101010101010101");
    builder.addChromosome("01010101010110101.....................0101010101");
    builder.addChromosome("10101010101010101010..........101010101010101010");
    builder.addChromosome("01010010...010101010101....011010101101010101010");
    builder.addChromosome("101010010101010101010101010101101010101010101010");
    builder.addChromosome("0101010101010101...10101010101010101101010101010");
    builder.addChromosome("1010101010101.........01010101010101010101101010");
    builder.addChromosome("01010101.................0101010..........101010");
    builder.addChromosome("10101010101............010101010..........101010");
    builder.addChromosome("01010101010101010101010101010101...........01010");
    builder.addChromosome("101010101010101010010101010101010101010101101010");
    builder.addChromosome("010101010101101....01010101010101010101010101010");
    builder.addChromosome("101010101010101010101010101010101010101010101010");

    return builder.build();
  }
}
