package GUI.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import parts.Spent;

import GUI.Frame;

public class NewSpent extends Dialog implements ChangeListener, FocusListener {

    private final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr",
	    "May", "Jun", "Jul", "Aug", "Sep", "Oct","Nov", "Dec" };

    private Calendar calendar;

    private JLabel amountL;
    private JTextField amountT;

    private JLabel descL;
    private JTextArea descT;
    private JScrollPane descS;

    private JLabel dateL;
    private JSpinner dateDS;
    private JSpinner dateMS;
    private JSpinner dateYS;

    private JButton ok;
    private JButton cancel;

    public double amount;
    public String desc;
    public Date date;

    public NewSpent(Frame parent) {
    this(parent,new Spent(0.0,"Enter description here",Calendar.getInstance().getTime()));
    }
    
    public NewSpent(Frame parent,Spent sp) {
	super(parent);
	setSize(265, 320);
	setLocation();
	setTitle("Spent Details");
	setLayout(null);
	calendar = Calendar.getInstance();
	calendar.setTime(sp.getDate());

	amountL = new JLabel("Amount:");
	amountL.setSize(250, 25);
	amountL.setLocation(5, 5);
	add(amountL);

	amountT = new JTextField(""+sp.getAmount());
	amountT.setSize(250, 25);
	amountT.setLocation(5, 35);
	amountT.addFocusListener(this);
	add(amountT);

	descL = new JLabel("Description:");
	descL.setSize(250, 25);
	descL.setLocation(5, 65);
	add(descL);

	descT = new JTextArea(""+sp.getDescription());
	descT.addFocusListener(this);

	descS = new JScrollPane(descT);
	descS.setSize(250, 85);
	descS.setLocation(5, 95);
	add(descS);

	dateL = new JLabel("The date of the transaction:");
	dateL.setSize(250, 25);
	dateL.setLocation(5, 185);
	add(dateL);

	dateDS = new JSpinner(new SpinnerNumberModel(calendar
		.get(Calendar.DAY_OF_MONTH), 1, calendar
		.getMaximum(Calendar.DAY_OF_MONTH), 1));
	dateDS.setSize(75, 25);
	dateDS.setLocation(5, 215);
	add(dateDS);

	dateMS = new JSpinner(new SpinnerListModel(months));
	dateMS.setValue(months[calendar.get(Calendar.MONTH)]);
	dateMS.setSize(75, 25);
	dateMS.setLocation(85, 215);
	dateMS.addChangeListener(this);
	add(dateMS);

	dateYS = new JSpinner(new SpinnerNumberModel(calendar
		.get(Calendar.YEAR), calendar.get(Calendar.YEAR) - 100,
		calendar.get(Calendar.YEAR), 1));
	dateYS.setSize(75, 25);
	dateYS.setLocation(165, 215);
	add(dateYS);

	ok = new JButton("Ok");
	ok.setSize(80, 25);
	ok.setLocation(getWidth() / 2 - ok.getWidth(), 245);
	ok.addActionListener(this);
	add(ok);

	cancel = new JButton("Cancel");
	cancel.setSize(90, 25);
	cancel.setLocation(getWidth() / 2 + 5, 245);
	cancel.addActionListener(this);
	add(cancel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(ok)) {

	    // Amount
	    try {
		amount = Double.parseDouble(amountT.getText());
	    } catch (Exception exc) {
		JOptionPane.showMessageDialog(this,
			"The amount must be a real number", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    // -----------
	    // Desc
	    desc = descT.getText();
	    // -----------
	    // Date
	    String tmp = (String) dateMS.getValue();
	    int day = ((Integer) dateDS.getValue());

	    int month = 0;
	    for (int i = 0; i < months.length; i++) {
		if (months[i].equals(tmp)) {
		    month = i;
		    break;
		}

	    }
	    calendar.set(Calendar.MONTH, month);
	    calendar.set(Calendar.YEAR, ((Integer) dateYS.getValue())
		    .intValue());
	    calendar.set(Calendar.DAY_OF_MONTH, ((Integer) dateDS.getValue())
		    .intValue());

	    date = calendar.getTime();
	    // -----------

	    option = OK;
	} else if (e.getSource().equals(cancel)) {
	    option = CANCEL;
	}
	setVisible(false);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	// only dateMS and dateYS call this

	String tmp = (String) dateMS.getValue();
	int month = 0;
	for (int i = 0; i < months.length; i++) {
	    if (months[i].equals(tmp)) {
		month = i;
		break;
	    }

	}

	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.MONTH, month);
	cal.set(Calendar.YEAR, ((Integer) dateYS.getValue()).intValue());

	SpinnerNumberModel tmpM = ((SpinnerNumberModel) dateDS.getModel());
	tmpM.setMaximum(cal.getActualMaximum(Calendar.DAY_OF_MONTH));

	if (((Integer) dateDS.getValue()).intValue() > ((Integer) tmpM
		.getMaximum()).intValue()) {
	    tmpM.setValue(tmpM.getMaximum());
	}
    }

    @Override
    public void focusGained(FocusEvent e) {
	if(e.getSource().equals(descT)){
	    descT.setSelectionStart(0);
	    descT.setSelectionEnd(descT.getText().length());
	}else if(e.getSource().equals(amountT)){
	    amountT.setSelectionStart(0);
	    amountT.setSelectionEnd(amountT.getText().length());
	}

    }

    @Override
    public void focusLost(FocusEvent e) {
	if(e.getSource().equals(descT)){
	    descT.setSelectionStart(0);
	    descT.setSelectionEnd(0);
	}else if(e.getSource().equals(amountT)){
	    amountT.setSelectionStart(0);
	    amountT.setSelectionEnd(0);
	}

    }

}
