package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import GUI.handlers.MenuEventHandler;

public class FileMenu extends JMenu {

    private JMenuItem save;
    private JMenuItem saveOld;
    private JMenuItem close;
    private JMenuItem reload;

    private MenuEventHandler handler;

    public FileMenu(MenuEventHandler handler) {
	super("File");

	this.handler = handler;

	save = new JMenuItem("Save");
	save.addActionListener(handler);
	save.setActionCommand("F.save");
	add(save);

	reload = new JMenuItem("Reload");
	reload.addActionListener(handler);
	reload.setActionCommand("F.reload");
	add(reload);

	saveOld = new JMenuItem("Archive");
	saveOld.addActionListener(handler);
	saveOld.setActionCommand("F.saveOld");
	add(saveOld);

	addSeparator();

	close = new JMenuItem("Close");
	close.addActionListener(handler);
	close.setActionCommand("F.close");
	add(close);
    }


}
