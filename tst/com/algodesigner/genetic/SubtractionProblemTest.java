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

public class SubtractionProblemTest {

  private static final double CROSSOVER_RATE = 0.7;
  private static final double MUTATION_RATE = 0.5;
  private static final boolean ELITISM = true;
  private static final int POPULATION_SIZE = 256;

  private static final TerminationCriteria TERMINATION_CRITERIA =
    new TerminationCriteria(1000 * 3, 3000);

  @Test
  public void testEvolution() throws IncompatibleChromosomeException {
    trySolveProblem(createEvolutionEngine());
  }

  private static void trySolveProblem(IEvolutionEngine engine) {
    int index = engine.findSolution(1, TERMINATION_CRITERIA);
    Chromosome c = engine.getGeneration().getChromosome(index);
    System.out.println("Raw chromosome: " + c);
    System.out.println("Generations: " + engine.getGenerationCount());
    System.out.println("Solution:\n" + toSolutionString(c));
  }

  private static IEvolutionEngine createEvolutionEngine() {
    return new EvolutionEngine(createInitGeneration(), CROSSOVER_RATE,
      MUTATION_RATE, new FitnessFunction(), ELITISM);
  }

  private static Generation createInitGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
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

  private static class FitnessFunction implements IFitnessFunction {

    private static final int CHROMOSOME_LENGTH = 9;

    @Override
    public double apply(Chromosome chromosome) {

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
