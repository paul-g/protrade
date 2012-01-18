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


## Finally

You must also create a file called build.properties in the project root (i.e. under protrade/).
(Simply copy protrade/examples/build.properties.example to protrade/build.properties)
Then set lin.ver to either 32 or 64, based on your linux version(32-bit/64-bit) and you're good to go.

# Usage

After installation, to run the project:

`ant run` 

If everything is OK, a login window will appear, prompting you for a Betfair account and password.


#Note!

A web site for the latest protrade release is under construction.

