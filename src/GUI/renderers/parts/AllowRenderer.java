package GUI.renderers.parts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import parts.Element;
import util.Number;
import GUI.handlers.AllowHandler;
import GUI.renderers.Renderer;
import GUI.util.ProgressBar;
import core.Main;

public class AllowRenderer extends Renderer implements ChangeListener,
	ActionListener {

    private AllowHandler handler;

    private ProgressBar bar;

    // LD= label, description, LV = label, value
    private JLabel totalLD;
    private JLabel totalLV;
    private JLabel usedLD;
    private JLabel usedLV;
    private JLabel leftLD;
    private JLabel leftLV;
    // Panel for the labels
    private JPanel labelP;

    // The control buttons
    private JButton add;
    private JButton edit;
    private JButton view;
    // the panel for the buttons
    private JPanel buttonsP;

    public AllowRenderer(Element elm, Main main) {
	super(elm, main);
	elm.setListener(this);
	setLayout(new BorderLayout(0,2));

	
	setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(
			190, 190, 255)), elm.getName(), TitledBorder.LEFT,
			TitledBorder.TOP,TITLE_FONT));

	ini();
    }

    private void ini() {

	handler = new AllowHandler(parent, elm);

	iniBar();
	iniLabels();
	initButtons();
    }

    private void iniBar() {

	bar = new ProgressBar(elm.getTotal(), 0, elm.getAmountSpentOn(), false,
		new Color(255, 140, 100), new Color(100, 180, 100),new Color(70, 150, 120),new Color(255,90,80),elm.isShowProgress());
	bar.setPreferredSize(new Dimension(25, 25));
	bar.setTarget(bar.getMax()*Number.getPecInMonth());

	add(bar, BorderLayout.CENTER);

    }

    private void iniLabels() {
	labelP = new JPanel();
	labelP.setPreferredSize(new Dimension(getWidth(), 60));
	labelP.setLayout(null);

	totalLD = new JLabel("Total:");
	totalLD.setFont(CONTENT_FONT);
	labelP.add(totalLD);

	totalLV = new JLabel(parent.getOptions().getPrefix() + "0.00"
		+ parent.getOptions().getSuffix());
	totalLV.setFont(CONTENT_FONT);
	labelP.add(totalLV);

	usedLD = new JLabel("Used:");
	usedLD.setFont(CONTENT_FONT);
	labelP.add(usedLD);

	usedLV = new JLabel(parent.getOptions().getPrefix() + "0.00"
		+ parent.getOptions().getSuffix());
	usedLV.setFont(CONTENT_FONT);
	labelP.add(usedLV);

	leftLD = new JLabel("Left:");
	leftLD.setFont(CONTENT_FONT);
	labelP.add(leftLD);

	leftLV = new JLabel(parent.getOptions().getPrefix() + "0.00"
		+ parent.getOptions().getSuffix());
	leftLV.setFont(CONTENT_FONT);
	labelP.add(leftLV);

	add(labelP, BorderLayout.NORTH);
    }

    private void initButtons() {

	buttonsP = new JPanel();
	buttonsP.setPreferredSize(new Dimension(getWidth(), 25));
	buttonsP.setLayout(new BorderLayout(2,0));

	add = new JButton("Add");
	add.setToolTipText("Add an entry");
	add.addActionListener(this);
	add.setFont(BUTTON_FONT);
	buttonsP.add(add, BorderLayout.WEST);

	edit = new JButton("Edit");
	edit.setToolTipText("Edit this account's details");
	edit.addActionListener(this);
	edit.setFont(BUTTON_FONT);
	buttonsP.add(edit, BorderLayout.EAST);

	view = new JButton("View");
	view.setToolTipText("View the entries in this account");
	view.addActionListener(this);
	view.setFont(BUTTON_FONT);
	buttonsP.add(view, BorderLayout.CENTER);

	add(buttonsP, BorderLayout.SOUTH);

    }

    @Override
    public void update() {
	bar.setValue(elm.getAmountSpentOn());

	labelP.setPreferredSize(new Dimension(getWidth(), 60));

	totalLD.setBounds(0, 0, labelP.getWidth() / 2,
		labelP.getHeight() / 3 - 10);
	totalLV.setBounds(labelP.getWidth() / 2, 0, labelP.getWidth() / 2,
		labelP.getHeight() / 3 - 10);
	usedLD.setBounds(0, labelP.getHeight() / 3, labelP.getWidth() / 2,
		labelP.getHeight() / 3 - 10);
	usedLV.setBounds(labelP.getWidth() / 2, labelP.getHeight() / 3, labelP
		.getWidth() / 2, labelP.getHeight() / 3 - 10);
	leftLD.setBounds(0, (2 * labelP.getHeight()) / 3,
		labelP.getWidth() / 2, (labelP.getHeight()) / 3 - 10);
	leftLV.setBounds(labelP.getWidth() / 2, (2 * labelP.getHeight()) / 3,
		labelP.getWidth() / 2, (labelP.getHeight()) / 3 - 10);

	totalLV.setText(parent.getOptions().getPrefix()
		+ Number.round2Dp(elm.getTotal())
		+ parent.getOptions().getSuffix());
	usedLV.setText(parent.getOptions().getPrefix()
		+ Number.round2Dp(elm.getAmountSpentOn())
		+ parent.getOptions().getSuffix());
	leftLV.setText(parent.getOptions().getPrefix()
		+ Number.round2Dp(elm.getTotal() - elm.getAmountSpentOn())
		+ parent.getOptions().getSuffix());

	labelP.doLayout();
	buttonsP.doLayout();

    }

    @Override
    public void stateChanged(ChangeEvent e) {
	handler.stateChanged();
	if (e.getSource().equals(Element.TOTAL)) {
	    bar.setMax(elm.getTotal());
	    update();
	} else if (e.getSource().equals(Element.SPENT)) {
	    update();
	} else if (e.getSource().equals(Element.NAME)) {
	    setBorder(BorderFactory.createTitledBorder(new LineBorder(
		    new Color(190, 190, 255)), elm.getName(),
		    TitledBorder.LEFT, TitledBorder.TOP));
	}else if (e.getSource().equals(Element.PROPETRY)){
		bar.setUseMid(elm.isShowProgress());
		
		bar.setTarget(bar.getMax()*Number.getPecInMonth());
	}

	// update();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(add)) {
	    handler.add();
	} else if (e.getSource().equals(edit)) {
	    handler.edit();
	}
	if (e.getSource().equals(view)) {
	    handler.view();
	}

    }

}
