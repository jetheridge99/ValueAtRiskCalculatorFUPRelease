package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import backEnd.HistoricalSimulationCalc;
import backEnd.Portfolio;


public class HistoricalSimCalcTests {

  @Test
  public void hsTestBasic() {
    Portfolio p = new Portfolio();
    p.portfolioReader("testPortfolio.csv");
    HistoricalSimulationCalc hsCalc1 = new HistoricalSimulationCalc(p, 90, 1);
    System.out.println(hsCalc1.calculate());
    HistoricalSimulationCalc hsCalc2 = new HistoricalSimulationCalc(p, 95, 1);
    System.out.println(hsCalc2.calculate());
    HistoricalSimulationCalc hsCalc3 = new HistoricalSimulationCalc(p, 95, 5);
    System.out.println(hsCalc3.calculate());
    //we can assert true if hsCalc3 > hsCalc2 > hsCalc 1 purely because
    //Mathematically the value at risk increases with a higher confidence and
    //a higher time horizon.
    assertTrue("Basic implementation is incorrect if assertion error.",
            hsCalc1.calculate() < hsCalc2.calculate()
        && hsCalc2.calculate() < hsCalc3.calculate());
  }
  
  @Test
  public void hsTestCVaR() {
    Portfolio p = new Portfolio();
    p.portfolioReader("testPortfolio.csv");
    HistoricalSimulationCalc hsCalc1 = new HistoricalSimulationCalc(p, 90, 1);
    System.out.println(hsCalc1.condVaR());
    System.out.println(hsCalc1.calculate());
    System.out.println("-----------");
    assertTrue("test conditional VaR is larger than VaR.", hsCalc1.condVaR() > hsCalc1.calculate());
  }

}
