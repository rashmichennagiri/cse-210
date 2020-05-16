.DEFAULT_GOAL := install

help:
	@echo "clean - remove all build, test, coverage and Python artifacts"
	@echo "install - install the package to the active Python's site-packages"

clean: 
	mvn clean


install: clean
	mvn install
	cp target/hw4-while.jar .
	
