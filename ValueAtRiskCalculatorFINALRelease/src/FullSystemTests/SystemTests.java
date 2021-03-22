package FullSystemTests;

import static org.junit.Assert.assertTrue;

import backEnd.Portfolio;
import org.junit.Before;
import org.junit.Test;


public class SystemTests {


  @Test
  public void test1() {
    Portfolio p5 = new Portfolio();
    p5.portfolioReader("testPortfolios/tp5.csv");
    assertTrue(true);
  }
  
  @Test
  public void test2() {
    Portfolio p10 = new Portfolio();
    p10.portfolioReader("testPortfolios/tp10.csv");
    assertTrue(true);
  }
  
  @Test
  public void test3() {
    Portfolio p15 = new Portfolio();
    p15.portfolioReader("testPortfolios/tp15.csv");
    assertTrue(true);
  }
  
  @Test
  public void test4() {
    Portfolio p5 = new Portfolio();
    p5.portfolioReader("testPortfolios/tp5.csv");
  
    int s1Size = p5.getContents().get(1).getHistValues().size();
    int s2Size = p5.getContents().get(2).getHistValues().size();
    int s4Size = p5.getContents().get(4).getHistValues().size();
  
    assertTrue(s1Size > 500 && s2Size > 500 && s4Size > 500);
  }
}
