package com.aigenes.genetic;

import static org.junit.Assert.fail;
import org.junit.Test;

import com.aigenes.genetic.Generation;
import com.aigenes.genetic.IEvolutionEngine;
import com.aigenes.genetic.IncompatibleChromosomeException;
import com.aigenes.genetic.TerminationCriteria;
import com.aigenes.genetic.TerminationEvaluator;
import com.aigenes.genetic.TerminationException;

/**
 * TerminationEvaluator test cases
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
  public void testOperation() throws TerminationException, InterruptedException {
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
    try {
      terminationEvaluator.evaluate(criteria, 0);
      fail("Expected TerminationException");
    } catch (TerminationException ex) {
    }

    criteria = new TerminationCriteria(-1, 3);
    try {
      terminationEvaluator.evaluate(criteria, 0);
      fail("Expected TerminationException");
    } catch (TerminationException ex) {
    }
  }

  private class TestEvolutionEngine implements IEvolutionEngine {

    public Generation getGeneration() {
      return null;
    }

    public int findSolution(double fitnessTarget,
      TerminationCriteria terminationCriteria)
      throws IncompatibleChromosomeException, TerminationException
    {
      return -1;
    }

    public void step() throws IncompatibleChromosomeException {
    }

    public int step(double fitnessTarget)
      throws IncompatibleChromosomeException
    {
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
