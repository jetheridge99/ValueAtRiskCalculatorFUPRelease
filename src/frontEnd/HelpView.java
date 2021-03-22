package frontEnd;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HelpView {
  private static JFrame frame;
  private static JPanel panel;

  
  /** This method is where the help box is created.
   */
  
  public static void run() {
    frame = new JFrame("Help");
    frame.setSize(600, 400);
    panel = new JPanel();
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb);
    panel.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    GridBagLayout gbl = new GridBagLayout();
    panel.setLayout(gbl);
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel text1 = new JLabel();
    text1.setText("Using the interface:");
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(text1, gbc);
    
    JLabel text2 = new JLabel();
    text2.setText("1. Browse the system for a CSV file containing your portfolio.");
    gbc.gridx = 1;
    gbc.gridy = 2;
    panel.add(text2, gbc);
    
    JLabel text3 = new JLabel();
    text3.setText("2. Select a confidence interval (percentage between 1-100%).");
    gbc.gridx = 1;
    gbc.gridy = 3;
    panel.add(text3, gbc);
    
    JLabel text4 = new JLabel();
    text4.setText("3. Select a time horizon."
        + " This can be any number of days,");
    gbc.gridx = 1;
    gbc.gridy = 4;
    panel.add(text4, gbc);
    
    JLabel text41 = new JLabel();
    text41.setText(" but longer time horizons"
        + "will take longer to calculate.");
    gbc.gridx = 1;
    gbc.gridy = 5;
    panel.add(text41, gbc);
    
    
    JLabel text5 = new JLabel();
    text5.setText("4. If you make a mistake, click reset and start again.");
    gbc.gridx = 1;
    gbc.gridy = 6;
    panel.add(text5, gbc);
    
    JLabel text6 = new JLabel();
    text6.setText("5. Otherwise, click calculate and wait for the estimations to appear.");
    gbc.gridx = 1;
    gbc.gridy = 7;
    panel.add(text6, gbc);
    
    JLabel text61 = new JLabel();
    text61.setText("6. Optionally, click one of the chart "
            + "buttons to see a visual representation of results.");
    gbc.gridx = 1;
    gbc.gridy = 8;
    panel.add(text61, gbc);
    
    JLabel text7 = new JLabel();
    text7.setText("\n");
    gbc.gridx = 1;
    gbc.gridy = 9;
    panel.add(text7, gbc);
    
    JLabel text8 = new JLabel();
    text8.setText("What do the results mean?");
    gbc.gridx = 1;
    gbc.gridy = 10;
    panel.add(text8, gbc);
    
    JLabel text9 = new JLabel();
    text9.setText("- The Percentage at Risk is a percentage value of your portfolio that"
        + " is estimated to be at risk.");
    gbc.gridx = 1;
    gbc.gridy = 11;
    panel.add(text9, gbc);
    
    JLabel text10 = new JLabel();
    text10.setText("- The Value at Risk is a monetary value of your portfolio that"
        + " is estimated to be at risk.");
    gbc.gridx = 1;
    gbc.gridy = 12;
    panel.add(text10, gbc);
    
    JLabel text11 = new JLabel();
    text11.setText("- The Conditional Value at Risk is a measure "
            + "for when the approximation falls short.");
    gbc.gridx = 1;
    gbc.gridy = 13;
    panel.add(text11, gbc);
    
    JLabel text12 = new JLabel();
    text12.setText("It is the amount of money at risk if the losses exceed the VaR.");
    gbc.gridx = 1;
    gbc.gridy = 14;
    panel.add(text12, gbc);
    
    JLabel text13 = new JLabel();
    text13.setText("- It is important to note, these are only estimations"
        + " using different models.");
    gbc.gridx = 1;
    gbc.gridy = 15;
    panel.add(text13, gbc);
    
    JLabel text14 = new JLabel();
    text14.setText("You should not take them to be true predictions of the future.");
    gbc.gridx = 1;
    gbc.gridy = 16;
    panel.add(text14, gbc);
    
    frame.add(panel);
    frame.setVisible(true);
    

  }
}
