all: 
	make compile
	make run
	make clang

clang:
	clang -o out1 Example.ll
	./out1

compile:
	java -jar ../jtb132di.jar -te minijava.jj
	java -jar ../javacc5.jar minijava-jtb.jj
	javac Main.java

run:
	java Main Example.java

clean:
	rm -f *.class *~

