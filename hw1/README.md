CSE-210A  HOMEWORK 1


Interpreter for the ARITH language


Name: Rashmi Chennagiri


Email: rchennag@ucsc.edu


Language: Python 3.7.6


Folder Structure:
(base) RASHMIs-MacBook-Pro:hw1 rashmichennagiri$ ls

README.md     arith.py     arith     arith.spec     bin     libexec     test.sh     tests     cse-210-hw1-report.pdf     makefile		


HW1 code: arith.py

Makefile: makefile

Test-related files: test.sh, tests/, libexec/, bin/ (custom test cases in ‘tests/custom.bats’)

Build-related files: arith arith.spec

Test evidence: cse-210-hw1-report.pdf


To create executable:
1. pyinstaller --onefile arith.py 
2. An executable named 'arith' will be created in the 'dist' folder


To run the executable:
1. ./arith  (run this from the 'dist' folder)
2. Enter the expression to be evaluated 
     i. press 'enter' to execute
    ii. 'ctrl+D' (EOF) to stop


To run test script:
1. cp dist/arith .
2. ./test.sh
3. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases


To run submitted homework from zip file:
1. unzip cse210-hw1.zip
2. ./test.sh
3. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases



References:
1. For arith.py:
    Let’s Build A Simple Interpreter in Python (parts 1 to 8) 
    https://ruslanspivak.com/lsbasi-part1/
2. For the makefile: 
    https://gist.github.com/lumengxi/0ae4645124cd4066f676
3. Testing scripts: 
    https://github.com/versey-sherry/cse210A-asgtest/tree/master/cse210A-asgtest-hw1-arith
