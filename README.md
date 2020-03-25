# Project 2: Nondeterministic Finite Automata)

* Author: Van Nguyen, Lam Nguyen
* Class: CS361 Section Sole
* Semester: Spring 2020

## Overview

This java application will model a nondeterministic finite automata by scanning through
formatted text files to extract information of a certain machine.
## Compiling and Using

To compile, execute the following command in the main project directory:
```
$ javac fa.dfa.NFADriver
```

Run the compiled class with the command:
```
$ java fa.dfa.NFADriver ./tests/*.txt
```
The input of this program is test files under the test directories with the correct path listed.

## Discussion

The problem with this project is to _____________

This project is not really hard and the guideline provided very detailed information, thus the project went pretty well.  

We first started with the NFAState.java to implement all the method as requires and decides on what attributes we want to have for
my NFAState object. Then we went out to write NFA.java and take a look over the DFADriver.java to see how all the information are scanned.

We tested the projects with the 3 given test files but we realized that they are missing the case where the start state can be the end state so 
we added 3-4 more test cases to make sure that our machine produces correct output and we found some errors to fix as we first decided not to add the start 
state if it is already included so we have to set that start state equal to that of the start state which help with duplication problem. 

The challenging part of this project is to write the getDFA() method as there is a lot of cases to take care of as we implementing the
breadth first search.It was a fun project and we learned some organization skill through it as well
as about how to implement breadth first search and depth first search. 

We think the project was good, there is no needed to change anything about it. 
We think that this project was a good base for building up a more complex machine.

## Testing
We tested the projects with the 4 given test files but we realized that they are missing the case where the start state can be the end state so 
we added 3-4 more test cases to make sure that our machine produces correct output. We split the testing part between the 2 of us to make sure there is no mistakes when testing.

## Sources used
I used the documentation for HashSet and HashMap and the project description. For creating the getDFA() method I looked at documentation for Queue.
