LOGGER=org.apache.tools.ant.listener.AnsiColorLogger
all:
	ant -logger $(LOGGER) init-ivy
	ant -logger $(LOGGER) resolve
	ant -logger $(LOGGER) all
git-all:
	ant -logger $(LOGGER) git-all
clean:
	ant clean
	rm -f *.log
clean-lib:
	ant clean-lib