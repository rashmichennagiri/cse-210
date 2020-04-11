CSE-210A 
HOMEWORK 1
Interpreter for the ARITH language


Name: Rashmi Chennagiri
Email: rchennag@ucsc.edu


<br>
Language: Python 3.7.6


<br>
Folder Structure:\
(base) RASHMIs-MacBook-Pro:hw1 rashmichennagiri$ ls -lrt\
total 12992\
-rw-r--r--@ 1 rashmichennagiri  staff     2143 Apr 10 18:49 makefile\
drwxr-xr-x@ 3 rashmichennagiri  staff       96 Apr 10 18:50 bin\
drwxr-xr-x@ 3 rashmichennagiri  staff       96 Apr 10 18:50 libexec\
-rwxr-xr-x@ 1 rashmichennagiri  staff       44 Apr 10 18:50 test.sh\
drwxr-xr-x@ 6 rashmichennagiri  staff      192 Apr 10 21:53 tests\
-rw-r--r--@ 1 rashmichennagiri  staff   306759 Apr 10 22:10 cse-210-hw1-report.pdf\
-rw-r--r--@ 1 rashmichennagiri  staff    10640 Apr 10 22:27 arith.py\
-rw-r--r--@ 1 rashmichennagiri  staff     1349 Apr 10 22:31 README.md\
-rw-r--r--  1 rashmichennagiri  staff      909 Apr 10 22:31 arith.spec\
-rwxr-xr-x  1 rashmichennagiri  staff  5754692 Apr 10 22:31 arith\

HW1 code: arith.py\
Makefile: makefile\
Test-related: test.sh, tests/, libexec/, bin/ (custom test cases in ‘tests/custom.bats’)\
Build-related: arith arith.spec\
Test evidence: cse-210-hw1-report.pdf\


<br>
To create executable:
1. pyinstaller --onefile arith.py 
2. An executable named 'arith' will be created in the 'dist' folder


<br>
To run the executable:
1. ./arith  (run this from the 'dist' folder)
2. Enter the expression to be evaluated 
     i. press 'enter' to execute
    ii. 'ctrl+D' (EOF) to stop


<br>
To run test script:
2. cp dist/arith .
3. ./test.sh
4. You have two options here:
     i. Enter the expression to be evaluated
    ii. Press 'ctrl+D' to trigger the execution of test cases


<br>
<br>
References:
1. For arith.py:
    Let’s Build A Simple Interpreter in Python (parts 1 to 8) 
    https://ruslanspivak.com/lsbasi-part1/
2. For the makefile: 
    https://gist.github.com/lumengxi/0ae4645124cd4066f676
3. Testing scripts: 
    https://github.com/versey-sherry/cse210A-asgtest/tree/master/cse210A-asgtest-hw1-arith
