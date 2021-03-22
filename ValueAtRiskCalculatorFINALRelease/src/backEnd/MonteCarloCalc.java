/** This method calculates the value at risk of a portfolio using the monte carlo method.
 It uses the multivariate random normal method to calculate.
 * @author james etheridge
 */

package backEnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MonteCarloCalc implements Calculator {
  private Portfolio portfolio;
  private double confidence;
  private int timeHorizon;
  private double[][] covarianceMatrix;
  private double[][] correlationMatrix;
  private int portfolioSize;
  private ArrayList<Double> paths;
  
  /**This is the constructor for a monte carlo calculator.
   * Here the covariance matrix and the Cholesky decomposition is also performed. 
   This means it doesnt have to be recalculated multiple times.
   * @param p - this is the portfolio given by the user
   * @param confidence - this is the confidence interval,
   it can be between 0-100 with 1 decimal place.
   * @param timeHorizon - this is the time horizon of which we are calculating VaR for.
   */
  
  public MonteCarloCalc(Portfolio p, double confidence, int timeHorizon) {
    this.portfolio = p;
    this.confidence = confidence;
    this.timeHorizon = timeHorizon;
    this.portfolioSize = this.portfolio.getContents().size();
    //Set these up now so they dont have to be recalculated thousands of times
    //within the other methods.
    pfResize();
    this.covarianceMatrix = covarianceCalc();
    this.correlationMatrix = choleskyDecomp();
    
    
  }
  
  /**This method resizes the portfolio using listwise deletion.
    It removes stock history so that all historical values are the same sized arrays.
   * 
   */
  
  private void pfResize() {
    int small = 100000;
    //find the smallest sized stock.
    for (int i = 0; i < portfolioSize; i++) {
      if (portfolio.getContents().get(i).getHistValues().size() <= small) {
        small = portfolio.getContents().get(i).getHistValues().size();
      }
    }
    //resize all of the other stocks to the correct size.
    for (int i = 0; i < portfolioSize; i++) {
      int hvSize = portfolio.getContents().get(i).getHistValues().size();
      for (int j = hvSize - 1; j > small - 1; j--) {
        portfolio.getContents().get(i).getHistValues().remove(j);
      }
    }
    //resize all of the returns to the correct size.
    for (int i = 0; i < portfolioSize; i++) {
      int rhSize = portfolio.getContents().get(i).getReturnHistory().size();
      for (int j = rhSize - 1; j > small - 2; j--) {
        portfolio.getContents().get(i).getReturnHistory().remove(j);
      }
    }
  }

  /**This method calculates the VaR of the portfolio.
   * @return it returns a double which is the value at risk of the portfolio.
   */
  
  public double calculate() {
    paths = pathsCalculate();
    Collections.sort(paths); //Paths must be in order
    return -1 * percentilePaths(paths); //show loss as positive by * -1
    
  }
  
  /**This method calculates the covariance matrix of a portfolio. This is shows the spread of data.
   This is shown by a square matrix where each value shows the variance between two stocks.
   * 
   * @return - returns the covariance matrix of the portfolio.
   */
  
  private double[][] covarianceCalc() {
    int stockHistSize = portfolio.getContents().get(0).getReturnHistory().size();
    double[][] stockMatrix = loadMatrix(stockHistSize, portfolioSize);
    double[][] transposeStockMatrix = transpose(stockMatrix);
    double[][] covarianceMatrix = matrixMult(transposeStockMatrix, stockMatrix);
    //this is now a square matrix.
    
    return covarianceMatrix;
  }
  
  /** This method multiplies two matrices together.
   * 
   * @param m1 first matrix to be multiplied together.
   * @param m2 second matrix to be multiplied together.
   * @return result of the two matrices being multiplied together.
   */
  
  private double[][] matrixMult(double[][] m1, double[][] m2) {
    double[][] result = new double[m1.length][m2[0].length];
    for (int i = 0; i < m1.length; i++) {
      for (int j = 0; j < m2[0].length; j++) {
        for (int k = 0; k < m1[0].length; k++) {
          result[i][j] += m1[i][k] * m2[k][j]; //add multiplications to get value at point.
        }
      }
    }
    return result;
  }

  /** This transposes a matrix, which means to flip it by the diagonal.
   * 
   * @param stockMatrix this is the original matrix.
   * @return this is the transposed matrix.
   */
  
  private double[][] transpose(double[][] stockMatrix) {
    int m = stockMatrix.length;
    int n = stockMatrix[0].length;

    double[][] transposedMatrix = new double[n][m];

    for (int x = 0; x < n; x++) {
      for (int y = 0; y < m; y++) {
        transposedMatrix[x][y] = stockMatrix[y][x]; //flip matrix values/
      }
    }
    return transposedMatrix;
  }
  
  /** This method loads the starting values into the matrix.
   * 
   * @param stockHistSize this is the size of the stock history.
   * @param contentsSize this is the number of stocks.
   * @return returns a matrix of return history differences.
   */
  
  private double[][] loadMatrix(int stockHistSize, int contentsSize) {
    double[][] stockMatrix = new double[stockHistSize][contentsSize];
    for (int i = 0; i < contentsSize; i++) {
      Stock curStock = portfolio.getContents().get(i); //load current stock
      double avgHistValues = 0;
      for (int j = 0; j < stockHistSize; j++) { //work out average value each stock
        avgHistValues += curStock.getReturnHistory().get(j); 
      }
      avgHistValues = avgHistValues / (curStock.getReturnHistory().size());
      for (int k = 0; k < stockHistSize; k++) {
        stockMatrix[k][i] = curStock.getReturnHistory().get(k) - avgHistValues;
        //take average off each return value.
        stockMatrix[k][i] /= 100; //Downscaling to go from percent to decmial percent.
      }
    }
    return stockMatrix;
  }

  /**This method perfoms a cholesky decomposition on the covariance matrix. 
   It returns the lower diagonal.
   * 
   * @return Lower diagonal from the cholesky decomposition of the covariance matrix.
   */
  
  public double[][] choleskyDecomp() {
    double [][] corMatrix = 
        new double[covarianceMatrix.length][covarianceMatrix.length];
    double extra = 0.00001; //used to stop NaN and Infinity appearing.
    int n = portfolioSize;
    for (int i = 0; i < n; i++)  {
      for (int j = 0; j <= i; j++) {
        double sum = 0.0;
        for (int k = 0; k < j; k++) {
          sum += corMatrix[i][k] * corMatrix[j][k];
        }
        if (i == j) {
          //formulae for diagonals.
          corMatrix[i][i] = Math.sqrt(covarianceMatrix[i][i] + extra - sum);
        } else {
          //formulae for all other matrix values..
          corMatrix[i][j] = 1.0 / corMatrix[j][j] * (covarianceMatrix[i][j] - sum);
        }
      } 
    }
    return corMatrix;
  }

  /**This method calculates possible returns of the portfolio after the  time horizon.
   * @return returns 1000 possible paths.
   */
  
  private ArrayList<Double> pathsCalculate() {
    ArrayList<Double> potentialResults = new ArrayList<Double>();
    for (int i = 0; i < 1000; i++) {
      double[][] path = getRandPath(); //get a new path matrix
      double[][] weights = getWeights(); //get portfolio weights
      double[][] varMatrix = matrixMult(path, weights); //multiply paths and weights
      double potentialVaR = varMatrix[0][0]; //the 1X1 matrix value.
      potentialResults.add(potentialVaR); //show loss as positive.
    }
    return potentialResults;
  }
  
  /**This calculates the weighting of each stock in the portfolio.
   * 
   * @return array of weights of the stocks.
   */
  
  private double[][] getWeights() {
    double[][] weights = new double[portfolioSize][1];
    for (int i = 0; i < portfolioSize; i++) {
      //weight = total stock value/total portfolio value
      weights[i][0] = (portfolio.getContents().get(i).getHistValues().get(0) 
         * portfolio.getContents().get(i).getShares()) / portfolio.getTotalValue();      
    }
    return weights;
  }
  
  /** This calcuates a random path using the multivariate normal method.
   * 
   * @return - returns the differences between the stocks values and the path end points.
   */
  
  private double[][] getRandPath() {
    double timeInterval;
    if (timeHorizon <= 22) { //determines number of time intervals to use
      timeInterval = 0.25;
    } else {
      timeInterval = 1;
    }
    double[][] cumulativePath = new double[1][portfolioSize]; //path matrix set up
    for (int i = 0; i < portfolioSize; i++) {
      cumulativePath[0][i] = portfolio.getContents().get(i).getValue();
    }
    for (int i = 0; i < timeHorizon / timeInterval; i++) {
      for (int j = 0; j < portfolioSize; j++) {
        double[][] randomMatrix = randomMatrix();  
        double[][] corRandMatrix = matrixMult(randomMatrix, correlationMatrix);  
        cumulativePath[0][j] *= 1 + (averageReurns(portfolio.getContents().get(j)) 
            + corRandMatrix[0][j]); //random path instrument
      }
    }
    double[][] pathDifference = new double [1][portfolioSize];
    for (int i = 0; i < portfolioSize; i++) {
      pathDifference[0][i] = ((cumulativePath[0][i] - portfolio.getContents().get(i).getValue())
        / portfolio.getContents().get(i).getValue()); //work out the change in price 
      //between original stocks and the new price.
    }
    return pathDifference;
  }

  /**This calculates the average return for a stock.
   * 
   * @param stock - the stock being used.
   * @return average return percentage of the stock.
   */
  
  private double averageReurns(Stock stock) {
    double returnSum = 0; //this is the average returns of an individual stock.
    for (int i = 0; i < stock.getReturnHistory().size(); i++) {
      returnSum += stock.getReturnHistory().get(i);
    }
    return returnSum / stock.getReturnHistory().size();
  }
  
  /**This method gets a random number from the gaussian distribution.
   * 
   * @return Returns vector of random numbers.
   */
  
  private double[][] randomMatrix() {
    double[][] randMatrix = new double[1][portfolioSize];
    Random random = new Random();
    for (int i = 0; i < portfolioSize; i++) {
      randMatrix[0][i] = random.nextGaussian(); //fill matrix with new random numbers
    }
    return randMatrix;
  }

  /**This method takes all of the sorted paths and returns the one at the correct percentile point.
   * @return - returns the value at the percentile point based on the confidence.
   */
  
  private double percentilePaths(ArrayList<Double> potentialResults) {
    return potentialResults.get((int) (1000 - (10 * confidence))); //find path at percentile point.
  }
  
  /**This method calculates the conditional value at risk of a portfolio.
   * 
   * @return value at risk to be returned.
   */
  
  public double condVaR() {
    ArrayList<Double> cutHistory = new ArrayList<Double>();
    double stockSize = paths.size();
    for (int j = 0; j < stockSize * (1 - (confidence / 100)); j++) {
      cutHistory.add(paths.get(j)); //only add values from the data that is out of range.
    }
    double averagePfReturn = averageCalculator(cutHistory); //average of new historical data.
    return -1 * averagePfReturn;
  }

  /** This method calcuates the average of a list that is passed to it.
   * 
   * @param list - this is the list to be analysed.
   * @return the return value will be the average of the list passed as a parameter.
   */
  
  private double averageCalculator(ArrayList<Double> list) {
    int listSize = list.size(); //the average of this list will be calculated.
    double runningTotal = 0;
    for (int i = 0; i < listSize; i++) {
      runningTotal += list.get(i); //add all together.
    }
    return (runningTotal / listSize); //divide by size of list.
  }   
  
}
