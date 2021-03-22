/**This class calculates the value at risk of a portfolio using the variance/covariance method.
 * @author james etheridge
 */

package backEnd;

import java.util.ArrayList;
import org.apache.commons.math3.special.Erf;

public class ModelBuildingSimulationCalc implements Calculator {
  private Portfolio portfolio;
  private double confidence;
  private int timeHorizon;

  /** This is the constructor of the model building class. 
   * 
   * @param portfolio - this is the portfolio given by the user.
   * @param confidence - this is the confidence interval chosen by the user.
   * @param timeHorizon - this is the time horizon chosen by the user.
   */
  
  public ModelBuildingSimulationCalc(Portfolio portfolio, double confidence, int timeHorizon) {
    this.portfolio = portfolio;
    this.confidence = confidence;
    this.timeHorizon = timeHorizon;
    presize(); //Need to immediately resize the portfolio to set up matrices.
  }

  /**This is the main method which performs the hands out work to the
    other methods to calculate the VaR.
   * 
   * @return - the value returned is the estimated value at risk.
    It is a double in the range of 0.0 to 1.0 where 1.0 is 100%.
   */
  
  public double calculate() {

    double mu = averageReturns(portfolio);
    //this is the average returns over the whole portfolio.
    double theta = Math.sqrt(matrixVariance(portfolio));
    //the standard deviation is the square root of the matrix variance.
    double zscore = zcalc(confidence);
    double mbVaR = ((zscore * theta) - mu) * Math.sqrt(timeHorizon);
    //This is the equation for the value at risk using model building.
    return mbVaR;
  }
  
  /**This method is important because it cuts down portfolio items so that they are a uniform size
   with regards to historical data.
   * 
   */
  
  private void presize() {
    int small = 100000; //no portfolio will have data larger than this.
    
    double pfSize = portfolio.getContents().size();
    //first we find the smallest sized stock.
    for (int i = 0; i < pfSize; i++) {
      if (portfolio.getContents().get(i).getHistValues().size() <= small) { 
        small = portfolio.getContents().get(i).getHistValues().size();
      }
    }

    //next we cut the historical values for that stock.
    for (int i = 0; i < pfSize; i++) {
      int hvSize = portfolio.getContents().get(i).getHistValues().size();
      for (int j = hvSize - 1; j > small - 1; j--) {
        portfolio.getContents().get(i).getHistValues().remove(j);
      }
    }

    //then we cut the return history too.
    for (int i = 0; i < pfSize; i++) {
      int rhSize = portfolio.getContents().get(i).getReturnHistory().size();
      for (int j = rhSize - 1; j > small - 2; j--) {
        portfolio.getContents().get(i).getReturnHistory().remove(j);
      }
    }
  }

  /** This method calculates the matrix variance. This is vital to be able to calculate the
   the standard deviation of the portfolio. It is a refined version of the proof of concept version.
   * 
   * @param pf - this is the portfolio passed through in its current state, 
   no changes will be made to it.
   * @return the returned value is the variance of the portfolio, this is a way of measuring
   the spread of data.
   */
  
  public double matrixVariance(Portfolio pf) {
    int stockHistSize = pf.getContents().get(0).getReturnHistory().size();
    int portfolioSize = pf.getContents().size();
    //we load in starting values, transpose and multiply them together
    double[][] stockMatrix = loadMatrix(stockHistSize, portfolioSize);
    double[][] transposeStockMatrix = transpose(stockMatrix);
    double[][] covarianceMatrix = matrixMult(transposeStockMatrix, stockMatrix);
    //The variance is found with help from the weights of the portfolio.
    double[][] weights = new double[portfolioSize][1];
    double[][] tweights = new double[1][portfolioSize];
    for (int i = 0; i < portfolioSize; i++) {
      weights[i][0] = pf.getContents().get(i).getHistValues().get(0) / pf.getSingleValue();
      tweights[0][i] = pf.getContents().get(i).getHistValues().get(0) / pf.getSingleValue();
      
    }
    //now we can do a double multiplication to get a 1x1 matrix which is our final variance.
    return matrixMult(tweights, matrixMult(covarianceMatrix, weights))[0][0];
    //This return value is the matrix variance.
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
      for (int j = 0; j < stockHistSize; j++) { //calculate the average returns
        avgHistValues += curStock.getReturnHistory().get(j);
      }
      avgHistValues = avgHistValues / (curStock.getReturnHistory().size());
      for (int k = 0; k < stockHistSize; k++) { 
        //subtract the average returns from each individual return
        stockMatrix[k][i] = curStock.getReturnHistory().get(k) - avgHistValues;
        stockMatrix[k][i] /= 100; //going from percent to decimal percent for compatibility
      }
    }
    return stockMatrix;
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
      for (int y = 0; y < m; y++) { //switch each value with its opposite based on the diagonal.
        transposedMatrix[x][y] = stockMatrix[y][x];
      }
    }
    return transposedMatrix;
  }
  
  /**This method calculates the product of two matrices, in the order m1 * m2.
   * Order is very important in matrix multiplication.
   * @param m1 this is the first matrix in the multiplication.
   * @param m2 this is the second matrix in the multiplication.
   * @return this is a matrix that is the product of m1*m2.
   */
  
  public double[][] matrixMult(double[][] m1, double[][] m2) {
    double[][] result = new double[m1.length][m2[0].length];
    for (int i = 0; i < m1.length; i++) {
      for (int j = 0; j < m2[0].length; j++) {
        for (int k = 0; k < m1[0].length; k++) {
          result[i][j] += m1[i][k] * m2[k][j]; //add the multiplications for each point.
        }
      }
    }
    return result;
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
  
  /** This method works out the average over a whole portfolio.
   * 
   * @param pf - this is the portfolio passed through. The method will not make any changes to it.
   * @return the returned value is the average over the whole portfolio.
   */
  
  public double averageReturns(Portfolio pf) {
    int psize = pf.getContents().size(); //This is the average percentage returns of the portfolio.
    ArrayList<Double> list = new ArrayList<Double>();
    for (int i = 0; i < psize; i++) {
      list.add(averageCalculator(pf.getContents().get(i).getReturnHistory()));
    }
    return averageCalculator(list);
  }
  
  /** This method calcualtes the Z score using the normal distribution curve.
   * 
   * @param confidence - this is the confidence interval of the portfolio.
   * @return the value returned will give us the Z score.
   */
  
  public static double zcalc(double confidence) {
    double z = Math.sqrt(2) * Erf.erfcInv(2 * (1 - (confidence / 100)));
    //Use the erfc function to find the Z score of the normal distribution.
    return  z;
  }
  
  /** This is a getter to be used for testing.
   * 
   * @return the class portfolio is returned.
   */
  
  public Portfolio getPortfolio() {
    return this.portfolio;
  }

  /**This method calculates the conditional VaR for the model building method.
   * 
   * @return returns the expected shortfall.
   */
  
  public double condVaR() {
    double mu = averageReturns(portfolio);
    //this is the average returns over the whole portfolio.
    double theta = Math.sqrt(matrixVariance(portfolio));
    //the standard deviation is the square root of the matrix variance.
    double zscore = zcalc(confidence);
    //need to recalculate Z to get new value for the shortfall.
    double newZ = recalcZ(zscore);
    double mbCVaR = (mu + newZ * theta) * Math.sqrt(timeHorizon);
    //This is the equation for the value at risk using model building.
    return mbCVaR;
  }

  private double recalcZ(double zscore) {
    //this is the formula to calculate the new Z score
    return ((1 / (1 - (confidence) / 100)) 
            *
            (1 / Math.sqrt(2 * Math.PI)) 
            *
            (Math.exp(-0.5 * Math.pow(zscore, 2))));
  }
  
}
