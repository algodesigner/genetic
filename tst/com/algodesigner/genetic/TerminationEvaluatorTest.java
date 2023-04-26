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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * TerminationEvaluator test cases
 * 
 * @author Vlad Shurupov
 * @version 1.01
 */
public class TerminationEvaluatorTest {

  @Test
  public void testConstructor() {
    try {
      new TerminationEvaluator(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
    }
  }

  @Test
  public void testOperation() throws InterruptedException {
    TerminationEvaluator terminationEvaluator =
      new TerminationEvaluator(new TestEvolutionEngine());

    try {
      terminationEvaluator.evaluate(null, 0);
      fail("Expected a Throwable");
    } catch (Throwable t) {
    }

    TerminationCriteria criteria = new TerminationCriteria(-1, -1);
    terminationEvaluator.evaluate(criteria, 0);

    criteria = new TerminationCriteria(100, -1);
    terminationEvaluator.evaluate(criteria, 0);
    Thread.sleep(100);
    assertFalse(terminationEvaluator.evaluate(criteria, 0));

    criteria = new TerminationCriteria(-1, 3);
    assertFalse(terminationEvaluator.evaluate(criteria, 0));
  }

  private class TestEvolutionEngine implements IEvolutionEngine {

    public Generation getGeneration() {
      return null;
    }

    public int findSolution(double fitnessTarget,
      TerminationCriteria terminationCriteria)
    {
      return -1;
    }

    public void step() {
    }

    public int step(double fitnessTarget) {
      return -1;
    }

    public long getGenerationCount() {
      return 5;
    }

    public int getBestIndex() {
      return 0;
    }

    @Override
    public double getBestFitnessScore() {
      return 0;
    }
  }
}
