/**This class is to test the stock class.
 * @author James Etheridge
 */

package tests;

import static org.junit.Assert.assertTrue;

import backEnd.Stock;
import java.util.ArrayList;
import org.junit.Test;

public class StockTests {

  @SuppressWarnings("unused")
  @Test
  public void stockTest1() { //Test the class exists
    Stock s1 = new Stock("AMZN");
    assertTrue(true);
  }

  @SuppressWarnings("unused")
  @Test
  public void stockTest2() { //Test class variables exist using getters.
    Stock s1 = new Stock("AMZN");
    String a = s1.getTicker();
    String b = s1.getName();
    ArrayList<Double> c = s1.getHistValues();
    ArrayList<Double> d = s1.getReturnHistory();
    assertTrue(true);
  }

  @Test
  public void stockTest3() { //Test class variables can be set via setters.
    Stock s1 = new Stock("AMZN");
    s1.setTicker("GOOGL");
    s1.setName("Google");
    s1.setHistValues(new ArrayList<Double>());
    s1.setReturnHistory(new ArrayList<Double>());
  }

  @Test
  public void stockTest4() { //Test a stock object can be created given only a ticker
    //For this I need to write a method to gather information from Alpha Vantage.
    Stock s1 = new Stock("AMZN");
    //to check it is populating, there should be over 5000 values in the historical values
    //because there is over 20 years of data to add.
    //Stock market isnt open 365 days a year but still over 5000 values should be there for
    //this stock.
    System.out.println(s1.getHistValues().size());
    assertTrue(s1.getHistValues().size() > 5000);
  }

  @Test
  public void stockTest5() { //We now need to test that a return history will be generated off the 
    //base of the historical values.
    Stock s1 = new Stock("AMZN");
    System.out.println(s1.getReturnHistory().size());
    //test if size is 1 less than the history size because thats how return history works.
    //there is always 1 less because you cannot do a return for the final value.
    assertTrue(s1.getReturnHistory().size() == s1.getHistValues().size() - 1);
  }

  @Test
  public void stockTest6() { //This tests that the constructor to set a number of shares works.
    Stock s1 = new Stock("AMZN", "50");
    assertTrue(s1.getShares() == 50);
  }
}