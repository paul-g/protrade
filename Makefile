include Makefile.local

all: build run
	
build:
	javac -cp lib/swt-$(LIN_VER)/swt.jar:. src/HelloWorld.java 

run:
	java  -cp lib/swt-$(LIN_VER)/swt.jar:. src.HelloWorld
	
clean:
	rm -f src/*.class
