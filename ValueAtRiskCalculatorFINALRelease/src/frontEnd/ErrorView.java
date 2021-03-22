package frontEnd;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ErrorView {
  private static JFrame frame;
  private static JPanel panel;
  private static JLabel text;
  private static JButton okButton;
  
  /** This method is where the error box is created.
   * 
   * @param error The software calls this method giving it the specific error that has occured.
   */
  
  public static void run(String error) {
    frame = new JFrame("ERROR");
    frame.setSize(500, 250);
    
    //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    panel = new JPanel();
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb);
    panel.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    GridBagLayout gbl = new GridBagLayout();
    panel.setLayout(gbl);
    GridBagConstraints gbc = new GridBagConstraints();  
    
    text = new JLabel(error);
    gbc.gridx = 1;
    gbc.gridy = 1;
    panel.add(text, gbc);
    
    okButton = new JButton("OK");
    gbc.gridy = 2;
    panel.add(okButton, gbc);
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);  
        VaRCalculatorView.run(); //restart calculator
      }
    });
    
    
    frame.add(panel);
    frame.setVisible(true);
    

  }
}
