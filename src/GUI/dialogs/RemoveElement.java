package GUI.dialogs;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;

import GUI.Frame;

public class RemoveElement extends Dialog {

	private JTextArea message;
	private JButton yes;
	private JButton no;

	public RemoveElement(Frame parent) {
		super(parent);
		setModalityType(java.awt.Dialog.ModalityType.MODELESS);
		init();
	}

	public void init() {
		setLayout(null);
		setLocation();
		setSize(330, 150);

		yes = new JButton("Yes");
		yes.setSize(100, 25);
		yes.setLocation(40, 85);
		yes.addActionListener(this);
		yes.setVisible(false);
		add(yes);

		no = new JButton("No");
		no.setSize(100, 25);
		no.setLocation(160, 85);
		no.addActionListener(this);
		no.setVisible(false);
		add(no);

		message = new JTextArea("Please click on the Account to delete");
		message.setEditable(false);
		message.setBackground(getContentPane().getBackground());
		message.setFont(yes.getFont());
		message.setSize(getWidth()-10, 70);
		message.setLocation(5, 5);
		add(message);

	}

	public void update(String name) {
		setVisible(false);
		message.setText("Are you sure you want to delete Element:\n'" + name + "'?");
		yes.setVisible(true);
		no.setVisible(true);
		setModal(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent eve) {
		if (eve.getSource().equals(yes)) {
			option = OK;
		} else if (eve.getSource().equals(no)) {
			option = CANCEL;
		}
		setVisible(false);

	}

}
