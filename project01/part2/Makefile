all: 
	@make compile && make execute

myall:
	@make compile && make myexecute && make test

compile:
	jflex scanner.flex
	java -jar ../java-cup-11b.jar -interface -parser Parser parser.cup 
	javac -cp ../java-cup-11b-runtime.jar *.java

execute:
	@java -cp ../java-cup-11b-runtime.jar:. Driver

myexecute:
	@java -cp ../java-cup-11b-runtime.jar:. Driver < input > Main.java

test:
	@javac Main.java
	@java Main

clean:
	rm -f *.class *~ Main.java Parser.java Scanner.java sym.java