package cc.openhome.menu;

import cc.openhome.EasyJShop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import cc.openhome.main.EasyJShopMenu;

public class AboutMenu extends EasyJShopMenu {
    //private JMenu aboutMenu;
    private ImageIcon logoImage;
    private JMenuItem aboutEasyJShopMenuItem;
    
    public AboutMenu(EasyJShop parent) {
        super(parent);
        initResource();
        setupUIComponent();
        setupEventListener();
    }
    
    public void initResource() {
        logoImage = new ImageIcon(AboutMenu.class.getResource("../images/logo.jpg"));
    }
    
    public void setupUIComponent() {
        //aboutMenu = new JMenu();
        setText("About");
        aboutEasyJShopMenuItem = new JMenuItem("EasyJShop");
        add(aboutEasyJShopMenuItem);
    }
    
    public void setupEventListener() {
        aboutEasyJShopMenuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        
        aboutEasyJShopMenuItem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showOptionDialog(null, 
                            "EasyJShop....  :)\n" +  
                            "http://openhome.cc\n" +
                            "caterpillar@openhome.cc",
                            "About EasyJShop",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            logoImage, null, null);
                    }
                }   
            );
    }
}
