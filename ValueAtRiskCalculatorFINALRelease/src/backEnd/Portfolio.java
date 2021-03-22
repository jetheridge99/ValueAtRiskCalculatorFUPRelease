/**This class creates a portfolio. A portfolio holds an array of stock objects.
 * @author James Etheridge
 */
package backEnd;

import java.util.ArrayList;
import java.io.*;
import java.util.*;

import org.apache.commons.io.IOExceptionWithCause;

import frontEnd.ErrorView;

public class Portfolio {
  private double totalValue;
  private ArrayList<Stock> contents;
  
  /** This is the main constructor. It creates an empty portfolio.
   * 
   */
  
  public Portfolio() {

    this.totalValue = 0;
    this.contents = new ArrayList<Stock>();
  }

  /**This method constructs a portfolio of stocks. This is used for testing as the main software
   takes the file name from a call to the reader.
   * 
   * @param file - takes the file to be read.
   */
  
  public Portfolio(File file) {
    this.totalValue = 0;
    this.contents = new ArrayList<Stock>();
    portfolioReader(""); //does not need file name here.
  }
  
  public ArrayList<Stock> getContents() {
    return this.contents;
  }

  public double getTotalValue() {
    return this.totalValue;
  }

  public void setContents(ArrayList<Stock> contents) {
    this.contents = contents;
  }

  public void setTotalValue(double totalValue) {
    this.totalValue = totalValue;
  }

  /** Similar to ArrayList<>.add, this method just adds a stock to the list of stocks.
   * 
   * @param s - this is the stock, passed from the csv after being created.
   */
  
  public void add(Stock s) {
    this.contents.add(s);
  }
  
  /**PortfolioReader reads a file from a csv and creates a portfolio based on it.
   * The format of the CSV file should be 'ticker,number of shares' for each line.s
   * @param fileName this is the name of the file that will be read.
   */
  
  public void portfolioReader(String fileName) {
    Scanner sc = null;
    try {
      File file = new File(fileName);
      sc = new Scanner(file); //find the file in the system.
    } catch (FileNotFoundException e) {
      System.out.println("File not found!");
      ErrorView.run("This file could not be found. Please select a CSV file from the system.");
    }
    while (sc.hasNextLine()) {
      try {
        String[] ticker = sc.nextLine().split(","); //read each line of file.
        this.contents.add(new Stock(ticker[0], ticker[1])); //create a new stock.
      } catch (IndexOutOfBoundsException e) {
        ErrorView.run("File not formatted correctly. Please ensure file is "
            + "of the form 'ticker,number of shares' "
            + "and that each stock is on a new line");
      }
    }
    for (int i = 0; i < contents.size(); i++) { //work out the total value of the portfolio.
      totalValue += contents.get(i).getHistValues().get(0) * contents.get(i).shares;
    }
  }

  /**This gets the most recent historical value of a stock to take as the current value.
   It then adds all of the values together to get the total portfolio value for 1 of each.
   * @return - returns the value of 1 of each stock as a cumulative total
   */
  
  public Double getSingleValue() {
    double total = 0;
    for (int i = 0; i < contents.size(); i++) {
      total += contents.get(i).getHistValues().get(0); 
      //the total is the most recent history values of each stock.
    }
    return total;
  }
}
