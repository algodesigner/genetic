package com.aigeneration.genetic.test;

import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.EvolutionEngine;
import com.aigeneration.genetic.Gene;
import com.aigeneration.genetic.Generation;
import com.aigeneration.genetic.GenerationBuilder;
import com.aigeneration.genetic.IFitnessEvaluator;
import com.aigeneration.genetic.IncompatibleChromosomeException;
import com.aigeneration.genetic.TerminationCriteria;
import com.aigeneration.genetic.TerminationException;

/**
 * EvolutionEngine intergration test.
 * @author Vlad Shurupov
 * @version 1.02
 */
public class EvolutionTest {

  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 60 * 3, 10000);
  private static final boolean ELITISM = true;
  
  private FitnessEvaluator fitnessEvaluator = new FitnessEvaluator();

  @Test
  public void testEvolution()
    throws IncompatibleChromosomeException, TerminationException
  {
    EvolutionEngine evolutionEngine =
      new EvolutionEngine(createInitialGeneration(), MUTATION_RATE,
        fitnessEvaluator, ELITISM);    

    System.out.println(evolutionEngine.getGeneration());
    System.out.println(evolutionEngine.findSolution(1, TERMINATION_CRITERIA));
    System.out.println("Problem Solved! Generation: " +
      evolutionEngine.getGenerationCount());      
    System.out.println(evolutionEngine.getGeneration());
    System.out.println("Best index: " + evolutionEngine.getBestIndex());
    
    System.out.println(evolutionEngine.getGeneration().getChromosome(evolutionEngine.getBestIndex()));
  }

  private static Generation createInitialGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
    builder.setCrossoverRate(CROSSOVER_RATE);
    
    builder.addChromosome("010101010101010101010101010101010101010101010101");
    builder.addChromosome("10101010101010101010101.....01010101010101010101");
    builder.addChromosome("01010101010110101.....................0101010101");
    builder.addChromosome("10101010101010101010..........101010101010101010");
    builder.addChromosome("01010010101010101010101....011010101101010101010");
    builder.addChromosome("101010010101010101010101010101101010101010101010");
    builder.addChromosome("0101010101010101...10101010101010101101010101010");
    builder.addChromosome("1010101010101.........01010101010101010101101010");
    builder.addChromosome("01010101.................0101010..........101010");
    builder.addChromosome("10101010101............010101010..........101010");
    builder.addChromosome("01010101010101010101010101010101...........01010");
    builder.addChromosome("101010101010101010010101010101010101010101101010");
    builder.addChromosome("010101010101101010101010101010101010101010101010");
    builder.addChromosome("101010101010101010101010101010101010101010101010");
    
    return builder.build();
  }

  private static class FitnessEvaluator implements IFitnessEvaluator {

    /**
     * @see com.aiage.gene.IFitnessEvaluator#evaluate(com.aiage.gene.Chromosome)
     */
    public double evaluate(Chromosome chromosome) {
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
    }
  }
}
