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
 * Termination evaluator helper class.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
class TerminationEvaluator {

  private IEvolutionEngine evolutionEngine;
  private long startTime = -1;

  /**
   * Constructs this termination evaluator.
   * 
   * @param evolutionEngine an instance of the evolution engine.
   */
  public TerminationEvaluator(IEvolutionEngine evolutionEngine) {
    if (evolutionEngine == null)
      throw new IllegalArgumentException("null");
    this.evolutionEngine = evolutionEngine;
  }

  /**
   * Evaluates the provided criteria and returns {@code true} if it has not been
   * met.
   * 
   * @param criteria the termination criteria; cannot be {@code null}.
   * @param bestIndex
   * @return {@code true} if and only the termination criteria has not been met,
   *         otherwise {@code false}.
   */
  public boolean evaluate(TerminationCriteria criteria, int bestIndex) {
    // If the timer is not initialised, set it
    if (startTime == -1)
      startTime = System.currentTimeMillis();
    else {
      // We only check the maximum time criteria if the timer was initialised
      // and the time criteria is actually set (i.e. not equal -1)
      if (criteria.getMaxTime() != -1
        && criteria.getMaxTime() <= System.currentTimeMillis() - startTime)
      {
        reset();
        // Time is over
        return false;
      }
    }
    // In any event, check the max generations criteria as long as it is
    // set (i.e. not equal -1)
    if (criteria.getMaxGenerations() != -1
      && evolutionEngine.getGenerationCount() >= criteria.getMaxGenerations())
    {
      reset();
      // Max generations is reached
      return false;
    }
    return true;
  }

  /**
   * Resets the object state for further use.
   */
  private void reset() {
    startTime = -1;
  }
}
