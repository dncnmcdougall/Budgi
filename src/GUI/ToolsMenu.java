package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import GUI.handlers.MenuEventHandler;

public class ToolsMenu extends JMenu  {

    private MenuEventHandler handler;

    private JMenuItem export;

    public ToolsMenu(MenuEventHandler handler){
	super("Tools");
	this.handler = handler;
	
	export = new JMenuItem("Export");
	export.addActionListener(handler);
	export.setActionCommand("T.export");
	add(export);
    }
    
}
