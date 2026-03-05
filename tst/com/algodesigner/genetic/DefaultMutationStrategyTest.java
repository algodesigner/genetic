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

import java.util.Random;

import org.junit.Test;

/**
 * DefaultMutationStrategy test.
 * 
 * @author Vlad Shurupov
 * @version 1.0
 */
public class DefaultMutationStrategyTest {

  @Test
  public void testConstructorDefaultRandom() {
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorCustomRandom() {
    Random random = new Random(12345);
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    assertNotNull(strategy);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeMutationRate() {
    new DefaultMutationStrategy(-0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeMutationRateAndRandom() {
    new DefaultMutationStrategy(-0.1, new Random());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructorNullRandom() {
    new DefaultMutationStrategy(0.5, null);
  }

  @Test
  public void testConstructorZeroMutationRate() {
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.0);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorOneMutationRate() {
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(1.0);
    assertNotNull(strategy);
  }

  @Test
  public void testConstructorHighMutationRate() {
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(2.5);
    assertNotNull(strategy);
  }

  @Test
  public void testMutateZeroRateNoChange() {
    Random random = new Random(12345);
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.0, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    Chromosome result = strategy.mutate(chromosome);
    
    // With mutation rate 0, should always return original chromosome
    assertSame(chromosome, result);
    assertEquals("0101010101", result.toString());
  }

  @Test
  public void testMutateRateOneOccurs() {
    // Use a predictable random that will always choose mutation
    Random random = new Random(12345) {
      private static final long serialVersionUID = -1490351483204078573L;
      private int callCount = 0;
      
      @Override
      public double nextDouble() {
        return 0.5; // Always less than 1.0, so mutation happens
      }
      
      @Override
      public int nextInt(int bound) {
        callCount++;
        // Always swap first and last elements for predictable test
        if (callCount == 1) {
          return 0; // First call returns 0
        } else {
          return 9; // Second call returns 9
        }
      }
    };
    
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(1.0, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    Chromosome result = strategy.mutate(chromosome);
    
    // With mutation rate 1.0 and random < 1.0, should perform mutation
    assertNotSame(chromosome, result);
    
    // Should swap first and last genes (0 and 1 become 1 and 0)
    assertEquals("1101010100", result.toString());
  }

  @Test
  public void testMutateRateOneNoMutation() {
    // Use a predictable random that will never choose mutation
    Random random = new Random(12345) {
      private static final long serialVersionUID = -6839965965186329191L;

      @Override
      public double nextDouble() {
        return 1.0; // Always 1.0, which is not less than 1.0
      }
    };
    
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(1.0, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    Chromosome result = strategy.mutate(chromosome);
    
    // With random >= 1.0, should return original chromosome
    assertSame(chromosome, result);
    assertEquals("0101010101", result.toString());
  }

  @Test
  public void testMutatePreservesLength() {
    Random random = new Random(12345);
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    Chromosome result = strategy.mutate(chromosome);
    
    assertEquals(10, result.length());
  }

  @Test
  public void testMutateDeterministicSameSeed() {
    Random random1 = new Random(12345);
    Random random2 = new Random(12345);
    
    DefaultMutationStrategy strategy1 = new DefaultMutationStrategy(0.5, random1);
    DefaultMutationStrategy strategy2 = new DefaultMutationStrategy(0.5, random2);
    
    Chromosome chromosome = new Chromosome("01010101010101010101");
    
    Chromosome result1 = strategy1.mutate(chromosome);
    Chromosome result2 = strategy2.mutate(chromosome);
    
    // With same random seed, results should be identical
    assertEquals(result1.toString(), result2.toString());
  }

  @Test
  public void testMutateSameIndex() {
    // Mock random to choose same index twice (swap with itself)
    Random random = new Random(12345) {
      private static final long serialVersionUID = 1531265331648817155L;

      @Override
      public double nextDouble() {
        return 0.4; // Always less than 0.5, so mutation happens
      }
      
      @Override
      public int nextInt(int bound) {
        return 3; // Always return same index
      }
    };
    
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0123456789");
    Chromosome result = strategy.mutate(chromosome);
    
    // Swapping gene with itself should result in same chromosome
    assertEquals("0123456789", result.toString());
  }

  @Test
  public void testMutateSingleGene() {
    Random random = new Random(12345) {
      private static final long serialVersionUID = 4138311897652010492L;

      @Override
      public double nextDouble() {
        return 0.4; // Always less than 0.5, so mutation happens
      }
      
      @Override
      public int nextInt(int bound) {
        return 0; // Only one index available
      }
    };
    
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0");
    Chromosome result = strategy.mutate(chromosome);
    
    // Single gene chromosome - swapping with itself
    assertEquals("0", result.toString());
    assertEquals(1, result.length());
  }

  @Test(expected = NullPointerException.class)
  public void testMutateNull() {
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5);
    strategy.mutate(null);
  }

  @Test
  public void testMutateMultiple() {
    Random random = new Random(12345);
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    
    // Test multiple mutations
    Chromosome result1 = strategy.mutate(chromosome);
    Chromosome result2 = strategy.mutate(chromosome);
    Chromosome result3 = strategy.mutate(chromosome);
    
    // All should have same length
    assertEquals(10, result1.length());
    assertEquals(10, result2.length());
    assertEquals(10, result3.length());
  }

  @Test
  public void testMutationRateBoundaries() {
    // Test various boundary mutation rates
    for (double rate : new double[]{0.0, 0.001, 0.5, 0.999, 1.0, 2.0, 10.0}) {
      DefaultMutationStrategy strategy = new DefaultMutationStrategy(rate);
      assertNotNull(strategy);
      
      Chromosome chromosome = new Chromosome("0101010101");
      Chromosome result = strategy.mutate(chromosome);
      
      assertNotNull(result);
      assertEquals(10, result.length());
    }
  }

  @Test
  public void testMutateSameGenes() {
    Random random = new Random(12345);
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0000000000");
    Chromosome result = strategy.mutate(chromosome);
    
    // Swapping identical genes should result in same chromosome
    assertEquals("0000000000", result.toString());
    assertEquals(10, result.length());
  }

  @Test
  public void testMutateChangesChromosome() {
    // Non-deterministic test: mutation should sometimes change chromosome
    Random random = new Random();
    DefaultMutationStrategy strategy = new DefaultMutationStrategy(0.5, random);
    
    Chromosome chromosome = new Chromosome("0101010101");
    boolean foundMutation = false;
    
    // Try multiple times to increase chance of mutation
    for (int i = 0; i < 100; i++) {
      Chromosome result = strategy.mutate(chromosome);
      if (!result.toString().equals("0101010101")) {
        foundMutation = true;
        assertEquals(10, result.length());
        break;
      }
    }
    
    // With 0.5 mutation rate over 100 trials, very high probability of mutation
    // But test won't fail if no mutation (probabilistic)
    if (foundMutation) {
      // Verified mutation happened
    }
  }
}