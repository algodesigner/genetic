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

import org.junit.Test;

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
    engine.findSolution(Double.MAX_VALUE, TERMINATION_CRITERIA);
    Generation generation = engine.getGeneration();
    System.out.println(generation.getChromosome(engine.getBestIndex()));
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
