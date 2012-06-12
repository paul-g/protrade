protrade
=========

protrade is a client desktop application for tennis trading.
It connects users to the Betfair exchange (hence requires a Betfair account) and allows visualization of market data, as well as match & player statistics and live scores.
The project uses protrade-data (on Github here: https://github.com/paul-g/protrade-data), an API which provides a high level match model and handles the intricacies of fetching all the required information from external sources.


# Installation instructions

The build process uses make and ant.
The Makefile only provides a wrapper for the ant script, so it is not really required, but provides a few simple tasks to pass some parameters to the ant runtime.

To install ant, follow the instructions here: http://ant.apache.org/.

To install the project:

## Get the code from Github (i.e. here)

```
mkdir protrade
cd protrade
git init
git remote add origin git@github.com:paul-g/protrade.git
git pull -u origin master
```
## Build the project

### Create build.properties
First, create a build.properties files in the project root where you set lin.ver to either 32 or 64, based on your linux version(32-bit/64-bit).
To do this you can run:
```
echo "lin.ver=32" > build.properties
```

To build using make:

```
make
```

To build using ant only:

```
ant init-ivy  (fetch ivy  - required for managing dependencies)
ant resolve   (resolve dependencies from maven central, using ivy)
ant all       (run a standard build)
```


## Finally



### Create config.local

To enable automatic login and to allow running the tests which require a Betfair account, a config.local file must be created under the project root. This file must contain the Betfair user account and the encrypted password.


```
username:=yourusername
password:=yourencryptedpassword
```

To help encrypt the password, a utility class is provided under:  org.ic.protrade.authentication.Encrypt.java.

# Run

Type: `ant run` 

If everything is OK, a login window will appear, prompting you for a Betfair account and password.

The task `ant run-test` allows bypassing the login to enable the functionalities which do not require a Betfair account.