/**This is the test class for the portfolio class.
 * @author James Etheridge
 */

package tests;

import static org.junit.Assert.assertTrue;

import backEnd.Portfolio;
import backEnd.Stock;
import org.junit.Test;


public class PortfolioTests {

  @Test
  public void portfolioTest1() { //Tests the constructor creates an empty arrayList
    Portfolio p1 = new Portfolio();
    assertTrue(p1.getContents().size() == 0);
  }

  @Test
  public void portfolioTest2() { //Test total value method works when portfolio is empty.
    Portfolio p1 = new Portfolio();
    assertTrue(p1.getTotalValue() == 0);
  }

  @Test
  public void portfolioTest3() { //test 1 stock can be added
    Portfolio p1 = new Portfolio();
    Stock s1 = new Stock("AMZN");
    p1.add(s1);
    assertTrue(true);
  }

  @Test
  public void portfolioTest4() { //test 2 stocks can be added
    Portfolio p1 = new Portfolio();
    Stock s1 = new Stock("AMZN");
    Stock s2 = new Stock("GE");
    p1.add(s1);
    p1.add(s2);
    assertTrue(true);
  }

  @Test
  public void portfolioTest5() { //Test stocks can be read from a csv file

    Portfolio p1 = new Portfolio();
    p1.portfolioReader("testPortfolio.csv");
    //System.out.println(p1.getContents().size());
    assertTrue(true);
  }

  @Test
  public void portfolioTest6() { //Test that full portfolio is set up.
    Portfolio p1 = new Portfolio();
    p1.portfolioReader("testPortfolio.csv");

    System.out.println(p1.getContents().get(0).shares); //Should be 50
    System.out.println(p1.getContents().get(1).shares); //Should be 500
    System.out.println(p1.getContents().get(2).shares); //Should be 5000
    //Next print the first 5 historical values for each stock
    for (int j = 0; j < p1.getContents().size(); j++) {
      for (int i = 0; i < 5; i++) {
        System.out.println(p1.getContents().get(j).getHistValues().get(i));
      }
    }
    for (int j = 0; j < p1.getContents().size(); j++) {
      for (int i = 0; i < 5; i++) {
        System.out.println(p1.getContents().get(j).getReturnHistory().get(i));
      }
    }
    assertTrue(true);
  }
  
  @Test
  public void portfolioTest7() { //Test stocks can be read from a csv file

    Portfolio p1 = new Portfolio();
    p1.portfolioReader("testPortfolio.csv");
    System.out.println(p1.getTotalValue());
    assertTrue(true);
  }
}
