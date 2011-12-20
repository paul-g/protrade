LOGGER=org.apache.tools.ant.listener.AnsiColorLogger
all:
	ant -logger $(LOGGER) all
git:
	ant -logger $(LOGGER) git
