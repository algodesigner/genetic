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
 * TerminationCriteria class contains criteria for termination of a genetic
 * search. The class is intended to be used by <tt>EvolutionEngine</tt>.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class TerminationCriteria {

  private final long maxTime;
  private final long maxGenerations;

  /**
   * Constructs a TerminationCriteria object.
   * 
   * @param maxTime The maximum time allowed for a genetic search in
   *        milliseconds. Use -1 if time constraint is not to be applied
   * @param maxGenerations The maximum number of generations allowed in a
   *        genetic search. Use -1 if not this limit is not to be applied
   */
  public TerminationCriteria(long maxTime, long maxGenerations) {
    if (maxTime < -1)
      throw new IllegalArgumentException("Invalid maxTime");
    if (maxGenerations < -1)
      throw new IllegalArgumentException("Invalid maxGenerations");
    this.maxTime = maxTime;
    this.maxGenerations = maxGenerations;
  }

  /**
   * Returns the maximum time allowed for a genetic search
   * 
   * @return the maximum time allowed for a genetic search
   */
  public long getMaxTime() {
    return maxTime;
  }

  /**
   * Returns the maximum number of generations allowed in a genetic search
   * 
   * @return the maximum number of generations
   */
  public long getMaxGenerations() {
    return maxGenerations;
  }
}
