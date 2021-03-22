#VaR Project ReadMe
##Introduction
This application can calculate the Value at Risk of a financial portfolio. It uses three different calculation methods:
* The Historical Simulation Method
* The Model Building Method
* The Monte Carlo Method

The application will return a monetary Value at Risk and a Percentage at Risk for each of the three methods. The application also will return the Conditional Value at Risk/Expected Shortfall.

These are only estimations and should not be taken as a safe bet against an investment.

##Prerequisites

##### The portfolio
* The portfolio must be a .csv file. 
* The portfolio must consist of stock tickers and a number of shares.
* Each stock ticker/number of shares pairing must be on an individual line.
* There should be a single comma between the ticker and the number of shares, with no spaces anywhere.
* A line will look like this: "ticker,number".
* For example, "AMZN,20" - which means the portfolio contains 20 shares of Amazon.

** IMPORTANT NOTICE **
* To be able to find your portfolio files, they must be placed in your root Directory.
* Users should create a folder in the root directory for the portfolio files to be held. This is because the Desktop, Documents and Download folders do not hold actual files, but pointers to them.
* Follow the Instructions below for information on how to do this.

##### Setting up a location to hold portfolios
###### Unix systems
* Create a new folder in your root directory to hold portfolios:

``` 
cd
mkdir portfolios
```
* If you wish to use the demo portfolios that have been created, they must be moved into the folder at the root directory:

``` 
cd
mkdir portfolios

cd [File Path containing JAR and test Portfolios]
cd testPortfolios

cp *.csv ~/portfolios
```

* Once this is done, you will be able to use any portfolios that you create in this directory.

##### Software requirements
The application must have a few components installed for it to work:
* The system **must** have Java 8 installed. [Download and installation instructions here](https://www.java.com/en/download/help/index_installing.html)
* The system must be able to run a Maven Project, **only if you wish to run from an IDE**.
 [Download and installation instructions here](https://maven.apache.org/download.cgi)
* The system must allow access to the file system so that portfolios can be selected.
* During compilation, the Maven project will install the dependencies and packages that are required to run the Maven project.


##### Installation
To run the application there are three options.
###### Unix Systems
* For a JAR file, double click the Icon and the application will run from there.
* Alternatively, to run from the command line, use:

```
 cd [path to the file location]
 
 java -jar ValueAtRiskCalculator.jar
```
###### Windows Systems
* For an EXE file, double click the icon and the application will run from there.
* Alternatively, to run from the command line, use:

```
 cd [path to the file location]
 
 start ValueAtRiskCalculator.exe
```
###### Java files
* If you have the entire project source files and dependencies, you can import this software as a Maven project on your IDE.
* From there, navigate to the ``ValueAtRiskController.java`` file and create a build path from there.
* Run the application from there and the application will open.

##Using the application
Once the user opens the application they will be presented with a friendly User interface. From here, they must select:
* Portfolio
* Time Horizon 
* Confidence Interval

Once this is done, click ``calculate`` and wait for the results to appear.

If a mistake is made, or if new inputs wish to be explored,  press ``reset``. This will clear the interface, as if the application has just started up.

If the interface is confusing or if the results do not make sense, click ``help``. This will outline the software basics and the theory to understand the results.

Once the results appear, click either of the ``charts`` buttons, where a screen will pop up. This screen will show pie charts that give a visual representation of the results.

##Author
Software written by James Etheridge
* [Email](jamesajetheridge@hotmail.com)
* [LinkedIn](https://www.linkedin.com/in/james-etheridge-83471a195/)

##Acknowledgements
* This project was supervised by Emma Lieu, on behalf of the RHUL Computer Science Department
* Credit is also due to [Alpha Vantage](https://www.alphavantage.co), for letting me use their API free of charge for educational purposes.
* I have reused some code from [CrazzyGhost](https://github.com/crazzyghost/alphavantage-java)'s Alpha Vantage API wrapper to connect to the API and retrieve data. It is marked in the source code and JavaDoc too.

##License
This software was created by [James Etheridge](jamesajetheridge@hotmail.com) and should not be used, distributed or modified without the Authors permission. 

2020-2021 RHUL Final Year Project