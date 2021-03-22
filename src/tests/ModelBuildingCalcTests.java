package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import backEnd.HistoricalSimulationCalc;
import backEnd.ModelBuildingSimulationCalc;
import backEnd.Portfolio;


public class ModelBuildingCalcTests {

  Portfolio pf;
  
  /**This initialises the tests so they run more efficiently.
   * 
   */
  
  @Before
  public void initialize() {
    pf = new Portfolio();
    pf.portfolioReader("testPortfolio.csv");
    //create portfolio first to reduce time taken between tests.
  }
  
  @Test
  public void test1History() { //Test that stock history is cut down accordingly

    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(pf, 90, 1);
    System.out.println(mbCalc1.getPortfolio().getContents().get(0).getHistValues().size());
    System.out.println(mbCalc1.getPortfolio().getContents().get(1).getHistValues().size());
    System.out.println(mbCalc1.getPortfolio().getContents().get(2).getHistValues().size());
    System.out.println();
    System.out.println(mbCalc1.getPortfolio().getContents().get(0).getReturnHistory().size());
    System.out.println(mbCalc1.getPortfolio().getContents().get(1).getReturnHistory().size());
    System.out.println(mbCalc1.getPortfolio().getContents().get(2).getReturnHistory().size());
    
    
    assertTrue("Stock not cut down accordingly",
        mbCalc1.getPortfolio().getContents().get(0).getHistValues().size()
        == mbCalc1.getPortfolio().getContents().get(1).getHistValues().size() 
        && mbCalc1.getPortfolio().getContents().get(1).getHistValues().size()
        == mbCalc1.getPortfolio().getContents().get(2).getHistValues().size() 
        && mbCalc1.getPortfolio().getContents().get(0).getReturnHistory().size()
        == mbCalc1.getPortfolio().getContents().get(1).getReturnHistory().size() 
        && mbCalc1.getPortfolio().getContents().get(1).getReturnHistory().size()
        == mbCalc1.getPortfolio().getContents().get(2).getReturnHistory().size());
  }
  
  @Test
  public void test2Answer() { //Test that a number is produced.
   
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(pf, 95, 1);
    System.out.println("VaR is " + mbCalc1.calculate());
    assertTrue("calculation doesnt produce a number", mbCalc1.calculate() < 0.05);
  }
  
  @Test
  public void test3Similarity() { //Check that similar results are produced
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(pf, 95, 1);
    double mbVaR1 = mbCalc1.calculate();
    System.out.println(" mbVaR is " + mbVaR1);
    
    HistoricalSimulationCalc hsCalc1 = new HistoricalSimulationCalc(pf, 95, 1);
    double hsVaR1 = hsCalc1.calculate();
    System.out.println("hsVaR is " + hsVaR1);
    double difference = mbVaR1 - hsVaR1;
    if (difference < 0) {
      difference = difference * -1;
    }
    assertTrue("Answer is too far from HS answer", difference < 0.05);

  }
  
  @Test
  public void test4MatMult() { //test Matrix multiplication method works.
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(pf, 95, 1);
    double[][] m1 = {{2, 2}, {2, 2}};
    double[][] m2 = {{6, 6}, {6, 6}};
    double[][] result = mbCalc1.matrixMult(m1, m2);
  }
  
  @Test
  public void test5CVaR() { //Test that CVaR is larger than VaR.
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(pf, 90, 1);
    System.out.println(mbCalc1.calculate() + " is var");
    System.out.println(mbCalc1.condVaR() + " is cvar");
    assertTrue("CVAR is smaller than VAR", mbCalc1.calculate() < mbCalc1.condVaR());
  }

}
