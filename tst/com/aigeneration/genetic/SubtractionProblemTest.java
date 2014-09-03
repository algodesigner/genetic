package com.aigeneration.genetic;

import org.junit.Test;

public class SubtractionProblemTest {

  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final boolean ELITISM = true;
  private static final int POPULATION_SIZE = 256;

  private static final TerminationCriteria TERMINATION_CRITERIA = new TerminationCriteria(
    1000 * 3, 3000);

  @Test
  public void testEvolution() throws IncompatibleChromosomeException {
    trySolveProblem(createEvolutionEngine());
  }

  private static void trySolveProblem(IEvolutionEngine engine)
    throws IncompatibleChromosomeException
  {
    try {
      int index = engine.findSolution(1, TERMINATION_CRITERIA);
      Chromosome c = engine.getGeneration().getChromosome(index);
      System.out.println("Raw chromosome: " + c);
      System.out.println("Solution:\n" + toSolutionString(c));
    } catch (TerminationException e) {
      e.printStackTrace();
    }
  }

  private static IEvolutionEngine createEvolutionEngine() {
    return new EvolutionEngine(createInitGeneration(), MUTATION_RATE,
      new FitnessEvaluator(), ELITISM);
  }

  private static Generation createInitGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
    builder.setCrossoverRate(CROSSOVER_RATE);
    builder.addChromosomes(POPULATION_SIZE, "123456789");
    return builder.build();
  }
  
  private static String toSolutionString(Chromosome chromosome) {
    final String cs = chromosome.toString();
    char a = cs.charAt(0);
    char b = cs.charAt(1);
    char c = cs.charAt(2);
    int va = a - '0';
    int vb = b - '0';
    int vc = c - '0';
    int result = (vc * 100 + vb * 10 + va) - (va * 100 + vb * 10 + vc);
    
    StringBuilder sb = new StringBuilder();
    sb.append("  ").append(c).append(b).append(a).append('\n');
    sb.append("- ").append(a).append(b).append(c).append('\n');
    sb.append("  ---\n");
    sb.append("  ").append(result);
    return sb.toString();
  }

  private static class FitnessEvaluator implements IFitnessEvaluator {

    private static final int CHROMOSOME_LENGTH = 9;

    @Override
    public double evaluate(Chromosome chromosome) {

      if (chromosome.length() != CHROMOSOME_LENGTH)
        throw new IllegalArgumentException("Invalid chromosome legnth "
          + chromosome.length() + ", expected " + CHROMOSOME_LENGTH);

      final String cs = chromosome.toString();

      if (cs.length() != CHROMOSOME_LENGTH)
        throw new IllegalArgumentException("Broken chromosome");

      // Decode elements
      int a = cs.charAt(0) - '0';
      int b = cs.charAt(1) - '0';
      int c = cs.charAt(2) - '0';

      int op = (c * 100 + b * 10 + a) - (a * 100 + b * 10 + c);
      String ops = String.valueOf(op);

      double c1 = ops.indexOf(cs.charAt(0)) != -1 ? 1 : 0;
      double c2 = ops.indexOf(cs.charAt(1)) != -1 ? 1 : 0;
      double c3 = ops.indexOf(cs.charAt(2)) != -1 ? 1 : 0;
      double c4 = (a != b && b != c) ? 1 : 0;
      double c5 = op > 0 ? 1 : 0;

      return (c1 + c2 + c3 + c4 + c5) / 5;
    }
  }
}
