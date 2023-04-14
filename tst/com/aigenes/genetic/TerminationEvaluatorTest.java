package com.aigenes.genetic;

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
