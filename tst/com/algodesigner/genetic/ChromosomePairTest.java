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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChromosomePairTest {

  @Test
  public void testConstruction() {
    new ChromosomePair(new Chromosome("ABC"), new Chromosome("DEF"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFirstArg() {
    new ChromosomePair(null, new Chromosome("DEF"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullSecondArg() {
    new ChromosomePair(new Chromosome("ABC"), null);
  }

  @Test
  public void testBehaviour() {
    Chromosome c = new Chromosome("ABC");
    Chromosome c2 = new Chromosome("DEF");
    ChromosomePair pair = new ChromosomePair(c, c2);
    assertEquals(c, pair.getFirst());
    assertEquals(c2, pair.getSecond());
  }
}
