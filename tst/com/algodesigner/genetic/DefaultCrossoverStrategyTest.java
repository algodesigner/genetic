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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

/**
 * DefaultCrossoverStrategy test.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class DefaultCrossoverStrategyTest {

  @Test
  public void testConstructorDefaultRandom() {
    DefaultCrossoverStrategy strategy = new DefaultCrossoverStrategy(0.7);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorCustomRandom() {
    Random random = new Random(12345);
    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);
    assertNotNull(strategy);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeCrossoverRate() {
    new DefaultCrossoverStrategy(-0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeCrossoverRateAndRandom() {
    new DefaultCrossoverStrategy(-0.1, new Random());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorNullRandom() {
    new DefaultCrossoverStrategy(0.7, null);
  }

  @Test
  public void testConstructorZeroCrossoverRate() {
    DefaultCrossoverStrategy strategy = new DefaultCrossoverStrategy(0.0);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorOneCrossoverRate() {
    DefaultCrossoverStrategy strategy = new DefaultCrossoverStrategy(1.0);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorHighCrossoverRate() {
    DefaultCrossoverStrategy strategy = new DefaultCrossoverStrategy(2.5);
    assertNotNull(strategy);
  }

  @Test
  public void testCrossoverZeroRateNoChange() {
    Random random = new Random(12345);
    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.0, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // With crossover rate 0, should always return original chromosomes
    assertSame(chromosome1, result.getFirst());
    assertSame(chromosome2, result.getSecond());
  }

  @Test
  public void testCrossoverRateOneCompatible() {
    // Use a predictable random that will always choose crossover
    Random random = new Random(12345) {
      private static final long serialVersionUID = -251541658454447408L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 1.0, so crossover happens
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(1.0, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // With crossover rate 1.0 and random < 1.0, should perform crossover
    assertNotSame(chromosome1, result.getFirst());
    assertNotSame(chromosome2, result.getSecond());

    // Verify chromosomes were swapped at some position
    String result1 = result.getFirst().toString();
    String result2 = result.getSecond().toString();

    // They should be different from originals due to crossover
    assertNotEquals("0101010101", result1);
    assertNotEquals("1010101010", result2);

    // But combined genes should be preserved
    assertEquals(10, result1.length());
    assertEquals(10, result2.length());
  }

  @Test(expected = IncompatibleChromosomeException.class)
  public void testCrossoverIncompatibleChromosomes() {
    Random random = new Random(12345) {
      private static final long serialVersionUID = 3314866715431349540L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 1.0, so crossover would happen
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(1.0, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("101010101010"); // Different length

    strategy.crossover(chromosome1, chromosome2);
  }

  @Test
  public void testCrossoverRateOneNoCrossover() {
    // Use a predictable random that will never choose crossover
    Random random = new Random(12345) {
      private static final long serialVersionUID = 1585397382586653127L;

      @Override
      public double nextDouble() {
        return 1.0; // Always 1.0, which is not less than 1.0
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(1.0, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // With random >= 1.0, should return original chromosomes
    assertSame(chromosome1, result.getFirst());
    assertSame(chromosome2, result.getSecond());
  }

  @Test
  public void testCrossoverPreservesLength() {
    Random random = new Random(12345);
    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    assertEquals(10, result.getFirst().length());
    assertEquals(10, result.getSecond().length());
  }

  @Test
  public void testCrossoverSameChromosomes() {
    Random random = new Random(12345);
    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("0101010101"); // Same as
                                                           // chromosome1

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // Should work fine with identical chromosomes
    assertNotNull(result);
    assertEquals(10, result.getFirst().length());
    assertEquals(10, result.getSecond().length());
  }

  @Test
  public void testCrossoverDeterministicSameSeed() {
    Random random1 = new Random(12345);
    Random random2 = new Random(12345);

    DefaultCrossoverStrategy strategy1 =
      new DefaultCrossoverStrategy(0.7, random1);
    DefaultCrossoverStrategy strategy2 =
      new DefaultCrossoverStrategy(0.7, random2);

    Chromosome chromosome1 = new Chromosome("01010101010101010101");
    Chromosome chromosome2 = new Chromosome("10101010101010101010");

    ChromosomePair result1 = strategy1.crossover(chromosome1, chromosome2);
    ChromosomePair result2 = strategy2.crossover(chromosome1, chromosome2);

    // With same random seed, results should be identical
    assertEquals(result1.getFirst().toString(), result2.getFirst().toString());
    assertEquals(result1.getSecond().toString(),
      result2.getSecond().toString());
  }

  @Test
  public void testCrossoverPositionZero() {
    // Mock random to always choose position 0
    Random random = new Random(12345) {
      private static final long serialVersionUID = 6166075220140940263L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 0.7, so crossover happens
      }

      @Override
      public int nextInt(int bound) {
        return 0; // Always return position 0
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // With position 0, entire chromosomes should be swapped
    assertEquals("1010101010", result.getFirst().toString());
    assertEquals("0101010101", result.getSecond().toString());
  }

  @Test
  public void testCrossoverPositionLast() {
    // Mock random to always choose last position
    Random random = new Random(12345) {
      private static final long serialVersionUID = -2989623870636552358L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 0.7, so crossover happens
      }

      @Override
      public int nextInt(int bound) {
        return bound - 1; // Always return last position
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    Chromosome chromosome1 = new Chromosome("0101010101");
    Chromosome chromosome2 = new Chromosome("1010101010");

    ChromosomePair result = strategy.crossover(chromosome1, chromosome2);

    // With last position, only last gene should be swapped
    String result1 = result.getFirst().toString();
    String result2 = result.getSecond().toString();

    // Last character swapped
    assertTrue(result1.endsWith("0"));
    assertTrue(result2.endsWith("1"));
  }

  @Test
  public void testCrossoverWithNullChromosome1() {
    // Use a random that always triggers crossover
    Random random = new Random(12345) {
      private static final long serialVersionUID = -6036604137029043426L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 0.7, so crossover happens
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    // With null chromosome and crossover triggered, should throw
    // NullPointerException
    // because chromosome1.isCompatible(chromosome2) is called on null
    // chromosome1
    try {
      strategy.crossover(null, new Chromosome("0101010101"));
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testCrossoverWithNullChromosome2() {
    // Use a random that always triggers crossover
    Random random = new Random(12345) {
      private static final long serialVersionUID = -5237007107747366392L;

      @Override
      public double nextDouble() {
        return 0.5; // Always less than 0.7, so crossover happens
      }
    };

    DefaultCrossoverStrategy strategy =
      new DefaultCrossoverStrategy(0.7, random);

    // With null chromosome2 and crossover triggered, should throw
    // IncompatibleChromosomeException
    // because chromosome1.isCompatible(null) returns false
    try {
      strategy.crossover(new Chromosome("0101010101"), null);
      fail("Expected IncompatibleChromosomeException");
    } catch (IncompatibleChromosomeException e) {
      // Expected
    }
  }

  // Helper method for assertion
  private void assertNotEquals(String expected, String actual) {
    if (expected.equals(actual)) {
      fail("Expected not equal but was: " + actual);
    }
  }
}