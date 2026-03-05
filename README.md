# algodesigner-genetic: A Lightweight Genetic Programming Library for Java

[![Java](https://img.shields.io/badge/Java-8%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

**algodesigner-genetic** is a straightforward and easy-to-use Genetic Programming (GP) library designed for Java developers. With a focus on simplicity and clarity, it provides a minimalistic framework for implementing GP algorithms without any external dependencies.

This library is ideal for educational purposes, quick prototyping, or as a foundation for more advanced genetic algorithm applications. Its intuitive design makes it accessible for both beginners and experienced developers.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Quick Start](#quick-start)
- [Core Concepts](#core-concepts)
  - [Basic Example: Dominant Gene Evolution](#basic-example-dominant-gene-evolution)
  - [Practical Example: Max Area Problem](#practical-example-max-area-problem)
  - [Practical Example: Subtraction Problem](#practical-example-subtraction-problem)
- [Customizing Evolution](#customizing-evolution)
- [API Overview](#api-overview)
  - [Key Classes](#key-classes)
  - [Interfaces](#interfaces)
- [Building from Source](#building-from-source)
- [Running Tests](#running-tests)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Zero dependencies** - Pure Java implementation
- **Simple API** - Easy to learn and use
- **Flexible configuration** - Customizable crossover, mutation, and selection strategies
- **Extensible design** - Implement your own fitness functions and strategies
- **Educational focus** - Clear, readable code with comprehensive test examples
- **Dual evolution engines** - Both simple and composite engine implementations

## Installation

### Manual Installation

Since this is a lightweight library with no dependencies, you can simply:

1. **Download the JAR**: Build the project using Ant (see below) to generate `algodesigner-genetic-0.0.2.jar`
2. **Add to classpath**: Include the JAR in your project's classpath
3. **Use the source**: Copy the source files directly into your project

### Building from Source

The recommended way to use this library is to build it from source using Ant:

```bash
# Clone the repository
git clone https://github.com/algodesigner/genetic.git
cd genetic

# Build the library (compiles, runs tests, creates JAR)
ant all

# The JAR will be created in the lib/ directory
# Use: lib/algodesigner-genetic-0.0.2.jar
```

## Quick Start

Here's a minimal example to get started:

```java
import com.algodesigner.genetic.*;

// Create initial generation
GenerationBuilder builder = new GenerationBuilder();
for (int i = 0; i < 100; i++) {
    builder.addChromosome("010101010101010101010101010101010101010101010101");
}
Generation initialGeneration = builder.build();

// Define fitness function
IFitnessFunction fitnessFunction = chromosome -> {
    Gene[] genes = chromosome.getGenes();
    int zeroCount = 0;
    for (Gene gene : genes) {
        if (((Character)gene.getValue()).charValue() == '0') {
            zeroCount++;
        }
    }
    return (double)zeroCount / genes.length;
};

// Create evolution engine
EvolutionEngine engine = new EvolutionEngine(
    initialGeneration,
    0.7,  // crossover rate
    0.5,  // mutation rate
    fitnessFunction,
    true  // elitism enabled
);

// Run evolution
TerminationCriteria criteria = new TerminationCriteria(3000, 10000);
engine.findSolution(1.0, criteria);

// Get results
System.out.println("Best fitness: " + engine.getBestFitnessScore());
System.out.println("Best chromosome: " + engine.getGeneration().getChromosome(engine.getBestIndex()));
```

## Core Concepts

### Basic Example: Dominant Gene Evolution

This example demonstrates a simple genetic algorithm where the fitness function promotes dominant genes (`0` or `1`) while ignoring "useless" genes (`.`):

```java
// Fitness function that favors dominant genes
IFitnessFunction fitnessFunction = chromosome -> {
    Gene[] genes = chromosome.getGenes();
    int zeroCount = 0;
    int oneCount = 0;
    for (int i = 0; i < genes.length; i++) {
        char c = ((Character)genes[i].getValue()).charValue();
        if (c == '0')
            zeroCount++;
        else if (c == '1')
            oneCount++;
    }
    return (double)Math.max(zeroCount, oneCount) / (double)genes.length;
};

// Create engine with initial population
EvolutionEngine engine = new EvolutionEngine(
    createInitialGeneration(),
    0.7,  // crossover rate
    0.5,  // mutation rate
    fitnessFunction,
    true  // elitism
);

// Run until perfect solution found or timeout
TerminationCriteria criteria = new TerminationCriteria(1000 * 60 * 3, 10000);
engine.findSolution(1.0, criteria);
```

**Sample Output:**
```
Initial generation (mixed 0s, 1s, and .s) evolves to:
000000000000000000000000000000000000000000000000 1.0 <- Winner!
```

### Practical Example: Max Area Problem

This example solves an optimization problem to maximize area given perimeter constraints:

```java
// Fitness function for maximizing area with perimeter constraint
private static class FitnessFunction implements IFitnessFunction {
    private final int maxPerimeter;
    
    public FitnessFunction(int maxPerimeter) {
        this.maxPerimeter = maxPerimeter;
    }
    
    @Override
    public double evaluate(Chromosome chromosome) {
        Gene[] genes = chromosome.getGenes();
        int a = Integer.parseInt("" + genes[0].getValue() + genes[1].getValue());
        int b = Integer.parseInt("" + genes[2].getValue() + genes[3].getValue());
        int perimeter = 2 * (a + b);
        if (perimeter > maxPerimeter)
            return 0;
        return a * b;
    }
}

// Create engine with composite strategy (multiple populations)
CompositeEvolutionEngine engine = new CompositeEvolutionEngine(
    createInitialGeneration(),
    0.7,  // crossover rate
    0.5,  // mutation rate
    new FitnessFunction(200),
    true, // elitism
    3     // number of subpopulations
);

engine.findSolution(Double.MAX_VALUE, new TerminationCriteria(3000, 3000));
```

### Practical Example: Subtraction Problem

This example solves a mathematical puzzle: find three distinct digits A, B, C such that CBA - ABC produces a valid result:

```java
// Fitness function for subtraction problem
private static class FitnessFunction implements IFitnessFunction {
    private static final int CHROMOSOME_LENGTH = 9;
    
    @Override
    public double evaluate(Chromosome chromosome) {
        if (chromosome.length() != CHROMOSOME_LENGTH)
            throw new IllegalArgumentException("Invalid chromosome length");
        
        final String cs = chromosome.toString();
        
        // Decode the first three digits (A, B, C)
        int a = cs.charAt(0) - '0';
        int b = cs.charAt(1) - '0';
        int c = cs.charAt(2) - '0';
        
        // Calculate CBA - ABC
        int result = (c * 100 + b * 10 + a) - (a * 100 + b * 10 + c);
        String resultStr = String.valueOf(result);
        
        // Evaluate fitness based on multiple criteria
        double c1 = resultStr.indexOf(cs.charAt(0)) != -1 ? 1 : 0;  // A in result
        double c2 = resultStr.indexOf(cs.charAt(1)) != -1 ? 1 : 0;  // B in result
        double c3 = resultStr.indexOf(cs.charAt(2)) != -1 ? 1 : 0;  // C in result
        double c4 = (a != b && b != c) ? 1 : 0;  // All digits distinct
        double c5 = result > 0 ? 1 : 0;  // Positive result
        
        return (c1 + c2 + c3 + c4 + c5) / 5;  // Average of criteria
    }
}

// Create evolution engine
EvolutionEngine engine = new EvolutionEngine(
    createInitGeneration(),
    0.7,  // crossover rate
    0.5,  // mutation rate
    new FitnessFunction(),
    true  // elitism
);

// Create initial generation
private static Generation createInitGeneration() {
    GenerationBuilder builder = new GenerationBuilder();
    // Create population of chromosomes with digits 1-9
    builder.addChromosomes(256, "123456789");
    return builder.build();
}

// Run evolution
engine.findSolution(1.0, new TerminationCriteria(3000, 3000));

// Display solution
Chromosome solution = engine.getGeneration().getChromosome(engine.getBestIndex());
System.out.println("Solution found in " + engine.getGenerationCount() + " generations:");
System.out.println("Chromosome: " + solution);
```

**Sample Solution:**
```
  CBA
- ABC
  ---
  495
```
Where A=4, B=9, C=5 gives 594 - 459 = 495

## Customizing Evolution

The library provides several customization points:

### Configuring Evolution Parameters

```java
// Create engine with custom parameters
EvolutionEngine engine = new EvolutionEngine(
    initialGeneration,
    0.7,   // crossover rate (0.0 to 1.0)
    0.05,  // mutation rate (0.0 to 1.0)
    fitnessFunction,
    true,  // elitism (preserve best individuals)
    new Random(12345)  // optional random seed for reproducibility
);

// Configure termination criteria
TerminationCriteria criteria = new TerminationCriteria(
    5000,   // maximum time in milliseconds
    10000   // maximum number of generations
);

// Find solution with target fitness
int solutionIndex = engine.findSolution(0.95, criteria);
```

### Implementing Custom Fitness Functions

```java
public class MyFitnessFunction implements IFitnessFunction {
    @Override
    public double evaluate(Chromosome chromosome) {
        // Decode chromosome into solution representation
        Gene[] genes = chromosome.getGenes();
        
        // Calculate fitness (higher is better)
        double fitness = 0.0;
        // ... your fitness calculation logic
        
        return fitness;
    }
}
```

### Using Composite Evolution Engine

The `CompositeEvolutionEngine` maintains multiple subpopulations for better diversity:

```java
CompositeEvolutionEngine engine = new CompositeEvolutionEngine(
    initialGeneration,
    0.7,   // crossover rate
    0.5,   // mutation rate
    fitnessFunction,
    true,  // elitism
    3      // number of subpopulations
);
```

## API Overview

### Key Classes

| Class | Purpose |
|-------|---------|
| `EvolutionEngine` | Main evolution engine with standard genetic algorithm |
| `CompositeEvolutionEngine` | Engine with multiple subpopulations for better diversity |
| `Chromosome` | Represents an individual solution with genes |
| `Gene` | Basic building block of a chromosome |
| `Generation` | Collection of chromosomes in a population |
| `GenerationBuilder` | Helper for creating initial generations |
| `TerminationCriteria` | Defines when evolution should stop (time/generations) |
| `DefaultSelector` | Default selection strategy (roulette wheel) |

### Interfaces

| Interface | Purpose |
|-----------|---------|
| `IEvolutionEngine` | Evolution engine contract |
| `IFitnessFunction` | Fitness evaluation contract |
| `ISelector` | Selection strategy contract |
| `ICrossoverStrategy` | Crossover strategy contract |
| `IMutationStrategy` | Mutation strategy contract |

## Building from Source

### Prerequisites

- Java 8 or higher
- Apache Ant 1.9+

### Using Ant

The project includes an Ant build file (`build.xml`) that handles everything:

```bash
# Build everything (compile, test, create JAR)
ant all

# Run tests only
ant test

# Clean all build artifacts
ant clean

# Generate Javadoc documentation
ant javadoc

# Just compile the source
ant compile

# Create JAR file (after compilation)
ant jar

# Run the example (requires jar target first)
ant run
```

### Build Output

After running `ant all`, you'll get:
- `lib/algodesigner-genetic-0.0.2.jar` - Main library JAR
- `lib/algodesigner-genetic-0.0.2-sources.jar` - Source code JAR
- `lib/algodesigner-genetic-0.0.2-javadoc.jar` - Javadoc JAR
- Compiled classes in `obj/` directory

## Running Tests

The library includes comprehensive unit tests that serve as practical examples. When you run the tests, you'll see actual evolution in action:

```bash
# Run all tests with Ant (shows evolution progress)
ant test

# Example output from EvolutionEngineTest:
# Shows initial generation with mixed 0s, 1s, and .s
# Then shows evolved generation with dominant genes winning
# Final output shows perfect chromosome with fitness 1.0
```

Key test files to explore:
- `EvolutionEngineTest.java` - Basic evolution example (dominant gene problem)
- `MaxAreaTest.java` - Optimization problem example (maximize area with perimeter constraint)  
- `SubtractionProblemTest.java` - Mathematical problem example (CBA - ABC puzzle)
- `ExtendedEvolutionEngineTest.java` - Advanced usage examples (currently ignored)

**Test Output Examples:**
- **EvolutionEngineTest**: Shows chromosomes evolving from mixed patterns (`010101...`, `101010...`) to uniform solutions (`000000...` or `111111...`)
- **SubtractionProblemTest**: Finds digits A=4, B=9, C=5 that solve 954 - 459 = 495
- **MaxAreaTest**: Optimizes rectangle dimensions to maximize area within perimeter constraints

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the BSD 3-Clause License - see the [LICENSE](https://opensource.org/license/bsd-3-clause) file for details.

Copyright (c) 2008-2023, Vlad Shurupov
