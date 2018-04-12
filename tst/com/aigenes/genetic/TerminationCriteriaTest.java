package com.aigenes.genetic;

import static org.junit.Assert.*;
import org.junit.Test;

import com.aigenes.genetic.TerminationCriteria;


public class TerminationCriteriaTest {

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMaxTime() {
    new TerminationCriteria(-2, 0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMaxGenerations() {
    new TerminationCriteria(0, -3);
  }
  
  @Test
  public void testBehaviour() {
    TerminationCriteria criteria = new TerminationCriteria(6000, 20000);
    assertEquals(6000, criteria.getMaxTime());
    assertEquals(20000, criteria.getMaxGenerations());
  }
}
