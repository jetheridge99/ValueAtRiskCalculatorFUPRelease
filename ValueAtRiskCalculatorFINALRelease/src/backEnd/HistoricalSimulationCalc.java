/**This class calculates VaR using historical simulation.
 * @author James Etheridge
 */

package backEnd;

import backEnd.Stock;
import java.util.ArrayList;
import java.util.Collections;

public class HistoricalSimulationCalc implements Calculator {
  private Portfolio pf;
  private double confidence;
  private int timeHorizon;
  
  
  /** This constructor creates a Historical Simulation Calculator. It calculates the value at risk
   of a portfolio over a time horizon with a level of confidence.
   * 
   * @param p this is the portfolio that gets passed in from a source.
   * @param confidence this is the confidence interval that is used to calculate VaR
   * @param timeHorizon this is the number of days we are calculating for.
   */
  
  public HistoricalSimulationCalc(Portfolio p, double confidence, int timeHorizon) {
    this.pf = p;
    this.confidence = confidence;
    this.timeHorizon = timeHorizon;
  }
  
  /**This method is part of the calculator interface. It returns the Value at Risk for the
   historical simulation method.
   */
  //Do not need to specify return as it is specified in calculator interface javadoc.
  
  public double calculate() {

    double hsVaR = -1 * (percentile()) * Math.sqrt(timeHorizon); 
    //negate the results and multiply the square root of the number of days.
    return hsVaR;
    
  }
  
  /** This method finds the value at the percentile point for the given confidence.
   * 
   * @return the returned value is the 1 day value at risk as a measure of profit or loss. 
   We negate it because we are measuring loss so the amount of loss should be a positive number.
   If the returned value is already negative, then there is no value at risk
    according to calculations.
   */
  
  private double percentile() {
    double invP = 100 - (int) confidence; //we want the VaR from the losses rather than the profits.
    double indivReturns;
    double average = 0;
    Stock current;
    int returnHistorySize = 0;
    ArrayList<Double> curReturns;
    int pfSize = pf.getContents().size(); //Refactored to bring efficiency,
    //no need to call size() with each iteration of the loop.
    for (int i = 0; i < pfSize; i++) { //this method works on single stocks so does 1 at a time.
      current = pf.getContents().get(i);
      curReturns = current.getReturnHistory();
      returnHistorySize = curReturns.size();
      ArrayList<Double> sortedReturns = sort(curReturns);
      indivReturns = sortedReturns.get(toInt((invP / 100.0) * (returnHistorySize)));
      //System.out.println(sortedReturns);// Debugging line
      average += indivReturns;
    }
    //average value at risk over whole portfolio.
    return (average / pfSize);
   
  }
  
  /**This method sorts an ArrayList into ascending order.
   * It is used to find percentile points.
   * @param list - an unsorted list is given ready to be sorted 
   * @return a sorted list is returned in ascending order.
   */
  
  public static ArrayList<Double> sort(ArrayList<Double> list) {
    Collections.sort(list); //Sorts list 
    return list;
  }
  
  /** This casts a double to an integer. This is used because we can only search for an integer
   position in an array not a double.
   * 
   * @param a this is the double passed through.
   * @return an integer is returned.
   */
  
  public static int toInt(double a) {
    return (int) a;
  }
  
  /**This calculates the Conditional Value at Risk from the historical simulation.
   * 
   * @return returns the CVaR value.
   */

  public double condVaR() {
    double cumulativeCVaR = 0; //Cumulate each CVaR for this method.
    double pfSize = pf.getContents().size(); //refactored for efficiency.
    for (int i = 0; i < pfSize; i++) {
      ArrayList<Double> sortedReturnHistory = pf.getContents().get(i).getReturnHistory();
      sortedReturnHistory = sort(sortedReturnHistory);
      double stockSize = sortedReturnHistory.size();
      ArrayList<Double> cutHistory = new ArrayList<Double>();
      for (int j = 0; j < stockSize * (1 - (confidence / 100)); j++) {
    	//only add values between the highest risk and the value at risk point.
        cutHistory.add(sortedReturnHistory.get(j));
      }
      cumulativeCVaR += averageCalculator(cutHistory); //CVaR of stock
    }
    return (-1 * (cumulativeCVaR / pfSize) * Math.sqrt(timeHorizon));
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
