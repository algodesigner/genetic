package com.aigeneration.genetic;

/**
 * Simple gene implementation
 * @author Vlad Shurupov
 * @version 1.0
 */
public class Gene {

  private Object value;

  /**
   * Constructs a Gene object.
   * @param value the gene value
   */
  public Gene(Object value) {
    if (value == null)
      throw new IllegalArgumentException("Gene value cannot be null");

    this.value = value;
  }

  /**
   * Returns the value of this gene.
   * @return the value of this gene
   */
  public Object getValue() {
    return value;
  }

  public String toString() {
    return value.toString();
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object object) {
    if (object == null)
      return false;
    if (object == this)
      return true;
    if (!object.getClass().equals(getClass()))
      return false;
    Gene otherGene = (Gene)object;
    return value.equals(otherGene.getValue());
  }
  
  public int hashCode() {
    return value.hashCode();
  }
}
