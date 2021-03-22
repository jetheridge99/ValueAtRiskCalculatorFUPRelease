/**This interface will be used to primarily set up the 3 VaR calculators.
 * @author James Etheridge
 */

package backEnd;

public interface Calculator {
  /**All calculators should have a calculate function that returns the value at risk.
   * 
   * @return - this is the value at risk, calculated and in the form of a double, where 100% is 1.0
   */
  public double calculate();
}
