CSE-210A 
HOMEWORK 1
Interpreter for the ARITH language


Name: Rashmi Chennagiri
Email: rchennag@ucsc.edu


Language: Python 3.7.6


Folder Structure:
    rashmi-MacBook-Pro:hw1 rashmichennagiri$ ls
    README.md       arith       arith.py        arith.spec
    libexec         bin         test.sh	        tests
    makefile

HW1 code: arith.py
Makefile: makefile
Test-related: test.sh, tests/, libexec/, bin/ (custom test cases in ‘tests/custom.bats’)
Build-related: arith arith.spec


To create executable:
1. pyinstaller --onefile arith.py 
2. An executable named 'arith' will be created in the 'dist' folder


To run the executable:
1. ./arith  (run this from the 'dist' folder)
2. Enter the expression to be evaluated 
     i. press 'enter' to execute
    ii. 'ctrl+D' (EOF) to stop


To run test script:
2. cp dist/arith .
3. ./test.sh
4. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases



References:
1. For arith.py:
    Let’s Build A Simple Interpreter in Python - parts 1 to 8
    https://ruslanspivak.com/lsbasi-part1/
2. For the makefile: 
    https://gist.github.com/lumengxi/0ae4645124cd4066f676
3. Testing scripts:
    https://github.com/versey-sherry/cse210A-asgtest/tree/master/cse210A-asgtest-hw1-arith
