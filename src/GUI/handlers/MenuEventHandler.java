package GUI.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import parts.Element;
import parts.PartConsts;
import sun.util.calendar.BaseCalendar.Date;
import util.Exporter;
import GUI.dialogs.AboutDialog;
import GUI.dialogs.ConfigDialog;
import GUI.dialogs.Dialog;
import GUI.dialogs.NewElement;
import GUI.dialogs.RemoveElement;
import GUI.renderers.Renderer;
import core.Main;

public class MenuEventHandler extends Handler implements MouseListener, ActionListener {

	private RemoveElement re;

	public MenuEventHandler(Main parent) {
		super(parent);
	}

	public void addElement() {

		NewElement ne = new NewElement(parent);
		ne.setVisible(true);

		if (ne.getOption() == CANCEL) {
			return;
		} else {
			Element e = new Element(ne.name, ne.priority, ne.type,
					ne.carryOver, ne.percentage, ne.amount,ne.showTarget);

			if (e.getType() == PartConsts.SAVINGS && e.isCarryOver()) {
				if (parent.getMainSav() != null) {
					int opt = JOptionPane.showConfirmDialog(parent.getFrame(),
							"Do you want to replace \""
									+ parent.getMainSav().getName()
									+ "\" with \"" + e.getName()
									+ "\"\nas the main savings Account?",
									"Replace Main Savings Account",
									JOptionPane.YES_NO_OPTION);

					if (opt == JOptionPane.YES_OPTION) {
						parent.getMainSav().setCarryOver(false);
						parent.setMainSav(e);
					} else {
						e.setCarryOver(false);
					}
				} else {
					parent.setMainSav(e);
				}
			}

			if (!ne.percentage) {
				e.setTotal(e.getInitTotal(0));
			}
			parent.addElement(e);
		}
	}

	public void refresh() {
		parent.proccessElements();
	}

	public void options() {
		GUI.dialogs.Options optD = new GUI.dialogs.Options(parent);
		optD.setVisible(true);

		if (optD.getOption() == CANCEL) {
			return;
		} else {
			parent.setOptions(optD.o);
			parent.getFrame().doLayout();
		}
	}

	public void close() {

		boolean changed = parent.isChanged();
		core.Options o = parent.getOptions();

		// Checking whether to save
		int save = o.getSaveOnExit();
		switch (save) {
		case core.Options.ALWAYS:
			save();
			break;
		case core.Options.ASK:
			if (changed) {
				int opt = JOptionPane
						.showConfirmDialog(
								parent.getFrame(),
								"There are unsaved changes. Do you want to save them now?",
								"Save?", JOptionPane.YES_NO_OPTION);
				switch (opt) {
				case JOptionPane.YES_OPTION:
					save();
					break;
				case JOptionPane.NO_OPTION:
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
				}
			}
			break;
		case core.Options.NEVER:
			break;
		}

		// Checking whether to archive
		int arch = o.getArchiveOnExit();
		switch (arch) {
		case core.Options.ALWAYS:
			saveOld();
			break;
		case core.Options.ASK:
			if (changed) {
				int opt = JOptionPane
						.showConfirmDialog(
								parent.getFrame(),
								"There are unsaved changes. Do you want to Archive them now?",
								"Archive?", JOptionPane.YES_NO_OPTION);
				switch (opt) {
				case JOptionPane.YES_OPTION:
					saveOld();
					break;
				case JOptionPane.NO_OPTION:
					break;
				case JOptionPane.CANCEL_OPTION:
					return;
				}
			}
			break;
		case core.Options.NEVER:
			break;
		}

		parent.getFrame().setVisible(false);
		parent.close();
	}

	public void save() {
		parent.save();

	}

	public void reload() {
		parent.clearElements();
		parent.setChanged(!parent.load());
	}

	public void saveOld() {
		parent.saveOld();

	}

	public void showCurrentBalance() {
		JOptionPane.showMessageDialog(parent.getFrame(), "You currently have\n"
				+ parent.getOptions().getPrefix() + parent.getTotalBalance()
				+ parent.getOptions().getSuffix() + "\n in your account",
				"Balance", JOptionPane.PLAIN_MESSAGE);
	}

	public void remove() {
		if (parent.getElementCnt() == 0) {
			JOptionPane.showMessageDialog(parent.getFrame(),
					"There are no elemnts to remove", "Cannot remove.",
					JOptionPane.PLAIN_MESSAGE);
			return;
		}
		re = new RemoveElement(parent.getFrame());
		re.setVisible(true);
	}

	public void config() {
		ConfigDialog conf = new ConfigDialog(parent);
		conf.setVisible(true);
		int opt=conf.getOption();

		if(opt==Dialog.OK){
			String text = conf.getText();

			parent.getConf().setSaveDir(new File(text));
			parent.getConf().create();
		}
	}

	public void about(){
		AboutDialog aboutD = new AboutDialog(parent.getFrame(),parent.getConf().getVersion());
		aboutD.setVisible(true);
	}

	private void export() {
		int ret=JOptionPane.showConfirmDialog(parent.getFrame(), "You are about to export your whole history.\n\rDo you want to continue?");
		if(ret!=JOptionPane.YES_OPTION){
			JOptionPane.showMessageDialog(parent.getFrame(), "Export Aborted");
			return;
		}
		switch(Exporter.export(parent)){
		case 0:
			JOptionPane.showMessageDialog(parent.getFrame(), "Export Completed suceffully.");
			break;
		case 1:
			JOptionPane.showMessageDialog(parent.getFrame(), "Export Aborted");
			break;
		default:
			JOptionPane.showMessageDialog(parent.getFrame(), "Export Failed");
			break;
		}

	}

	@Override
	public void mouseClicked(MouseEvent eve) {
		if (re != null) {
			Renderer rnd = ((Renderer) eve.getSource());
			Element elm = rnd.getElement();
			re.update(elm.getName());
			re.setVisible(false);
			if (re.getOption() == OK) {
				parent.removeElement(elm);
			}
			re = null;
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Do nothing
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// Do nothing
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// Do nothing
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		if (command.equals("E.add")) {
			addElement();
		} else if (command.equals("E.remove")) {
			remove();
		} else if (command.equals("E.total")) {
			showCurrentBalance();
		} else if (command.equals("E.refresh")) {
			refresh();
		} else if (command.equals("F.save")) {
			save();
		} else if (command.equals("F.reload")) {
			reload();
		} else if (command.equals("F.saveOld")) {
			saveOld();
		} else if (command.equals("F.close")) {
			close();
		}else if(command.equals("E.conf")){
			config();
		}else if (command.equals("E.options")) {
			options();
		}else if(command.equals("T.export")){
			export();

		}else if(command.equals("H.about")){
			about();
		}

	}
}
