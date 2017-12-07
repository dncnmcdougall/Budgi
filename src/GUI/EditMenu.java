package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import GUI.handlers.MenuEventHandler;

public class EditMenu extends JMenu {

    private MenuEventHandler handler;
    
    private JMenuItem add;
    private JMenuItem remove;
    private JMenuItem total;
    private JMenuItem refresh;
    
    private JMenuItem options;
    private JMenuItem conf;
    
    public EditMenu(MenuEventHandler handler) {
	super("Edit");
	this.handler = handler;

	add = new JMenuItem("Add Account");
	add.addActionListener(handler);
	add.setActionCommand("E.add");
	add(add);

	remove = new JMenuItem("Remove Account");
	remove.addActionListener(handler);
	remove.setActionCommand("E.remove");
	add(remove);

	total = new JMenuItem("Show total in accounts");
	total.addActionListener(handler);
	total.setActionCommand("E.total");
	add(total);

	refresh = new JMenuItem("Refresh");
	refresh.addActionListener(handler);
	refresh.setActionCommand("E.refresh");
	add(refresh);
	
	addSeparator();
	
	conf = new JMenuItem("Edit Config");
	conf.addActionListener(handler);
	conf.setActionCommand("E.conf");
	add(conf);
	
	options = new JMenuItem("Options");
	options.addActionListener(handler);
	options.setActionCommand("E.options");
	add(options);
    }


}
