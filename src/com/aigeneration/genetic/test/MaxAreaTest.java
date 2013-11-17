package com.aigeneration.genetic.test;

import org.junit.Test;
import com.aigeneration.genetic.Chromosome;
import com.aigeneration.genetic.EvolutionEngine;
import com.aigeneration.genetic.Generation;
import com.aigeneration.genetic.IFitnessEvaluator;
import com.aigeneration.genetic.IncompatibleChromosomeException;
import com.aigeneration.genetic.TerminationCriteria;
import com.aigeneration.genetic.TerminationException;

public class MaxAreaTest {
 
  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 3, 3000);
  private static final boolean ELITISM = true;
  
  private static final int POPULATION_SIZE = 256;
  
  @Test
  public void testMaxArea() throws IncompatibleChromosomeException {
    
    EvolutionEngine evolutionEngine =
      new EvolutionEngine(createInitialGeneration(), MUTATION_RATE,
        new FitnessEvaluator(200), ELITISM);    
    
    try {
      evolutionEngine.findSolution(Double.MAX_VALUE, TERMINATION_CRITERIA);
    } catch (TerminationException e) {
      Generation generation = evolutionEngine.getGeneration();
      System.out.println(generation.getChromosome(evolutionEngine.getBestIndex()));
    }
  }
  
  private static Generation createInitialGeneration() {
    String s1 = "123456";
    String s2 = "789000";
    Chromosome[] chromosomes = new Chromosome[POPULATION_SIZE];
    for (int i = 0; i < POPULATION_SIZE; i++) {
      String s = (i & 1) > 0 ? s1 : s2;
      chromosomes[i] = new Chromosome(s, CROSSOVER_RATE);
    }
    return new Generation(chromosomes);
  }

  private static class FitnessEvaluator implements IFitnessEvaluator {
    
    private final int perimeter;
    
    public FitnessEvaluator(int perimeter) {
      if (perimeter < 0)
        throw new IllegalArgumentException("perimeter cannot be negative");
      this.perimeter = perimeter;
    }

    /**
     * @see com.aigeneration.genetic.IFitnessEvaluator#evaluate(com.aigeneration.genetic.Chromosome)
     */
    @Override
    public double evaluate(Chromosome chromosome) {
      
      if (chromosome.length() != 6)
        throw new IllegalArgumentException("Invalid chromosome");
      
      final String cs = chromosome.toString();
      
      if (cs.length() != 6)
        throw new IllegalArgumentException("Invalid chromosome");
           
      // Decode X and Y
      int x = Integer.valueOf(cs.substring(0, 3));
      int y = Integer.valueOf(cs.substring(3, cs.length()));
      
      // Check the perimeter constraint
      if (x + y > perimeter)
        return 0;
      
      return x * y;
    }
  }
}
