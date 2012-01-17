To CHECKOUT project from github:

  mkdir tennis-trader
  cd tennis-trader
  git init
  git remote add origin git@github.com:paul-g/tennis-trader.git
  git pull -u origin master


To RUN you can use Ant (make sure you have it installed properly):

    First create a file called build.properties in the project root.
    (Simply copy tennis-trader/examples/build.properties.example to tennis-trader/build.properties)
    Then set lin.ver to either 32 or 64, based on your linux version(32-bit/64-bit) and you're good to go.

   Available targets are:
    ant clean
    ant compile - to build all classes under src/ in build/prod/
    ant run - to run the src.HelloWorld class
    
   If everything is OK, you should see a window with a tree list pop up.


To COMMIT:
  
  NOTE!!!!!! 
  Please do not commit any local configuration files, build outputs, test outputs etc. to the respository.

 ant clean                -> removes build output
 git status               -> shows a list of changed / added files
 git add <file1> <file2>  -> list all the files you want to commit
 git commit -m "<Comment for commit>"


To CHECKIN to github:
  git push -u origin master



protrade
=========

protrade is a client desktop application for tennis trading.
It connects users to the Betfair exchange (hence requires a Betfair account) and allows visualization of market data, as well as match & player statistics and live scores.
The project uses protrade-data (on Github here: https://github.com/paul-g/protrade-data), an API which provides a high level match model and handles the intricacies of fetching all the required information from external sources.


## Note! 
This project will soon be moved to a public repository.

# Installation instructions

The build process uses make and ant.
The Makefile only provides a wrapper for the ant script, so it is not really required, but provides a few simple tasks to pass some parameters to the ant runtime.

To install ant, follow the instructions here: http://ant.apache.org/.

To list all available tasks:
`ant -p` 

To install the project:

## Get the code from Github (i.e. here)

```
  mkdir protrade
  cd protrade
  git init
  git remote add origin git@github.com:paul-g/tennis-trader.git
  git pull -u origin master
```
## Build the project

First option (using make):

```
cd wokspace/protrade-data
make
```

(This will soon be added -> Second option:

```
ant init-ivy  (fetch ivy  - required for managing dependencies)
ant resolve   (resolve dependencies from maven central, using ivy)
ant all       (run a standard build)
```
)

## Done

If everything went well, you should be done at this stage :p

# Usage

After installation, to run the project:

`ant run` 

You will be prompted for a Betfair account and password.
