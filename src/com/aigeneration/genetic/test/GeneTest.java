package com.aigeneration.genetic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.aigeneration.genetic.Gene;

/**
 * Gene test cases.
 * @author Vlad Shurupov
 * @version 1.01
 */
public class GeneTest {

  @Test
  public void testConstructor() {
    try {
      new Gene(null);
    } catch (IllegalArgumentException e) {
      return;
    }
    fail("Gene constructor must not accept null");
  }

  @Test
  public void testEquals() {
    Gene gene1 = new Gene(new Boolean(true));
    Gene gene2 = new Gene(new Boolean(false));
    Gene gene3 = new Gene(new Boolean(true));
    
    assertEquals(gene1, gene1);
    assertEquals(gene1, gene3);
    assertTrue(!gene1.equals(gene2));
  }
}
