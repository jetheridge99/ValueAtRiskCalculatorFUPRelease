package backEnd;

import frontEnd.ErrorView;
import frontEnd.VaRCalculatorView;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ValueAtRiskController extends VaRCalculatorView {
  private static VaRCalculatorView view;
  private static Portfolio portfolio;
  
  /** This is the main method of the entire application. 
   It starts a main UI and the rest happens from there.
   * 
   * @param args no arguments are required to run this.
   */
  
  @SuppressWarnings("static-access")
  public static void main(String[] args)  {  
    view = new VaRCalculatorView(); //create a new view
    view.run(); //run the view
    portfolio = new Portfolio(); //create a new portfolio that is empty.
  }
  
  /** This method controls the calculation of the value at risk
    and feeds the results back to the UI.
   * 
   */
  
  @SuppressWarnings("static-access")
public static void calculateVaR() {
    //read values from text boxes.
    String timeHorizon = VaRCalculatorView.getTimeHorizon();
    String confidence = VaRCalculatorView.getConfidence();
    portfolio.portfolioReader(VaRCalculatorView.getFileText());
    int itimehorizon = Integer.parseInt(timeHorizon);
    double dconfidence = Double.parseDouble(confidence);
    if (dconfidence > 100) { //error checking
      ErrorView.run("Please enter a confidence interval less than 100");
    } else if (dconfidence < 0) {
      ErrorView.run("Please enter a confidence interval between 0 and 100");
    }
    //set up calculators
    HistoricalSimulationCalc hsCalc 
        = new HistoricalSimulationCalc(portfolio, dconfidence, itimehorizon);
    ModelBuildingSimulationCalc mbCalc 
        = new ModelBuildingSimulationCalc(portfolio, dconfidence, itimehorizon);
    MonteCarloCalc mcCalc = new MonteCarloCalc(portfolio, dconfidence, itimehorizon);
    NumberFormat format2 = new DecimalFormat("#0.0000"); //format results
    double mbPaR = mbCalc.calculate();
    double hsPaR = hsCalc.calculate();
    double mcPaR = mcCalc.calculate(); 
    //Fill all textboxes with calculated results.
    VaRCalculatorView.setHsPercentText(format2.format(hsPaR * 100));
    VaRCalculatorView.setMbPercentText(format2.format(mbPaR * 100));
    VaRCalculatorView.setMcPercentText(format2.format(mcPaR * 100));
    VaRCalculatorView.setHScparText(format2.format(hsCalc.condVaR() * 100));
    VaRCalculatorView.setMBcparText(format2.format(mbCalc.condVaR() * 100));
    VaRCalculatorView.setMCcparText(format2.format(mcCalc.condVaR() * 100));
    view.hideLoading();
    NumberFormat format1 = new DecimalFormat("#0.00"); //format results
    VaRCalculatorView.setHsValueText(format1.format(hsPaR * portfolio.getTotalValue()));
    VaRCalculatorView.setMbValueText(format1.format(mbPaR * portfolio.getTotalValue()));
    VaRCalculatorView.setMcValueText(format1.format(mcPaR * portfolio.getTotalValue()));
    VaRCalculatorView.setHScvarText(format1.format(hsCalc.condVaR() * portfolio.getTotalValue()));
    VaRCalculatorView.setMBcvarText(format1.format(mbCalc.condVaR() * portfolio.getTotalValue()));
    VaRCalculatorView.setMCcvarText(format1.format(mcCalc.condVaR() * portfolio.getTotalValue()));
    view.varchartButton.setEnabled(true); //enable use of chart views.
    view.cvarChartButton.setEnabled(true);
  }


}
