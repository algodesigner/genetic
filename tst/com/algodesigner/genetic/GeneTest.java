package com.algodesigner.genetic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Gene test cases.
 * 
 * @author Vlad Shurupov
 * @version 1.02
 */
public class GeneTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullValue() {
    new Gene(null);
  }

  @Test
  public void testEquals() {
    Gene gene1 = new Gene(true);
    Gene gene2 = new Gene(false);
    Gene gene3 = new Gene(true);

    assertEquals(gene1, gene1);
    assertEquals(gene1, gene3);
    assertTrue(!gene1.equals(gene2));
    assertFalse(gene1.equals(null));
    assertFalse(gene1.equals(new Object()));
  }

  @Test
  public void testHashCode() {
    Gene gene1 = new Gene(Boolean.TRUE);
    Gene gene2 = new Gene(Boolean.TRUE);
    assertEquals(gene1.hashCode(), gene2.hashCode());
  }
}
