compile:
	javac *.java

n1:
	java Node 1
	
n2:
	java Node 2

n3:
	java Node 3

clean:
	rm *.class
	rm *.txt
	rm 1/*.txt
	rm 2/*.txt
	rm 3/*.txt


