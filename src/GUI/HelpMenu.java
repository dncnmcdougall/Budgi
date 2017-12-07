package GUI;

import javax.swing.JMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import GUI.handlers.MenuEventHandler;

public class HelpMenu extends JMenu {

    private MenuEventHandler handler;
    
    private JMenuItem about;

    
    public HelpMenu(MenuEventHandler handler) {
	super("Help");
	this.handler = handler;
	
	about = new JMenuItem("About");
	about.addActionListener(handler);
	about.setActionCommand("H.about");
	add(about);
    }
}
