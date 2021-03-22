package frontEnd;

import backEnd.ValueAtRiskController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class VaRCalculatorView {

  protected static JButton varchartButton;
  protected static JButton cvarChartButton;
  private static JFrame mainFrame;
  private static JFrame loadFrame;
  private static JTextField thText;
  private static JTextField confidenceText;
  private static JTextField hspercentText;
  private static JTextField mbpercentText;
  private static JTextField mcpercentText;
  private static JTextField hsvalueText;
  private static JTextField mbvalueText;
  private static JTextField mcvalueText;
  private static JTextField hscvarText;
  private static JTextField hscparText;
  private static JTextField mbcvarText;
  private static JTextField mbcparText;
  private static JTextField mccvarText;
  private static JTextField mccparText;
  private static JTextField fileText;
  private static JLabel loadLabel;
  private static JPanel mainPanel;
  public static JFileChooser fileChooser;
  
  /**This method loads a constructor of the view. This sets up the loading screen,
   which means it can be opened and closed whenever.
   * 
   */
  
  public VaRCalculatorView() {
    loadFrame = new JFrame("Loading");
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb);
    loadFrame.setSize(200, 100);
    loadFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    loadFrame.add(new JLabel("Loading, please wait... ", JLabel.CENTER));
    loadFrame.getContentPane().setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    loadFrame.setVisible(true);
    loadFrame.setVisible(false);
  }

  /** This method sets up the whole user interface. This includes functions of buttons etc.
   * 
   */
  
  public static void run() {
    mainFrame = new JFrame("Value at Risk Calculator");
    mainFrame.setSize(1080, 540);
    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    mainPanel = new JPanel();
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb);
    mainPanel.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    GridBagLayout lay = new GridBagLayout();
    mainPanel.setLayout(lay);
    GridBagConstraints gbc = new GridBagConstraints();
    
    //Left side
    
    JLabel csvLabel = new JLabel("Portfolio File (.csv):");
    gbc.gridx = 1;
    gbc.gridy = 3;
    mainPanel.add(csvLabel, gbc);
    
    
    fileText = new JTextField(20);
    gbc.gridx = 2;
    gbc.gridy = 3;
    mainPanel.add(fileText, gbc);
    
    JButton browseButton = new JButton("Browse");
    browseButton.setForeground(Color.BLUE);
    mainPanel.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
    browseButton.setOpaque(false);
    browseButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          fileText.setText(browse());
        }
      });
    browseButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            browseButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            browseButton.setForeground(Color.BLUE);
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 4;
    mainPanel.add(browseButton, gbc);
    
    
    
    
    JLabel confidenceLabel = new JLabel("Confidence (0.0-100.0):");
    gbc.gridx = 1;
    gbc.gridy = 6;
    mainPanel.add(confidenceLabel, gbc);
    
    confidenceText = new JTextField(20);
    gbc.gridx = 2;
    gbc.gridy = 6;
    mainPanel.add(confidenceText, gbc);
    
    JLabel thLabel = new JLabel("Time Horizon (whole number):");
    gbc.gridx = 1;
    gbc.gridy = 7;
    mainPanel.add(thLabel, gbc);

    thText = new JTextField(20);
    gbc.gridx = 2;
    gbc.gridy = 7;
    mainPanel.add(thText, gbc);
    
    JButton calcButton = new JButton("Calculate");
    calcButton.setForeground(Color.BLUE);
    calcButton.setOpaque(false);
    calcButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            calcButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            calcButton.setForeground(Color.BLUE);
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 8;
    mainPanel.add(calcButton, gbc);
    calcButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showLoading();
        if (isInteger(thText.getText()) == false) {
          ErrorView.run("The time horizon inputted is not an integer. Please enter an integer.");
          
        } else if (isDouble(confidenceText.getText()) == false) {
          ErrorView.run("The confidence interval inputted is not a double,"
              + " please enter a double between 0 and 100");
        }
        
        ValueAtRiskController.calculateVaR();
      }
    });
    
    JButton resetButton = new JButton("Reset");
    resetButton.setForeground(Color.RED);
    resetButton.setOpaque(false);
    resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            resetButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            resetButton.setForeground(Color.RED);
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 9;
    mainPanel.add(resetButton, gbc);
    resetButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          hideLoading();
          reset();
        }
      });
    
    JButton helpButton = new JButton("Help");
    helpButton.setForeground(Color.BLUE);
    helpButton.setOpaque(false);
    helpButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            helpButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            helpButton.setForeground(Color.BLUE);
        }
    });
    gbc.gridx = 2;
    gbc.gridy = 10;
    mainPanel.add(helpButton, gbc);
    helpButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          help();
        }
      });
    
    ImageIcon loading = new ImageIcon("loader.gif");
    loadLabel = new JLabel("loading... ", loading, JLabel.CENTER);
    mainPanel.add(loadLabel);
    loadLabel.setVisible(false);
    
    //Right side
    JLabel mbLabel = new JLabel("Model Building:");
    gbc.gridx = 4;
    gbc.gridy = 1;
    mainPanel.add(mbLabel, gbc);
    JLabel mbpercentLabel = new JLabel("Percentage at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 2;
    mainPanel.add(mbpercentLabel, gbc);
    JLabel mbvalueLabel = new JLabel("Value at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 3; 
    mainPanel.add(mbvalueLabel, gbc);
    mbpercentText = new JTextField(10);
    mbpercentText.setEditable(false);
    mbpercentText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 2;
    mainPanel.add(mbpercentText, gbc);
    mbvalueText = new JTextField(10);
    mbvalueText.setEditable(false);
    mbvalueText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 3;
    mainPanel.add(mbvalueText, gbc);
    JLabel mbcvarLabel = new JLabel("Conditional Value at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 3;
    mainPanel.add(mbcvarLabel, gbc);
    mbcvarText = new JTextField(10);
    mbcvarText.setEditable(false);
    mbcvarText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 3;
    mainPanel.add(mbcvarText, gbc);
    JLabel mbcparLabel = new JLabel("Conditional Percentage at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 2;
    mainPanel.add(mbcparLabel, gbc);
    mbcparText = new JTextField(10);
    mbcparText.setEditable(false);
    mbcparText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 2;
    mainPanel.add(mbcparText, gbc);
    
    JLabel hsLabel = new JLabel("Historical Simulation:");
    gbc.gridx = 4;
    gbc.gridy = 5;
    mainPanel.add(hsLabel, gbc);
    JLabel hspercentLabel = new JLabel("Percentage at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 6;
    mainPanel.add(hspercentLabel, gbc);
    JLabel hsvalueLabel = new JLabel("Value at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 7;
    mainPanel.add(hsvalueLabel, gbc);
    hspercentText = new JTextField(10);
    hspercentText.setEditable(false);
    hspercentText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 6;
    mainPanel.add(hspercentText, gbc);
    hsvalueText = new JTextField(10);
    hsvalueText.setEditable(false);
    hsvalueText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 7;
    mainPanel.add(hsvalueText, gbc);
    JLabel cvarLabel = new JLabel("Conditional Value at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 7;
    mainPanel.add(cvarLabel, gbc);
    hscvarText = new JTextField(10);
    hscvarText.setEditable(false);
    hscvarText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 7;
    mainPanel.add(hscvarText, gbc);
    JLabel cparLabel = new JLabel("Conditional Percentage at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 6;
    mainPanel.add(cparLabel, gbc);
    hscparText = new JTextField(10);
    hscparText.setEditable(false);
    hscparText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 6;
    mainPanel.add(hscparText, gbc);
    
    
    JLabel mcLabel = new JLabel("Monte Carlo:");
    gbc.gridx = 4;
    gbc.gridy = 9;
    mainPanel.add(mcLabel, gbc);
    JLabel mcpercentLabel = new JLabel("Percentage at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 10;
    mainPanel.add(mcpercentLabel, gbc);
    JLabel mcvalueLabel = new JLabel("Value at Risk:");
    gbc.gridx = 4;
    gbc.gridy = 11;
    mainPanel.add(mcvalueLabel, gbc);
    mcpercentText = new JTextField(10);
    mcpercentText.setEditable(false);
    mcpercentText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 10;
    mainPanel.add(mcpercentText, gbc);
    mcvalueText = new JTextField(10);
    mcvalueText.setEditable(false);
    mcvalueText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 5;
    gbc.gridy = 11;
    mainPanel.add(mcvalueText, gbc);
    JLabel mccvarLabel = new JLabel("Conditional Value at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 11;
    mainPanel.add(mccvarLabel, gbc);
    mccvarText = new JTextField(10);
    mccvarText.setEditable(false);
    mccvarText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 11;
    mainPanel.add(mccvarText, gbc);
    JLabel mccparLabel = new JLabel("Conditional Percentage at Risk:");
    gbc.gridx = 6;
    gbc.gridy = 10;
    mainPanel.add(mccparLabel, gbc);
    mccparText = new JTextField(10);
    mccparText.setEditable(false);
    mccparText.setBackground(Color.LIGHT_GRAY);
    gbc.gridx = 7;
    gbc.gridy = 10;
    mainPanel.add(mccparText, gbc);
   
    varchartButton = new JButton("VaR Charts");
    varchartButton.setForeground(Color.BLUE);
    varchartButton.setOpaque(false);
    varchartButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            varchartButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            varchartButton.setForeground(Color.BLUE);
        }
    });
    gbc.gridx = 5;
    gbc.gridy = 12;
    mainPanel.add(varchartButton, gbc);
    varchartButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showVaRPieCharts();
        }

      });
    varchartButton.setEnabled(false);
    
    cvarChartButton = new JButton("CVaR Charts");
    cvarChartButton.setForeground(Color.BLUE);
    cvarChartButton.setOpaque(false);
    cvarChartButton.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            cvarChartButton.setForeground(Color.BLACK);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
            cvarChartButton.setForeground(Color.BLUE);
        }
    });
    gbc.gridx = 7;
    gbc.gridy = 12;
    mainPanel.add(cvarChartButton, gbc);
    cvarChartButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          showCVaRPieCharts();
        }

      });
    cvarChartButton.setEnabled(false);
    
    mainFrame.add(mainPanel);
    mainFrame.setVisible(true);
    
  }
  
  /** This method opens a system file browser.
   * 
   * @return this is the path of the file that is selected.
   */
  
  private static String browse() {
    
    File file;
    fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "CSV Files", "csv");
    fileChooser.setFileFilter(filter);
   
    int returnVal = fileChooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      file = fileChooser.getSelectedFile();
    } else {
      file = null;
    }
    return file.getAbsolutePath();
    
  }
  
  protected static void help() {
    HelpView.run();
  }

  protected static void reset() {
    thText.setText("");
    fileText.setText("");
    confidenceText.setText("");
    hspercentText.setText("");
    mbpercentText.setText("");
    mcpercentText.setText("");
    hsvalueText.setText("");
    mbvalueText.setText("");
    mcvalueText.setText("");
    hscvarText.setText("");
    hscparText.setText("");
    mbcvarText.setText("");
    mbcparText.setText("");
    mccvarText.setText("");
    mccparText.setText("");
    varchartButton.setEnabled(false);
    cvarChartButton.setEnabled(false);
    

  }

  public static String getTimeHorizon() {
    return thText.getText();
  }

  public static String getConfidence() {
    return confidenceText.getText();
  }

  public static void setHsPercentText(String d) {
    hspercentText.setText(d);
  }
  
  public static void setMbPercentText(String d) {
    mbpercentText.setText(d);
  }
  
  public static void setMcPercentText(String d) {
    mcpercentText.setText(d);
  }

  public static void setHsValueText(String d) {
    hsvalueText.setText(d);
  } 
  
  public static void setMbValueText(String d) {
    mbvalueText.setText(d);
  } 
  
  public static void setMcValueText(String d) {
    mcvalueText.setText(d);
  }

  public static String getFileText() {
    return fileText.getText();
  } 
  
  /**Self made method to check that a string is an integer.
   * 
   * @param s this is the string to be checked.
   * @return returns true if s is an integer.
   */
  
  public static boolean isInteger(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  /**Self made method to check that a string is a double.
   * 
   * @param s this is the string to be checked.
   * @return returns true if s is a double.
   */
  
  public static boolean isDouble(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  public static void showLoading() {
    loadFrame.setVisible(true);
  }
  
  public static void hideLoading() {
    loadFrame.setVisible(false);
  }
 
  private static void showVaRPieCharts() {

    JFrame chartFrame = new JFrame("Value at Risk");
    chartFrame.setSize(1000, 350);
    chartFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb); 
    FlowLayout layout = new FlowLayout();
    chartFrame.setLayout(layout);
    chartFrame.getContentPane().setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));

    DefaultPieDataset<String> dataSet1 = new DefaultPieDataset<String>();
    dataSet1.setValue("VaR", Double.parseDouble(mbpercentText.getText()));
    dataSet1.setValue("Risk Free", 100 - Double.parseDouble(mbpercentText.getText()));
    JFreeChart mbChart = ChartFactory.createPieChart(
        "Model Building results", dataSet1, false, false, false);
    PieSectionLabelGenerator labelGenerator1 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) mbChart.getPlot()).setLabelGenerator(labelGenerator1);
    mbChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp1 = new ChartPanel(mbChart);
    cp1.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp1, FlowLayout.LEFT);
    
    
    DefaultPieDataset<String> dataSet2 = new DefaultPieDataset<String>();
    dataSet2.setValue("VaR", Double.parseDouble(hspercentText.getText()));
    dataSet2.setValue("Risk Free", 100 - Double.parseDouble(hspercentText.getText()));
    JFreeChart hsChart = ChartFactory.createPieChart(
        "Historical Simulation results", dataSet2, false, false, false);
    PieSectionLabelGenerator labelGenerator2 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) hsChart.getPlot()).setLabelGenerator(labelGenerator2);
    hsChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp2 = new ChartPanel(hsChart);
    cp2.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp2, FlowLayout.CENTER);
    
    
    DefaultPieDataset<String> dataSet3 = new DefaultPieDataset<String>();
    dataSet3.setValue("VaR", Double.parseDouble(mcpercentText.getText()));
    dataSet3.setValue("Risk Free", 100 - Double.parseDouble(mcpercentText.getText()));
    JFreeChart mcChart = ChartFactory.createPieChart(
        "Monte Carlo results", dataSet3, false, false, false);
    PieSectionLabelGenerator labelGenerator3 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) mcChart.getPlot()).setLabelGenerator(labelGenerator3);
    mcChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp3 = new ChartPanel(mcChart);
    cp3.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp3, FlowLayout.RIGHT);

    chartFrame.setVisible(true);
    
  }
  
  private static void showCVaRPieCharts() {

    JFrame chartFrame = new JFrame("Conditional Value at Risk");
    chartFrame.setSize(1000, 350);
    chartFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    
    float[] hsb = new float[3];
    Color.RGBtoHSB(41, 227, 190, hsb); 
    FlowLayout layout = new FlowLayout();
    chartFrame.setLayout(layout);
    chartFrame.getContentPane().setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));

    DefaultPieDataset<String> dataSet1 = new DefaultPieDataset<String>();
    dataSet1.setValue("CVaR", Double.parseDouble(mbcparText.getText()));
    dataSet1.setValue("Risk Free", 100 - Double.parseDouble(mbcparText.getText()));
    JFreeChart mbChart = ChartFactory.createPieChart(
        "Model Building results", dataSet1, false, false, false);
    PieSectionLabelGenerator labelGenerator1 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) mbChart.getPlot()).setLabelGenerator(labelGenerator1);
    mbChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp1 = new ChartPanel(mbChart);
    cp1.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp1, FlowLayout.LEFT);
    
    
    DefaultPieDataset<String> dataSet2 = new DefaultPieDataset<String>();
    dataSet2.setValue("CVaR", Double.parseDouble(hscparText.getText()));
    dataSet2.setValue("Risk Free", 100 - Double.parseDouble(hscparText.getText()));
    JFreeChart hsChart = ChartFactory.createPieChart(
        "Historical Simulation results", dataSet2, false, false, false);
    PieSectionLabelGenerator labelGenerator2 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) hsChart.getPlot()).setLabelGenerator(labelGenerator2);
    hsChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp2 = new ChartPanel(hsChart);
    cp2.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp2, FlowLayout.CENTER);
    
    
    DefaultPieDataset<String> dataSet3 = new DefaultPieDataset<String>();
    dataSet3.setValue("CVaR", Double.parseDouble(mccparText.getText()));
    dataSet3.setValue("Risk Free", 100 - Double.parseDouble(mccparText.getText()));
    JFreeChart mcChart = ChartFactory.createPieChart(
        "Monte Carlo results", dataSet3, false, false, false);
    PieSectionLabelGenerator labelGenerator3 = new StandardPieSectionLabelGenerator(
            "{0} : ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.000%"));
    ((PiePlot) mcChart.getPlot()).setLabelGenerator(labelGenerator3);
    mcChart.setBackgroundPaint(new Color(41, 227, 190));
    ChartPanel cp3 = new ChartPanel(mcChart);
    cp3.setPreferredSize(new Dimension(300, 300));
    chartFrame.add(cp3, FlowLayout.RIGHT);

    chartFrame.setVisible(true);
    
  }

  public static void setHScvarText(String format) {
    hscvarText.setText(format);
  }
  
  public static void setHScparText(String format) {
    hscparText.setText(format);
  }

  public static void setMBcparText(String format) {
    mbcparText.setText(format);
  }
  
  public static void setMBcvarText(String format) {
    mbcvarText.setText(format);
  }
  
  public static void setMCcparText(String format) {
    mccparText.setText(format);
  }

  public static void setMCcvarText(String format) {
    mccvarText.setText(format);
  }
}

