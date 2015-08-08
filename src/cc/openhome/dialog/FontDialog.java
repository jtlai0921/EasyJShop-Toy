package cc.openhome.dialog;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FontDialog {
    private static JPanel fontPanel;
    private static JComboBox fontNameComboBox;
    private static JSpinner fontSizeSpinner;
    private static JCheckBox boldBox, italicBox;
    private static JTextField textField;
    
    static {
        setUIComponent();
        setEventListener();
    }
    
    private static void setUIComponent() {
        fontPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        fontNameComboBox = new JComboBox(fontNames);
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setValue(new Integer(12));

        boldBox = new JCheckBox("Bold");
        italicBox = new JCheckBox("Italic");
        
        textField = new JTextField("Input text here.");
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Name"));
        panel.add(fontNameComboBox);
        panel.add(new JLabel("　Size"));
        panel.add(fontSizeSpinner);
        
        fontPanel.add(panel);
        
        panel = new JPanel();
        panel.add(boldBox);
        panel.add(italicBox);
        
        fontPanel.add(panel);

        
        fontPanel.add(textField);
    }
    
    private static void setEventListener() {
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(((Integer) fontSizeSpinner.getValue()).intValue() <= 0) {
                    fontSizeSpinner.setValue(new Integer(1));
                }
            }
        });
        
        fontNameComboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                changeTextFont();
            }
            
        });
        
        boldBox.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                changeTextFont();
            }
        });
        
        italicBox.addActionListener(new ActionListener() {           
            public void actionPerformed(ActionEvent e) {
                changeTextFont();
            }
        });
    }
    
    public static int showDialog(Component component, String title, Icon logoIcon) {
        return showDialog(component, title, null, logoIcon);
    }
    
    public static int showDialog(Component component, String title, String defaultFontName, Icon logoIcon) {
        if(defaultFontName != null) {
            fontNameComboBox.setSelectedItem(defaultFontName);
        }
        
        return JOptionPane.showOptionDialog(component, 
                fontPanel, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE, logoIcon, null, null);
    }
    
    public static Font getFont() {
        return new Font(getFontName(), getFontStyle(), getFontSize());
    }
    
    public static String getFontName() {
        return (String) fontNameComboBox.getSelectedItem();
    }
    
    public static int getFontSize() {
        return ((Integer) fontSizeSpinner.getValue()).intValue();
    }
    
    public static String getInputText() {
        return textField.getText();
    }
    
    public static boolean isBold() {
        return boldBox.isSelected();
    }
    
    public static boolean isItalic() {
        return italicBox.isSelected();
    }
    
    private static int getFontStyle() {
        int fontStyle = 0;
        if(boldBox.isSelected() && italicBox.isSelected()) {
            fontStyle = Font.BOLD + Font.ITALIC;
        }
        else if(boldBox.isSelected()) {
            fontStyle = Font.BOLD;
        }
        else if(italicBox.isSelected()) {
            fontStyle = Font.ITALIC;
        }
        else {
            fontStyle = Font.PLAIN;
        }
        return fontStyle;
    }
    
    private static void changeTextFont() {
        textField.setFont(new Font(getFontName(), getFontStyle(), 14));
        textField.repaint();
    }
}
