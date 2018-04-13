package com.aigenes.genetic;

import static org.junit.Assert.*;

import org.junit.Test;

public class IncompatibleChromosomeExceptionTest {

  @Test
  public void testBehaviour() {
    IncompatibleChromosomeException e =
      new IncompatibleChromosomeException("hello");
    assertEquals("hello", e.getMessage());
  }
}
