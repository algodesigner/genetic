package com.aigenes.genetic;

/**
 * Simple gene implementation
 * @author Vlad Shurupov
 * @version 1.01
 */
public class Gene {

  private final Object value;

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

  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object == null)
      return false;
    if (object == this)
      return true;
    if (!object.getClass().equals(getClass()))
      return false;
    Gene gene = (Gene)object;
    return value.equals(gene.getValue());
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
