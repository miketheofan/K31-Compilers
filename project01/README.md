# Project 1 - K31 Compilers

# Part 1

## Overview

This project is an implementation of an LL(1) parser designed to parse and evaluate arithmetic expressions. The grammar used in this parser is capable of handling addition, multiplication, and parenthesized expressions. The parser adheres to the LL(1) parsing strategy, which ensures that each step of the parsing process is deterministic and requires a single lookahead token.

## Grammar

The grammar for this parser is defined as follows:

```
1. expr    = term expr2
3. expr2   = + term expr2
4.         | - term expr2
5.         | ε
7. term    = number term2
9. term2   = ** number term2
10.        | ε
12. number = digit mul
13.        | ( expr )
15. mul    = number
16.        | ε
18. digit  = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
```

### FIRST Sets

- **FIRST(expr)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(expr2)** = {+, -}
- **FIRST(term)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(term2)** = {**}
- **FIRST(number)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(digit)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
- **FIRST(mul)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}

### FOLLOW Sets

- **FOLLOW(expr)** = {)}
- **FOLLOW(expr2)** = {$}
- **FOLLOW(term)** = {+, -, $}
- **FOLLOW(term2)** = {$}
- **FOLLOW(number)** = {**, $}
- **FOLLOW(mul)** = {$}
- **FOLLOW(digit)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (, $}

## Features

- **LL(1) Parsing**: The parser is designed to handle LL(1) grammars, ensuring a single lookahead token is sufficient to make parsing decisions.
- **Error Handling**: Custom error handling has been implemented to report parsing errors clearly and accurately.
- **Arithmetic Expressions**: Supports addition (`+`), subtraction (`-`), multiplication (`**`), and parentheses for grouping expressions.

## Usage

1. **Clone the Repository**: 
   ```
   git clone https://github.com/yourusername/ll1-parser.git
   ```

2. **Navigate to the Project Directory**:
   ```
   cd ll1-parser
   ```

3. **Compile the Program**:
   Follow the instructions provided in the `Makefile` or use your preferred method to compile the source code.

4. **Run the Parser**:
   ```
   ./parser input.txt
   ```
   Where `input.txt` contains the arithmetic expressions to be parsed.

5. **View Output**:
   The program will output the results of parsing the expressions or report any syntax errors encountered.

## Notes

- The parser has been designed based on the format and techniques discussed in the first lab of the course.
- Modifications were made to handle parsing errors more effectively and ensure correct output for various input scenarios.

# Part 2

## Overview

This project is an implementation of an LL(1) parser designed to parse and evaluate arithmetic expressions. The parser handles addition, multiplication, and parenthesized expressions, following the LL(1) parsing strategy to ensure deterministic parsing with a single lookahead token.

## Grammar

The grammar for this parser is defined as follows:

```
1. expr    = term expr2
3. expr2   = + term expr2
4.         | - term expr2
5.         | ε
7. term    = number term2
9. term2   = ** number term2
10.        | ε
12. number = digit mul
13.        | ( expr )
15. mul    = number
16.        | ε
18. digit  = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
```

### FIRST Sets

- **FIRST(expr)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(expr2)** = {+, -}
- **FIRST(term)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(term2)** = {**}
- **FIRST(number)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}
- **FIRST(digit)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
- **FIRST(mul)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (}

### FOLLOW Sets

- **FOLLOW(expr)** = {)}
- **FOLLOW(expr2)** = {$}
- **FOLLOW(term)** = {+, -, $}
- **FOLLOW(term2)** = {$}
- **FOLLOW(number)** = {**, $}
- **FOLLOW(mul)** = {$}
- **FOLLOW(digit)** = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, (, $}

## Build and Execution

To compile and execute the program, use the following commands:

- **Compile the Program**:
  ```
  make compile
  ```

- **Execute the Program**:
  ```
  make execute
  ```

- **Run and Test**: To execute the generated `Main.java` file (which contains the output of the program), use:
  ```
  make test
  ```
  Alternatively, you can manually compile and run the file with:
  ```
  javac Main.java
  java Main
  ```

- **Run with Input File**: To execute the program using an input file (`input`) and save the output to `Main.java`:
  ```
  make myexecute
  ```
  By default, this uses example #3 from the assignment specification.

- **Build and Run**: The `make all` command compiles and runs the program:
  ```
  make all
  ```

- **Build, Run, and Test**: The `make myall` command performs the actions of `make all` and then runs `Main.java` to produce the output:
  ```
  make myall
  ```

## Dependencies

- The `.jar` files required for the program must be located in the `../` directory, following the format provided in the `.zip` file from the course page.

## Notes

- The exercise builds on the code presented in the lab but has been modified to meet the requirements of the assignment.
- The program incorporates a technique discussed by Dr. Smaragdakis in a lecture, using characters `)` and `{` to distinguish between top-level and lower-level calls more easily.
