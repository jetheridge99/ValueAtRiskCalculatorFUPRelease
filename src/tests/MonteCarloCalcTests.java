package tests;

import static org.junit.Assert.assertTrue;

import backEnd.HistoricalSimulationCalc;
import backEnd.ModelBuildingSimulationCalc;
import backEnd.MonteCarloCalc;
import backEnd.Portfolio;
import org.junit.Before;
import org.junit.Test;

public class MonteCarloCalcTests {
  public Portfolio p;
  
  /**This initialises the tests so that they run more efficiently.
   * 
   */
  @Before
  public void initialize() {
    p = new Portfolio();
    p.portfolioReader("testPortfolio.csv");
    //create portfolio first to reduce time taken between tests.
  }
  
  @Test
  public void test1Basic() { //Output is produced by the calculate function
    MonteCarloCalc mcCalc1 = new MonteCarloCalc(p, 95, 5);
    System.out.println(mcCalc1.calculate());
    assertTrue(mcCalc1.calculate() < 0.1);
    //if the double returns for this portfolio and it is small enough to be realistic, it works.
  }
  
  @Test
  public void test2Similarity() { //Test output is similar to other value at risk methods for 1-day
    MonteCarloCalc mcCalc1 = new MonteCarloCalc(p, 95, 1);
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(p, 95, 1);
    HistoricalSimulationCalc hsCalc1 = new HistoricalSimulationCalc(p, 95, 1);
    System.out.println("Monte Carlo VaR: " + mcCalc1.calculate());
    System.out.println("Model Building VaR: " + mbCalc1.calculate());
    System.out.println("Historical Simulation VaR: " + hsCalc1.calculate());
    double difference = mbCalc1.calculate() - mcCalc1.calculate();
    assertTrue("Output is too different.", difference <= 0.05 && difference >= -0.05);
  }
  
  @Test
  public void test3LongerSimilarity() { 
    //Test output is similar to other value at risk methods for 5-days
    MonteCarloCalc mcCalc1 = new MonteCarloCalc(p, 95, 5);
    ModelBuildingSimulationCalc mbCalc1 = new ModelBuildingSimulationCalc(p, 95, 5);
    HistoricalSimulationCalc hsCalc1 = new HistoricalSimulationCalc(p, 95, 5);
    System.out.println("Monte Carlo VaR: " + mcCalc1.calculate());
    System.out.println("Model Building VaR: " + mbCalc1.calculate());
    System.out.println("Historical Simulation VaR: " + hsCalc1.calculate());
    double difference = mbCalc1.calculate() - mcCalc1.calculate();
    assertTrue(difference <= 0.1 && difference >= -0.1);
  }
  
  @Test
  public void test4Cholesky() { //test cholesky decomposition produces a lower triangular matrix
    double[][] matrix = new double[3][3];
    matrix[0][0] = 1;
    matrix[0][1] = 2;
    matrix[0][2] = 4;
    matrix[1][0] = 2;
    matrix[1][1] = 1;
    matrix[1][2] = 3;
    matrix[2][0] = 4;
    matrix[2][1] = 3;
    matrix[2][2] = 1;
    MonteCarloCalc mcCalc1 = new MonteCarloCalc(p, 95, 5);
    double[][] newMatrix = mcCalc1.choleskyDecomp();
    assertTrue("Cholesky doesnt work",
            newMatrix[0][1] == 0 && newMatrix[0][2] == 0 && newMatrix[1][2] == 0);

  }
  
  @Test
  public void test5ConditionalVaR() {
    //Test that each VaR has a larger CVaR.
    MonteCarloCalc mcCalc1 = new MonteCarloCalc(p, 95, 1);
    System.out.println(mcCalc1.calculate() + " is var");
    System.out.println(mcCalc1.condVaR() + " is cvar");
    assertTrue("CVAR is bigger than VAR", mcCalc1.calculate() < mcCalc1.condVaR());
  }
}
