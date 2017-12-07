package GUI.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import parts.PartConsts;
import core.Main;

public class Options extends Dialog implements PartConsts, ChangeListener {

    // Components
    private JLabel incomeL;
    private JTextField incomeT;

    private JPanel saveP;
    private JRadioButton alSave;
    private JRadioButton asSave;
    private JRadioButton neSave;

    private JPanel archP;
    private JRadioButton alArch;
    private JRadioButton asArch;
    private JRadioButton neArch;

    private JPanel dimP;
    private JLabel widthL;
    private JSlider widthSl;
    private JLabel heightL;
    private JSlider heightSl;

    private JLabel currL;
    private JTextField preT;
    private JLabel valueL;
    private JTextField sufT;
    private JLabel previewL;

    private JButton ok;
    private JButton cancel;

    // Fields
    public core.Options o;

    public Options(Main main) {
	super(main.getFrame());
	// By default: so exiting has this value
	option = CANCEL;

	o = main.getOptions();

	init();
    }

    private void init() {
	setLayout(null);

	int width = 265;

	int comH = 28;
	int pad = 5;
	int comps = 0;
	int spaces = 0;

	incomeL = new JLabel("Income:");
	incomeL.setSize(200, comH);
	incomeL.setLocation(5, pad + comps * comH + pad * spaces);
	add(incomeL);
	comps++;

	incomeT = new JTextField("" + o.getIncome());
	incomeT.setSize(200, comH);
	incomeT.setLocation(5, pad + comps * comH + pad * spaces);
	add(incomeT);
	comps++;

	// leave space
	spaces++;

	currL = new JLabel("Currency:");
	currL.setSize(200, comH);
	currL.setLocation(5, pad + comps * comH + pad * spaces);
	add(currL);
	comps++;

	preT = new JTextField("" + o.getPrefix());
	preT.setSize(65, comH);
	preT.setLocation(5, pad + comps * comH + pad * spaces);
	preT.addActionListener(this);
	add(preT);

	valueL = new JLabel("3141.59");
	valueL.setSize(75, comH);
	valueL.setLocation(preT.getX() + preT.getWidth() + 5, pad + comps
		* comH + pad * spaces);
	valueL.setAlignmentY(Component.CENTER_ALIGNMENT);
	add(valueL);

	sufT = new JTextField("" + o.getSuffix());
	sufT.setSize(65, comH);
	sufT.setLocation(
		preT.getX() + preT.getWidth() + valueL.getWidth() + 10, pad
			+ comps * comH + pad * spaces);
	sufT.addActionListener(this);
	add(sufT);
	comps++;

	previewL = new JLabel(preT.getText() + "3141.59" + sufT.getText());
	previewL.setSize(200, comH);
	previewL.setLocation(5, pad + comps * comH + pad * spaces);
	add(previewL);
	comps++;

	// leave space
	spaces++;

	saveP = new JPanel();
	saveP.setLayout(null);
	saveP.setSize(125, comH * 4 + pad * 5);
	saveP.setLocation(5, pad + comps * comH + pad * spaces);
	saveP.setBorder(BorderFactory.createTitledBorder(new LineBorder(
		new Color(190, 190, 255)), "Save On Exit", TitledBorder.LEFT,
		TitledBorder.TOP));
	add(saveP);

	alSave = new JRadioButton("Always");
	alSave.setSize(115, comH);
	alSave.setLocation(5, pad + comH / 2);
	alSave.setSelected(o.getSaveOnExit() == core.Options.ALWAYS);
	saveP.add(alSave);

	asSave = new JRadioButton("Ask");
	asSave.setSize(115, comH);
	asSave.setLocation(5, pad * 2 + comH + comH / 2);
	asSave.setSelected(o.getSaveOnExit() == core.Options.ASK);
	saveP.add(asSave);

	neSave = new JRadioButton("Never");
	neSave.setSize(115, comH);
	neSave.setLocation(5, pad * 3 + comH * 2 + comH / 2);
	neSave.setSelected(o.getSaveOnExit() == core.Options.NEVER);
	saveP.add(neSave);

	archP = new JPanel();
	archP.setLayout(null);
	archP.setSize(125, comH * 4 + pad * 5);
	archP.setLocation(130, pad + comps * comH + pad * spaces);
	archP.setBorder(BorderFactory.createTitledBorder(new LineBorder(
		new Color(190, 190, 255)), "Archive On Exit",
		TitledBorder.LEFT, TitledBorder.TOP));
	add(archP);

	alArch = new JRadioButton("Always");
	alArch.setSize(115, comH);
	alArch.setLocation(5, pad + comH / 2);
	alArch.setSelected(o.getArchiveOnExit() == core.Options.ALWAYS);
	archP.add(alArch);

	asArch = new JRadioButton("Ask");
	asArch.setSize(115, comH);
	asArch.setLocation(5, pad * 2 + comH + comH / 2);
	asArch.setSelected(o.getArchiveOnExit() == core.Options.ASK);
	archP.add(asArch);

	neArch = new JRadioButton("Never");
	neArch.setSize(115, comH);
	neArch.setLocation(5, pad * 3 + comH * 2 + comH / 2);
	neArch.setSelected(o.getArchiveOnExit() == core.Options.NEVER);
	archP.add(neArch);

	comps += 5;

	// leave space
	spaces++;

	dimP = new JPanel();
	dimP.setLayout(null);
	dimP.setSize(250, comH * 5 + pad * 5);
	dimP.setLocation(5, pad + comps * comH + pad * spaces);
	dimP.setBorder(BorderFactory.createTitledBorder(new LineBorder(
		new Color(190, 190, 255)), "Prefered Dimetions",
		TitledBorder.LEFT, TitledBorder.TOP));
	add(dimP);
	comps += 6;

	widthL = new JLabel("Width  (" + (int) o.getPrefAccSize().getWidth()
		+ ")");
	widthL.setSize(190, comH);
	widthL.setLocation(5, pad + comH / 2);
	dimP.add(widthL);

	widthSl = new JSlider(50, 350, (int) o.getPrefAccSize().getWidth());
	widthSl.setMajorTickSpacing(10);
	widthSl.setPaintTicks(true);
	widthSl.setSnapToTicks(true);
	widthSl.setSize(240, comH);
	widthSl.setLocation(5, pad * 2 + comH + comH / 2);
	widthSl.addChangeListener(this);
	dimP.add(widthSl);

	heightL = new JLabel("Height  ("
		+ ((int) o.getPrefAccSize().getHeight()) + ")");
	heightL.setSize(190, comH);
	heightL.setLocation(5, pad * 3 + comH * 2 + comH / 2);
	dimP.add(heightL);

	heightSl = new JSlider(50, 350, (int) o.getPrefAccSize().getHeight());
	heightSl.setMajorTickSpacing(10);
	heightSl.setPaintTicks(true);
	heightSl.setSnapToTicks(true);
	heightSl.setSize(240, comH);
	heightSl.setLocation(5, pad * 4 + comH * 3 + comH / 2);
	heightSl.addChangeListener(this);
	dimP.add(heightSl);

	spaces++;

	// leave space
	spaces++;
	spaces++;

	ok = new JButton("Ok");
	ok.setSize(90, comH);
	ok.setLocation(15, pad + comps * comH + pad * spaces);
	ok.addActionListener(this);
	add(ok);

	cancel = new JButton("Cancel");
	cancel.setSize(90, comH);
	cancel.setLocation(115, pad + comps * comH + pad * spaces);
	cancel.addActionListener(this);
	add(cancel);
	comps++;

	// leave space
	spaces++;

	setSize(width, pad + (comps + 1) * comH + pad * spaces + 10);

	setLocation();

	ButtonGroup saveO = new ButtonGroup();
	saveO.add(alSave);
	saveO.add(asSave);
	saveO.add(neSave);

	ButtonGroup archO = new ButtonGroup();
	archO.add(alArch);
	archO.add(asArch);
	archO.add(neArch);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(ok)) {
	    option = OK;
	    double income = 0;
	    try {
		income = Double.parseDouble(incomeT.getText());
	    } catch (Exception exc) {
		JOptionPane.showMessageDialog(this,
			"The income must be a real number", "Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if (o.getPrefAccSize().width != widthSl.getValue()
		    || o.getPrefAccSize().height != heightSl.getValue()
		    || o.getIncome() != income)
		parent.m.setChanged(true);

	    o.setIncome(income);
	    o.setPrefAccSize(new Dimension(widthSl.getValue(), heightSl
		    .getValue()));
	    o.setMinAccSize(new Dimension(o.getPrefAccSize().width - 20, o
		    .getPrefAccSize().height - 20));
	    byte tmp = 0;
	    if (alSave.isSelected()) {
		tmp = core.Options.ALWAYS;
	    } else if (asSave.isSelected()) {
		tmp = core.Options.ASK;
	    } else {
		tmp = core.Options.NEVER;
	    }
	    if (o.getSaveOnExit() != tmp)
		parent.m.setChanged(true);

	    o.setSaveOnExit(tmp);

	    if (alArch.isSelected()) {
		tmp = core.Options.ALWAYS;
	    } else if (asArch.isSelected()) {
		tmp = core.Options.ASK;
	    } else {
		tmp = core.Options.NEVER;
	    }

	    if (o.getArchiveOnExit() != tmp)
		parent.m.setChanged(true);
	    o.setArchiveOnExit(tmp);

	    if (!preT.getText().equals(o.getPrefix())
		    || !sufT.getText().equals(o.getSuffix())) {
		parent.m.setChanged(true);
	    }

	    o.setPrefix(preT.getText());
	    o.setSuffix(sufT.getText());

	    setVisible(false);
	} else if (e.getSource().equals(cancel)) {
	    option = CANCEL;
	    setVisible(false);
	} else if (e.getSource().equals(sufT) || e.getSource().equals(preT)) {
	    previewL.setText(preT.getText() + "3141.59" + sufT.getText());
	}

    }

    @Override
    public void stateChanged(ChangeEvent e) {
	if (e.getSource().equals(widthSl)) {
	    widthL.setText("Width  (" + widthSl.getValue() + ")");
	} else if (e.getSource().equals(heightSl)) {
	    heightL.setText("Height  (" + heightSl.getValue() + ")");
	}

    }

}
