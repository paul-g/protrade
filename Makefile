LOGGER=org.apache.tools.ant.listener.AnsiColorLogger
all:
	ant -logger $(LOGGER) all
git-all:
	ant -logger $(LOGGER) git-all
clean:
	ant clean
