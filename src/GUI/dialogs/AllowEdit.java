package GUI.dialogs;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import parts.Element;
import util.Number;
import GUI.Frame;

public class AllowEdit extends Dialog {

	private Element elm;

	private JLabel nameL;
	private JTextField nameT;

	private JCheckBox carryOverC;
	private JCheckBox showTargetC;

	private JCheckBox resetC;

	private JCheckBox percC;
	private JLabel percL;
	private JTextField percT;

	private JLabel prioL;
	private JSpinner prioS;

	private JButton editTotal;

	private JButton ok;
	private JButton cancel;

	public String name;
	public boolean carryOver;
	public boolean showTarget;
	public boolean percentage;
	public double amount;
	public double total;
	public int priority;

	public boolean reset;

	public AllowEdit(Frame parent, Element elm) {
		super(parent);
		this.elm = elm;
		this.total = elm.getTotal();
		reset = false;
		ini();
	}

	private void ini() {

		int width = 310;
		setLocation();

		setLayout(null);

		int y=5;

		nameL = new JLabel("Name: ");
		nameL.setSize(width, 25);
		nameL.setLocation(10, y);
		add(nameL);
		y+=30;

		nameT = new JTextField(elm.getName());
		nameT.setSize(width, 25);
		nameT.setLocation(10, y);
		add(nameT);
		y+=30;

		carryOverC = new JCheckBox("Carries over?");
		carryOverC.setSelected(elm.isCarryOver());
		carryOverC.setSize(width, 25);
		carryOverC.setLocation(10, y);
		carryOverC.addActionListener(this);
		add(carryOverC);
		y+=30;

		showTargetC = new JCheckBox("Show Target?");
		showTargetC.setSelected(elm.isShowProgress());
		showTargetC.setSize(width, 25);
		showTargetC.setLocation(10, y);
		showTargetC.addActionListener(this);
		add(showTargetC);
		y+=30;

		resetC = new JCheckBox("Reset element");
		resetC.setSelected(false);
		resetC.setEnabled(elm.isCarryOver());
		resetC.setSize(width, 25);
		resetC.setLocation(10, y);
		resetC.addActionListener(this);
		add(resetC);
		y+=30;

		y = iniPerc(width, y);
		y = iniPrior(width, y);
		y = iniButtons(width, y);

		setSize(width+30, y+35);
	}

	private int iniPerc(int width, int y) {

		percC = new JCheckBox("Percentage based?", true);
		percC.setSize(width, 25);
		percC.setLocation(10, y);
		percC.setSelected(elm.isPercent());
		percC.addActionListener(this);
		add(percC);
		y+=30;

		String tmp = elm.isPercent() ? "Percentage (a number between 0 and 100)"
				: "Amount: (a real number)";
		percL = new JLabel(tmp);
		percL.setSize(width, 25);
		percL.setLocation(10, y);
		add(percL);
		y+=30;

		tmp = ""+ (elm.isPercent() ? elm.getAmount()*100 : elm.getAmount());
		percT = new JTextField(tmp);
		percT.setSize(width, 25);
		percT.setLocation(10, y);
		add(percT);
		y+=30;

		return y;
	}

	private int iniPrior(int width, int y) {
		prioL = new JLabel("Priority");
		prioL.setSize(width, 25);
		prioL.setLocation(10, y);
		add(prioL);
		y+=30;

		prioS = new JSpinner(new SpinnerNumberModel(elm.getPriority(), 1, 500, 1));
		prioS.setSize(width, 25);
		prioS.setLocation(10, y);
		add(prioS);
		y+=30;

		return y;
	}

	private int iniButtons(int width, int y) {

		editTotal = new JButton("Edit Total");
		editTotal.setSize(120, 25);
		editTotal.setLocation(10, y);
		editTotal.addActionListener(this);
		editTotal.setEnabled(elm.isCarryOver());
		add(editTotal);
		y+=30;

		ok = new JButton("Ok");
		ok.setSize(80, 25);
		ok.setLocation((width)/2-80, y);
		ok.addActionListener(this);
		add(ok);

		cancel = new JButton("Cancel");
		cancel.setSize(90, 25);
		cancel.setLocation((width)/2+5, y);
		cancel.addActionListener(this);
		add(cancel);
		y+=30;

		return y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(percC)) {
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
			reset = resetC.isSelected();

			try {
				amount = Double.parseDouble(percT.getText());
				if(percentage){
					amount=amount/100.0;
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

			try {
				priority = ((Integer) prioS.getModel().getValue()).intValue();
			} catch (Exception exc) {
				JOptionPane.showMessageDialog(this,
						"The priority must be a whole number", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			setVisible(false);
		} else if (e.getSource().equals(cancel)) {
			option = CANCEL;
			setVisible(false);
		} else if (e.getSource().equals(carryOverC)) {
			int action = JOptionPane.OK_OPTION;
			if (carryOverC.isSelected() != elm.isCarryOver()) {
				action = JOptionPane.showConfirmDialog(this,
						"Changing this element "
								+ (carryOverC.isSelected() ? "to" : "from")
								+ " a carry over element\r\n"
								+ "has significan consequences.\r\n"
								+ "Are you sure you want to continue?\r\n"
								+ "(See documentation for details)",
								"Warning: change CarryOver", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
			}
			if (action != JOptionPane.OK_OPTION) {
				carryOverC.setSelected(!carryOverC.isSelected());
			}
			editTotal.setEnabled(carryOverC.isSelected());
			resetC.setEnabled(carryOverC.isSelected());
		} else if (e.getSource().equals(editTotal)) {
			String tmp = JOptionPane
					.showInputDialog(
							this,
							"Please enter the new total for this carry over elemet.\r\n"
									+ "The current total is set at: "
									+ parent.m.getOptions().getPrefix()
									+ Number.round2Dp(elm.getTotal())
									+ parent.m.getOptions().getSuffix()
									+ "\r\n"
									+ "WARNING: Please see the documentation to see the effect \r\n    changing this value will have",
									total);

			boolean run = tmp != null;
			while (run) {
				try {
					total = Double.parseDouble(tmp);
					run = false;
				} catch (NumberFormatException exc) {
					run = true;
					tmp = JOptionPane
							.showInputDialog(
									this,
									"Please try again with a valid number\r\n"
											+ "The current total is set at: "
											+ parent.m.getOptions().getPrefix()
											+ Number.round2Dp(elm.getTotal())
											+ parent.m.getOptions().getSuffix()
											+ "\r\n"
											+ "WARNING: Please see the documentation to see the effect \r\n    changing this value will have",
											total);
					run = tmp != null;
				}
			}
		} else if (e.getSource().equals(resetC)) {

			if (!resetC.isSelected()) {
				editTotal.setEnabled(true);
			}
			if (!reset && resetC.isSelected()) {
				int action = JOptionPane
						.showConfirmDialog(
								this,
								"You are about to reset this carry over element.\r\n"
										+ "This clears all the elements expences and sets its total to 0.\n\r",
										"Reset?", JOptionPane.YES_NO_OPTION);

				if (action != JOptionPane.YES_OPTION) {
					resetC.setSelected(false);
				} else {
					reset = true;
					editTotal.setEnabled(false);
				}
			}
		}

	}

}
