import com.aigeneration.genetic.EvolutionEngine;
import com.aigeneration.genetic.Generation;
import com.aigeneration.genetic.IEvolutionEngine;
import com.aigeneration.genetic.IFitnessEvaluator;
import com.aigeneration.genetic.IMutationStrategy;
import com.aigeneration.genetic.ISelector;
import com.aigeneration.genetic.IncompatibleChromosomeException;
import com.aigeneration.genetic.TerminationCriteria;
import com.aigeneration.genetic.TerminationException;

public class CompositeEvolutionEngine implements IEvolutionEngine {
  
  private final IEvolutionEngine[] engines;
  private Generation generation;
  
  /**
   * Constructs this composite Evolution Engine.
   * @param generation the initial generation.
   * @param selector the chromosome selection strategy.
   * @param mutationStrategy the chromosome mutation strategy.
   * @param fitnessEvaluator the fitness evaluator (a.k.a. fitness function)
   * @param elitismEnabled <code>true</code> if elitism should be employed
   *        by this instance.
   * @param numOfAgents the number of sub-engines the work is delegated to.
   */
  public CompositeEvolutionEngine(Generation generation, ISelector selector,
    IMutationStrategy mutationStrategy, IFitnessEvaluator fitnessEvaluator,
    boolean elitismEnabled, int numOfAgents)
  {
    this.generation = generation;
    this.engines = new IEvolutionEngine[numOfAgents];
    for (int i = 0; i < numOfAgents; i++) {
      this.engines[i] = createEngine(generation, selector, mutationStrategy,
        fitnessEvaluator, elitismEnabled);
    }
  }
  
  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#getGeneration()
   */
  @Override
  public Generation getGeneration() {
    return generation;
  }

  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#findSolution(double, com.aigeneration.genetic.TerminationCriteria)
   */
  @Override
  public int findSolution(double fitnessTarget,
    TerminationCriteria terminationCriteria)
    throws IncompatibleChromosomeException, TerminationException {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#step()
   */
  @Override
  public void step() throws IncompatibleChromosomeException {
    for (int i = 0; i < engines.length; i++)
      engines[i].step();
  }

  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#step(double)
   */
  @Override
  public int step(double fitnessTarget) throws IncompatibleChromosomeException {
    for (int i = 0; i < engines.length; i++)
      engines[i].step(fitnessTarget);
    return 0;
  }

  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#getGenerationCount()
   */
  @Override
  public long getGenerationCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#getBestIndex()
   */
  @Override
  public int getBestIndex() {
    // TODO Auto-generated method stub
    return 0;
  }
  
  /**
   * @see com.aigeneration.genetic.IEvolutionEngine#getBestFitnessScore()
   */
  @Override
  public double getBestFitnessScore() {
    // TODO Auto-generated method stub
    return 0;
  }    
  
  private static IEvolutionEngine createEngine(Generation generation,
    ISelector selector, IMutationStrategy mutationStrategy,
    IFitnessEvaluator fitnessEvaluator, boolean elitismEnabled)
  {
    return new EvolutionEngine(generation, selector, mutationStrategy,
      fitnessEvaluator, elitismEnabled);
  }
}
