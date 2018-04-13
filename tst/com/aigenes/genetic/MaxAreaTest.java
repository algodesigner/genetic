package com.aigenes.genetic;

import org.junit.Test;

import com.aigenes.genetic.Chromosome;
import com.aigenes.genetic.CompositeEvolutionEngine;
import com.aigenes.genetic.EvolutionEngine;
import com.aigenes.genetic.Generation;
import com.aigenes.genetic.GenerationBuilder;
import com.aigenes.genetic.IEvolutionEngine;
import com.aigenes.genetic.IFitnessFunction;
import com.aigenes.genetic.IncompatibleChromosomeException;
import com.aigenes.genetic.TerminationCriteria;
import com.aigenes.genetic.TerminationException;

public class MaxAreaTest {
 
  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 3, 3000);
  private static final boolean ELITISM = true;
  
  private static final int POPULATION_SIZE = 256;
  
  @Test
  public void testMaxAreaSimpleEngine() throws IncompatibleChromosomeException {
    solveMaxArea(new EvolutionEngine(createInitialGeneration(), CROSSOVER_RATE,
      MUTATION_RATE, new FitnessFunction(200), ELITISM));
  }
  
  @Test
  public void testMaxAreaCompositeEngine()
    throws IncompatibleChromosomeException
  {
    solveMaxArea(new CompositeEvolutionEngine(createInitialGeneration(),
      CROSSOVER_RATE, MUTATION_RATE, new FitnessFunction(200), ELITISM, 3));
  }
  
  private static void solveMaxArea(IEvolutionEngine engine)
    throws IncompatibleChromosomeException
  {
    try {
      engine.findSolution(Double.MAX_VALUE, TERMINATION_CRITERIA);
    } catch (TerminationException e) {
      Generation generation = engine.getGeneration();
      System.out.println(generation.getChromosome(engine.getBestIndex()));
    }
  }
  
  private static Generation createInitialGeneration() {
    String s1 = "123456";
    String s2 = "789000";
    GenerationBuilder builder = new GenerationBuilder();
    for (int i = 0; i < POPULATION_SIZE; i++) {
      String s = (i & 1) > 0 ? s1 : s2;
      builder.addChromosome(s);
    }
    return builder.build();
  }

  private static class FitnessFunction implements IFitnessFunction {
    
    private final int perimeter;
    
    public FitnessFunction(int perimeter) {
      if (perimeter < 0)
        throw new IllegalArgumentException("perimeter cannot be negative");
      this.perimeter = perimeter;
    }

    @Override
    public double apply(Chromosome chromosome) {
      
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
      
      return sig(perimeter, x + y) * (double)x * (double)y;
    }
    
    private static double sig(int n, double x) {
      return Math.tanh(x * 2.5 / (double)n);
    }
  }
}
