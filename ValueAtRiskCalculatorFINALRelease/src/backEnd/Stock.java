/** This class creates and manages stock objects.
 * @author James Etheridge
 */

package backEnd;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import com.crazzyghost.alphavantage.timeseries.response.TimeSeriesResponse;
import frontEnd.ErrorView;
import java.util.ArrayList;

public class Stock {

  private String ticker;
  private String name;
  private double value;
  private ArrayList<Double> histValues;
  private ArrayList<Double> returnHistory;
  public double shares;

  /** This is just an empty stock for testing other classes.
  */
  
  public Stock() {
    
  }
  
  /**This is a constructor for a Stock.
  * @param shares - this is the total value of the stock owned, most people buy multiple stocks.
  * @param ticker - ticker is taken as a parameter because it is the unique identifier for the 
  stock on the stock market.
  */
  
  public Stock(String ticker, String shares) {
    this.ticker = ticker;
    this.histValues = new ArrayList<Double>();
    stockPopulate();
    this.returnHistory = returnHistCalculate();
    try {
      this.shares = Double.parseDouble(shares);
    } catch (Exception e) {
      ErrorView.run("Number of shares is not a valid number."
          + " Please enter a valid number of shares owned.");
      
    }
    this.value = histValues.get(histValues.size() - 1); //most recent value taken as value
  }
  
  /**This is a constructor for a Stock.
  * @param ticker - ticker is taken as a parameter because it is the unique identifier for 
  the stock on the stock market.
  */
  
  public Stock(String ticker) {
    this.ticker = ticker;
    this.histValues = new ArrayList<Double>();
    stockPopulate();
    this.returnHistory = returnHistCalculate();
    this.shares = 1; //assume one is owned only
  }

  /** This is a method to calculate the return history based on the Historical values.
  * It does so by comparing the closing prices of two days at at time, and each time it changes
  * the two days to get a full set.
  * 
  * @return returns a list of return values for each pair of continuous days.
  */

  private ArrayList<Double> returnHistCalculate() {
    ArrayList<Double> returnList = new ArrayList<Double>();
    for (int i = 0; i < histValues.size() - 1; i++) {
      double a = histValues.get(i + 1);
      double b = histValues.get(i);
      //System.out.println("Day " + i + " and " + (i-1) + " return is " + ((1-(a/b))));
      //The previous print line is to check the returns are correct
      
      //the return history is the percentage change from one day to the next.
      returnList.add((double) ((1 - (a / b))));
    }
    return returnList;
  }

  /**This method populates the stock class with information.
  * 
  * @author jamesetheridge
  * @author Crazzyghost
  It comunicates with the aplha vantage API using crazzyghost's java wrapper.
   See <a href="https://github.com/crazzyghost/alphavantage-java">https://github.com/crazzyghost/alphavantage-java</a>
  */
  
  private void stockPopulate() {
    try {
      /*The following chunk of code is taken from the crazzyghost's Alpha Vantage Wrapper ReadMe
      *
      */
      Config cfg = Config.builder()
          .key("OWM7EGP30DLPLECO")
          .timeOut(10)
          .build();
      AlphaVantage.api().init(cfg);
    } catch (Exception e) {
      ErrorView.run("Could not connect to Alpha Vantage."
            + " Please check connection. If not working, key is invalid.");
    }
    TimeSeriesResponse call = null;
    try {
      call = AlphaVantage.api().timeSeries().daily().forSymbol(ticker)
          .outputSize(OutputSize.FULL)
          .fetchSync();
    } catch (Exception e) {
      ErrorView.run("Could not find stock on the market."
          + " Please make sure all tickers are valid stock symbols. "
          + " Stock not found is: " + ticker);
    }
    //System.out.println(call.getStockUnits().toString());

    int size = call.getStockUnits().size();

    for (int i = 0; i < size; i++) {
      //we take the close values as the historical data.
      double close = call.getStockUnits().get(i).getClose();
      histValues.add(close);
    }
  }


  public String getTicker() {
    return this.ticker;
  }

  public String getName() {
    return this.name;
  }
  
  public ArrayList<Double> getHistValues() {
    return this.histValues;
  }

  public ArrayList<Double> getReturnHistory() {
    return this.returnHistory;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setHistValues(ArrayList<Double> histValues) {
    this.histValues = histValues;
  }

  public void setReturnHistory(ArrayList<Double> returnHistory) {
    this.returnHistory = returnHistory;
  }

  public double getShares() {
    return this.shares;
  }

  public void setShares(double shares) {
    this.shares = shares;
  }
  
  public double getValue() {
    return this.value;
  }
}
