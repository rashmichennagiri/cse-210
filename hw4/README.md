CSE-210A  HOMEWORK 4


Small-step WHILE interpreter


Name: Rashmi Chennagiri


Email: rchennag@ucsc.edu


Language: Python 3.7.6 (pyinstaller 3.6)


HW4 code: while-ss.py

Makefile: makefile

Custom test cases in ‘tests/custom.bats’

Name of executable: while-ss

Test evidence: cse-210-hw4-report.pdf


To create executable:
1. pyinstaller --onefile while-ss.py 
2. An executable named 'while-ss' will be created in the 'dist' folder


To run the executable:
1. ./while-ss  (run this from the 'dist' folder)
2. Enter the expression to be evaluated 
     i. press 'enter' to execute
    ii. 'ctrl+D' (EOF) to stop


To run test script:
1. cp dist/while-ss .
2. ./test.sh
3. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases


To run submitted homework from zip file:
1. unzip cse210-hw4.zip
2. ./test.sh
3. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases



References:
1. For while-ss.py:
https://craftinginterpreters.com/statements-and-state.html
https://ruslanspivak.com/lsbasi-part9/
https://github.com/versey-sherry/while
2. For the makefile: 
    https://gist.github.com/lumengxi/0ae4645124cd4066f676
3. Testing scripts: 
    https://github.com/versey-sherry/cse210A-asgtest/tree/master/cse210A-asgtest-hw1-arith
