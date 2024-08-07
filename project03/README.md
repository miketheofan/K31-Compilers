# Project 3 - K31 Compilers

# LLVM Code Generation for Java Programs

## Overview

This project extends the previous assignment by adding LLVM code generation capabilities. The updated implementation includes an additional visitor, `LLVMvisitor`, which generates LLVM Intermediate Representation (IR) from Java source files. This visitor is integrated into the existing framework and is invoked alongside other visitors from `Main.java`. Note that the functionality for printing offsets has been omitted in this version.

## Build and Execution

To build and run the program, follow these steps:

- **Compile the Program**:
  ```
  make compile
  ```

- **Run the Program**:
  ```
  make run
  ```
  This command executes the program using the `Example.java` file, which by default contains one of the provided LLVM examples.

- **Output Files**:
  The output of the program is written to one or more files with the `.ll` extension. The names of these files correspond to the Java source files provided as parameters.

## Implementation Details

- **LLVM Visitor**:
  - **LLVMvisitor**: This new visitor, implemented in `LLVMvisitor.java`, generates LLVM IR for Java source files. It is called along with the other visitors in `Main.java`.

- **Vtables**:
  - Two vtables are used:
    - One for each class, including its methods.
    - One for each field of a class.

- **Auxiliary Data Structures**:
  - **LinkedHashMaps**: Two `LinkedHashMaps` are used to facilitate recursive visitor calls that return registers and for other operations.
  - **Counters**: Two counters are maintained to generate unique registers and labels for the LLVM IR code.

- **Code Changes**:
  - The `printOffsets()` functionality from the previous assignment has been removed in this version.

## Usage

1. **Compile**:
   ```
   make compile
   ```

2. **Run**:
   ```
   make run
   ```
   This command will process `Example.java` and generate the LLVM IR output.

3. **Check Output**:
   The generated LLVM IR files will be named according to the Java source files provided as parameters.

## Code Comments

The code contains comments to explain design decisions and implementation details, including the new LLVM IR generation.
