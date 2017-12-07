package GUI.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import parts.PartConsts;
import core.Main;

public class NewElement extends Dialog implements PartConsts {

    // Components
    private JLabel nameL;
    private JTextField nameT;

    private JCheckBox carryOverC;
    private JCheckBox showTargetC;

    private JCheckBox percC;
    private JLabel percL;
    private JTextField percT;

    private JPanel typeL;
    private JRadioButton typeSR;
    private JRadioButton typeER;
    private JRadioButton typeAR;
    private ButtonGroup typeG;

    private JLabel prioL;
    private JSpinner prioS;

    private JButton ok;
    private JButton cancel;

    // Fields
    public String name;
    public boolean carryOver;
    public boolean showTarget;
    public boolean percentage;
    public double amount;
    public int type;
    public int priority;

    public NewElement(Main main) {
	super(main.getFrame());

	init();

    }

    private void init() {
	setLayout(null);

	int width = 310;

	int comH = 25;
	int pad = 5;
	int comps = 0;
	int spaces = 0;

	nameL = new JLabel("Name:");
	nameL.setSize(width, comH);
	nameL.setLocation(5, pad + comps * comH + pad * spaces);
	add(nameL);
	comps++;

	nameT = new JTextField();
	nameT.setSize(width, comH);
	nameT.setLocation(5, pad + comps * comH + pad * spaces);
	add(nameT);
	comps++;

	// leave space
	spaces++;

	typeL = new JPanel();
	typeL.setLayout(null);
	typeL.setSize(width, comH * 4 + 20);
	typeL.setLocation(5, pad + comps * comH + pad * spaces);
	typeL.setBorder(BorderFactory.createTitledBorder(new LineBorder(
		new Color(190, 190, 255)), "Type", TitledBorder.LEFT,
		TitledBorder.TOP));
	add(typeL);
	comps += 5;

	typeSR = new JRadioButton("Savings");
	typeSR.setSize(width-10, comH);
	typeSR.setLocation(5, pad + comH);
	typeSR.addActionListener(this);
	typeL.add(typeSR);

	typeER = new JRadioButton("Expense");
	typeER.setSize(width-10, comH);
	typeER.setLocation(5, 2 * pad + comH * 2);
	typeER.addActionListener(this);
	typeL.add(typeER);

	typeAR = new JRadioButton("Allowance", true);
	typeAR.setSize(width-10, comH);
	typeAR.setLocation(5, 3 * pad + comH * 3);
	typeAR.addActionListener(this);
	typeL.add(typeAR);

	typeL.doLayout();

	typeG = new ButtonGroup();
	typeG.add(typeSR);
	typeG.add(typeER);
	typeG.add(typeAR);

	// leave space
	spaces++;

	carryOverC = new JCheckBox("Carries Over?");
	carryOverC.setSize(width, comH);
	carryOverC.setLocation(5, pad + comps * comH + pad * spaces);
	add(carryOverC);
	comps++;

	showTargetC = new JCheckBox("Show Target?");
	showTargetC.setSize(width, comH);
	showTargetC.setLocation(5, pad + comps * comH + pad * spaces);
	add(showTargetC);
	comps++;
	
	percC = new JCheckBox("Percentage based?", false);
	percC.setSize(width, comH);
	percC.setLocation(5, pad + comps * comH + pad * spaces);
	percC.addActionListener(this);
	add(percC);
	comps++;

	percL = new JLabel("Amount: (a real number)");
	percL.setSize(width, comH);
	percL.setLocation(5, pad + comps * comH + pad * spaces);
	add(percL);
	comps++;

	percT = new JTextField("0.0");
	percT.setSize(width, comH);
	percT.setLocation(5, pad + comps * comH + pad * spaces);
	add(percT);
	comps++;

	// leave space
	spaces++;

	prioL = new JLabel("Priority");
	prioL.setSize(width, comH);
	prioL.setLocation(5, pad + comps * comH + pad * spaces);
	add(prioL);
	comps++;

	prioS = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
	prioS.setSize(width, comH);
	prioS.setLocation(5, pad + comps * comH + pad * spaces);
	add(prioS);
	comps++;

	// leave space
	spaces++;
	spaces++;

	ok = new JButton("Ok");
	ok.setSize(80, comH);
	ok.setLocation((width)/2-80, pad + comps * comH + pad * spaces);
	ok.addActionListener(this);
	add(ok);

	cancel = new JButton("Cancel");
	cancel.setSize(90, comH);
	cancel.setLocation((width)/2+5, pad + comps * comH + pad * spaces);
	cancel.addActionListener(this);
	add(cancel);
	comps++;

	// leave space
	spaces++;

	setLocation(parent.getX() + (parent.getWidth()) / 2 - width / 2, parent
		.getY());

	setSize(width+30, pad + (comps + 1) * comH + pad * spaces + 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(percC)) {
	    if (percC.isSelected()) {
		percL.setText("Percentage: (a number between 0 and 100)");
	    } else {
		percL.setText("Amount: (a real number)");
	    }
	} else if (e.getSource().equals(typeSR)) {
	    carryOverC.setText("Main savings Account?");
	    
	    showTargetC.setEnabled(false);
	    prioL.setEnabled(false);
	    prioS.setEnabled(false);
	    percC.setEnabled(false);
	    percL.setText("Amount: (a real number)");
	} else if (e.getSource().equals(typeER) || e.getSource().equals(typeAR)) {
	    carryOverC.setText("Carries Over?");

	    showTargetC.setEnabled(true);
	    prioL.setEnabled(true);
	    prioS.setEnabled(true);
	    percC.setEnabled(true);
	    if (percC.isSelected()) {
		percL.setText("Percentage: (a number between 0 and 100)");
	    } else {
		percL.setText("Amount: (a real number)");
	    }
	} else if (e.getSource().equals(ok)) {
	    option = OK;

	    name = nameT.getText();
	    if (name.contains(" ")) {
		JOptionPane.showMessageDialog(this,
			"The name cannot have any spaces", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    
	    carryOver = carryOverC.isSelected();
	    showTarget = showTargetC.isSelected();
	    percentage = percC.isSelected();

	    if (typeSR.isSelected()) {
		type = SAVINGS;
		percentage = false;
	    } else if (typeER.isSelected()) {
		type = EXPENCE;
	    } else if (typeAR.isSelected()) {
		type = ALLOWANCE;
	    }

	    try {
		amount = Double.parseDouble(percT.getText());
		if(percentage){
		    amount=amount/100;
		}
	    } catch (Exception exc) {
		String tmp = percentage ? "percantage" : "amount";
		JOptionPane.showMessageDialog(this, "The " + tmp
			+ " must be a number", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if (percentage) {
		if (amount < 0 || amount > 1) {
		    JOptionPane.showMessageDialog(this,
			    "The percentage must be a number between 0 and 100",
			    "Error", JOptionPane.ERROR_MESSAGE);
		    return;
		}

	    }
	    if (type != SAVINGS) {
		try {
		    priority = ((Integer) prioS.getModel().getValue())
			    .intValue();
		} catch (Exception exc) {
		    JOptionPane.showMessageDialog(this,
			    "The priority must be a whole number", "Error",
			    JOptionPane.ERROR_MESSAGE);
		    return;
		}
	    } else {
		priority = -1;
	    }

	    setVisible(false);
	} else if (e.getSource().equals(cancel)) {
	    option = CANCEL;
	    setVisible(false);
	}

    }

}
