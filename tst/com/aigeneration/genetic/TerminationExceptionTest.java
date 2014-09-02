package com.aigeneration.genetic;

import static org.junit.Assert.*;
import org.junit.Test;
import com.aigeneration.genetic.TerminationException;

public class TerminationExceptionTest {

  private static final int BEST_INDEX = 5;
  private static final String EXCEPTION_MESSAGE = "Boom!";

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeBestIndex() {
    new TerminationException(EXCEPTION_MESSAGE, -1);
  }

  @Test
  public void testBehaviour() {
    TerminationException exception = new TerminationException(
      EXCEPTION_MESSAGE, BEST_INDEX);
    assertEquals(EXCEPTION_MESSAGE, exception.getMessage());
    assertEquals(BEST_INDEX, exception.getBestIndex());
  }
}
