# Project 2 - K31 Compilers
# Semantic Analyzer for Java Programs

## Overview

This project implements a semantic analyzer for Java programs. The analyzer is designed to perform various semantic checks on Java source files, ensuring the correctness of identifiers and type usage. It consists of two main visitors that traverse the abstract syntax tree (AST) to collect and analyze semantic information.

## Build and Execution

To build and run the program, follow these instructions:

- **Compile the Program**:
  ```
  make compile
  ```

- **Execute the Program**:
  ```
  java Main [file1] [file2] ... [fileN]
  ```
  Where `file1`, `file2`, etc., are the files provided as arguments to the program for semantic analysis.

- **Dependencies**:
  Ensure that the required `.jar` files are located in the `../` directory.

- **Run the Example**:
  To compile and run the `Example.java` file, use:
  ```
  make all
  ```
  or simply:
  ```
  make
  ```

## Implementation Details

- **Visitors**:
  - **First Visitor**: Collects all identifiers (e.g., classes, methods, fields) and inserts them into the symbol table. It performs checks to ensure that identifiers are declared correctly and throws exceptions if errors are found (e.g., redefining a field with a different type or redeclaring a class).
  - **Second Visitor**: Performs additional checks, such as verifying that types in expressions match the expected types, using the information collected in the symbol table by the first visitor.

- **Symbol Table**:
  The symbol table is implemented using Java's `LinkedHashMap` for maintaining the hierarchy of data:
  1. **Symbol Table (ST)**: Maintains a `LinkedHashMap` of all declared classes.
  2. **Classes**: Each class contains:
     - A `LinkedHashMap` for all declared fields.
     - A `LinkedHashMap` for all declared methods.
  3. **Methods**: Each method has:
     - A `LinkedHashMap` for function parameters.
     - A `LinkedHashMap` for fields declared within the method.
  4. **Fields**: Each field maintains information about the variable's name and type.

- **Argument Helper Class**:
  An `arguHelper` class is used to provide information about which class and method a particular visit call is associated with.

- **Example File**:
  The `Example.java` file, included in the assignment's zip file, contains one of the examples provided.

## Code Comments

The code includes comments to facilitate understanding of the design choices and implementation details.

## Offsets Printing

After both visitors have completed their tasks, a method in the symbol table class is called to gather and print information about offsets, based on the collected data.
