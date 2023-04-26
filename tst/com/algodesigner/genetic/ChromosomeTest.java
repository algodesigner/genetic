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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ChromosomeTest {

  @Test
  public void testConstruction() {

    new Chromosome("ABDBCAA");
    new Chromosome("ABDBCAB");

    try {
      new Chromosome((String)null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
    }
  }

  @Test
  public void testGetGene() {
    Chromosome c = new Chromosome("ABDBCAA");
    assertEquals(new Gene('D'), c.getGene(2));
  }

  @Test
  public void testIsCompatible() {
    Chromosome c = new Chromosome("ABDBCAA");
    assertFalse(c.isCompatible(null));

    Chromosome c2 = new Chromosome("ABDBCAAA");
    assertFalse(c.isCompatible(c2));
    assertFalse(c2.isCompatible(c));

    Chromosome c3 = new Chromosome("ZZZZZZZ");
    assertTrue(c.isCompatible(c3));
    assertTrue(c3.isCompatible(c));

    Chromosome c4 = new Chromosome("ABDBCAA");
    assertTrue(c.isCompatible(c4));
    assertTrue(c4.isCompatible(c));
  }

  @Test
  public void testToString() {
    assertEquals("ABDBCAA", new Chromosome("ABDBCAA").toString());
    assertEquals("XXYYZZ", new Chromosome("XXYYZZ").toString());
  }
}
