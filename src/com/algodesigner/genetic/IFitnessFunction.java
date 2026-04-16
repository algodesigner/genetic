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

/**
 * Functional interface that defines how to evaluate the quality (fitness) of a
 * chromosome in a genetic algorithm. The fitness function is the objective
 * function that the algorithm attempts to maximise.
 * <p>
 * Fitness functions translate chromosome representations into numerical scores
 * that indicate solution quality. Higher scores represent better solutions.
 * The function should be designed to guide the evolutionary process toward
 * optimal solutions.
 * <p>
 * <strong>Design considerations:</strong>
 * <ul>
 *   <li><strong>Monotonic:</strong> Better solutions should have higher scores</li>
 *   <li><strong>Continuous:</strong> Small changes should produce small fitness changes</li>
 *   <li><strong>Efficient:</strong> Called frequently during evolution</li>
 *   <li><strong>Deterministic:</strong> Same chromosome should produce same fitness</li>
 * </ul>
 * <p>
 * <strong>Example implementation:</strong>
 * <pre>
 * // Fitness function for maximising function f(x) = x²
 * IFitnessFunction squareFitness = chromosome -> {
 *     Gene gene = chromosome.getGene(0);
 *     double x = ((Number) gene.getValue()).doubleValue();
 *     return x * x;  // Higher x gives higher fitness
 * };
 * 
 * // Fitness function for minimising distance
 * IFitnessFunction distanceFitness = chromosome -> {
 *     Point p1 = (Point) chromosome.getGene(0).getValue();
 *     Point p2 = (Point) chromosome.getGene(1).getValue();
 *     double distance = p1.distanceTo(p2);
 *     return 1.0 / (1.0 + distance);  // Smaller distance gives higher fitness
 * };
 * </pre>
 * 
 * @author Vlad Shurupov
 * @version 1.0
 * @see Chromosome
 * @see EvolutionEngine
 * @see Generation
 */
@FunctionalInterface
public interface IFitnessFunction {

  /**
   * Evaluates the specified chromosome and returns its fitness score.
   * The score represents how well the chromosome solves the target problem,
   * with higher values indicating better solutions.
   * <p>
   * This method is called for every chromosome in every generation during
   * evolution, so implementations should be efficient. The function must
   * return valid double values (not NaN or infinite) for all valid chromosomes.
   * 
   * @param chromosome the chromosome to evaluate, never {@code null}
   * @return the fitness score of the chromosome (higher is better)
   * @throws NullPointerException if {@code chromosome} is {@code null}
   * @throws ClassCastException if chromosome genes cannot be interpreted
   *         as expected by the fitness function
   */
  double apply(Chromosome chromosome);
}
