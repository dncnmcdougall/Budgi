package GUI.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import GUI.Frame;

public abstract class Dialog extends JDialog implements ActionListener,
	DialogConsts {

    protected Frame parent;

    protected int option;

    public Dialog(Frame parent) {
	super();
	this.parent = parent;

	setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
	//setModal(true);

	setResizable(false);

	// By default: so exiting has this value
	option = CANCEL;
    }

    public void setLocation() {
	int width = getWidth();

	setLocation(parent.getX() + (parent.getWidth()) / 2 - width / 2, parent
		.getY());
    }

    @Override
    public abstract void actionPerformed(ActionEvent arg0);

    public int getOption() {
	return option;
    }

}
