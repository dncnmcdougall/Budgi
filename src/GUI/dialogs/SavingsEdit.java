package GUI.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import parts.Element;
import parts.PartConsts;
import GUI.Frame;

public class SavingsEdit extends Dialog implements DialogConsts, PartConsts {

    private Element elm;

    private JLabel nameL;
    private JTextField nameT;

    private JCheckBox mainSavC;

    private JLabel targetL;
    private JSpinner targetS;

    private JButton ok;
    private JButton cancel;
    private JPanel buttonP;

    public double target;
    public String name;
    public boolean mainSav;

    public SavingsEdit(Frame parent, Element elm) {
	super(parent);
	this.elm = elm;

	int width = 265;

	setSize(width, 230);
	setLocation();

	setLayout(null);

	nameL = new JLabel("Name:");
	nameL.setSize(new Dimension(width - 15, 25));
	nameL.setLocation(5, 5);
	add(nameL);

	nameT = new JTextField(elm.getName());
	nameT.setSize(new Dimension(width - 15, 25));
	nameT.setLocation(5, 35);
	add(nameT);

	mainSavC = new JCheckBox("Main savings Account?", elm.isCarryOver());
	mainSavC.setSize(new Dimension(width - 15, 25));
	mainSavC.setLocation(5, 65);
	add(mainSavC);

	targetL = new JLabel("Target Value:");
	targetL.setSize(new Dimension(width - 15, 25));
	targetL.setLocation(5, 95);
	add(targetL);

	targetS = new JSpinner(new SpinnerNumberModel(elm.getTotal(), 0,
		Double.MAX_VALUE, 10));
	targetS.setSize(new Dimension(width - 15, 25));
	targetS.setLocation(5, 125);
	add(targetS);

	buttonP = new JPanel();
	buttonP.setSize(new Dimension(width - 15, 35));
	buttonP.setLocation(5, 155);
	buttonP.setLayout(null);

	cancel = new JButton("Cancel");
	cancel.setSize(85, 25);
	cancel.setLocation(buttonP.getWidth() / 2 + 5, 5);
	cancel.addActionListener(this);
	buttonP.add(cancel);

	ok = new JButton("Ok");
	ok.setSize(75, 25);
	ok.setLocation(buttonP.getWidth() / 2 - 80, 5);
	ok.addActionListener(this);
	buttonP.add(ok);

	add(buttonP);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	target = ((Double) targetS.getValue()).doubleValue();
	name = nameT.getText();
	mainSav = mainSavC.isSelected();

	if (e.getSource().equals(ok)) {
	    option = OK;
	} else if (e.getSource().equals(cancel)) {
	    option = CANCEL;
	}
	setVisible(false);

    }

}
