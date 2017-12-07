package GUI.renderers.parts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import GUI.handlers.SavingsHandler;
import GUI.renderers.Renderer;
import GUI.util.Area;
import core.Main;

public class SavingsRenderer extends Renderer implements ActionListener,
	ChangeListener {

    // The colorful progress bar
    private Area negB;
    private Area negF;

    private Area posB;
    private Area posF;

    private Area zeroA;
    private Area targetA;

    private Area supB;
    private Area supF;
    // The panel for the bar pieces to sit on
    private JPanel bar;

    // The Labels that describe the amount of money in the account
    private JLabel targetL;
    private JLabel cTargetL;
    private JLabel currentL;
    private JLabel cAmmountL;
    // The panel for the descriptions to sit on
    private JPanel amountP;

    // The control buttons
    private JButton add;
    private JButton edit;
    private JButton view;
    // the panel for the buttons
    private JPanel buttonsP;

    private SavingsHandler handler;

    public SavingsRenderer(Element elm, Main main) {
	super(elm, main);
	elm.setListener(this);
	setLayout(new BorderLayout(2,0));

	setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(
		190, 190, 255)), elm.getName(), TitledBorder.LEFT,
		TitledBorder.TOP,TITLE_FONT));
	ini();

    }

    public void ini() {
	handler = new SavingsHandler(parent, elm);

	initLabels();

	intiBar();

	initButtons();

	update();
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

    private void intiBar() {
	bar = new JPanel();
	bar.setPreferredSize(new Dimension(getWidth(), 25));
	bar.setLayout(null);

	negB = new Area(Color.LIGHT_GRAY, Color.GRAY, Area.Vertical, null);
	bar.add(negB);

	negF = new Area(new Color(250, 50, 50), new Color(170, 0, 0),
		Area.Vertical, new LineBorder(Color.GRAY));
	bar.add(negF);

	posB = new Area(Color.LIGHT_GRAY, Color.GRAY, Area.Vertical, null);
	bar.add(posB);

	posF = new Area(new Color(200, 250, 10), new Color(100, 150, 0),
		Area.Vertical, new LineBorder(Color.GRAY));
	bar.add(posF);

	supB = new Area(Color.LIGHT_GRAY, Color.GRAY, Area.Vertical, null);
	bar.add(supB);

	supF = new Area(new Color(100, 250, 10), new Color(50, 175, 0),
		Area.Vertical, new LineBorder(Color.GRAY));
	bar.add(supF);

	zeroA = new Area(Color.DARK_GRAY, Color.BLACK, Area.Vertical, null);
	zeroA.setSize(5, 25);
	bar.add(zeroA);

	targetA = new Area(Color.CYAN.darker(), Color.BLUE, Area.Vertical, null);
	targetA.setSize(5, 25);
	bar.add(targetA);
	add(bar, BorderLayout.CENTER);

    }

    private void initLabels() {
	amountP = new JPanel();
	amountP.setPreferredSize(new Dimension(getWidth(), 60));
	amountP.setLayout(null);

	targetL = new JLabel("Target:");
	targetL.setFont(CONTENT_FONT);
	amountP.add(targetL);

	cTargetL = new JLabel(parent.getOptions().getPrefix() + "0.00"
		+ parent.getOptions().getSuffix());
	cTargetL.setFont(CONTENT_FONT);
	amountP.add(cTargetL);

	currentL = new JLabel("Current:");
	currentL.setFont(CONTENT_FONT);
	amountP.add(currentL);

	cAmmountL = new JLabel(parent.getOptions().getPrefix() + "0.00"
		+ parent.getOptions().getSuffix());
	cAmmountL.setFont(CONTENT_FONT);
	amountP.add(cAmmountL);

	add(amountP, BorderLayout.NORTH);
    }

    @Override
    public void update() {
	int width = bar.getWidth();
	int negPos = bar.getWidth() / 4;
	int targPos = 3 * negPos;

	double amount = elm.getAmountSpentOn();
	if (amount < 0) {
	    int exp = 1;
	    amount = -1 * amount;
	    while ((amount / 10) >= Math.pow(10, exp - 1)) {
		exp++;
	    }
	    if (amount / Math.pow(10, exp - 1) >= 9) {
		exp++;
	    }

	    double negBound = Math.pow(10, exp);

	    double total_Size = elm.getTotal() * 1.2 + negBound;

	    int negWidth = (int) Math.round((negBound / total_Size)
		    * (width - zeroA.getWidth() - targetA.getWidth()));
	    negPos = negWidth;
	    int posWidth = (int) Math
		    .round(((elm.getTotal() / total_Size) * (width
			    - zeroA.getWidth() - targetA.getWidth())));
	    targPos = negPos + posWidth + zeroA.getWidth();
	    int supWidth = (int) Math.round(((elm.getTotal() / 5) / total_Size)
		    * (width - zeroA.getWidth() - targetA.getWidth()));

	    int tmpX = (int) Math.round(((negBound - amount) / negBound)
		    * negWidth);
	    int tmpW = (int) Math.round((amount / negBound) * negWidth);

	    negB.setBounds(0, 0, tmpX, 25);
	    negF.setBounds(tmpX, 0, tmpW, 25);
	    zeroA.setLocation(negPos, 0);
	    posF.setBounds(0, 0, 0, 0);
	    posB.setBounds(negPos + zeroA.getWidth(), 0, posWidth, 25);
	    targetA.setLocation(targPos, 0);
	    supF.setBounds(0, 0, 0, 0);
	    supB.setBounds(targPos + targetA.getWidth(), 0, supWidth, 25);

	} else if (amount > elm.getTotal()) {
	    int exp = 1;
	    while (((amount - elm.getTotal()) / 10) >= Math.pow(10, exp - 1)) {
		exp++;
	    }
	    if ((amount - elm.getTotal()) / Math.pow(10, exp - 1) >= 9) {
		exp++;
	    }

	    double supBound = Math.pow(10, exp);

	    double total_Size = elm.getTotal() * 1.2 + supBound;

	    int negWidth = (int) Math.round(((elm.getTotal() / 5) / total_Size)
		    * (width - zeroA.getWidth() - targetA.getWidth()));
	    negPos = negWidth;
	    int posWidth = (int) Math
		    .round(((elm.getTotal() / total_Size) * (width
			    - zeroA.getWidth() - targetA.getWidth())));
	    targPos = negPos + posWidth + zeroA.getWidth();
	    int supWidth = (int) Math.round((supBound / total_Size)
		    * (width - zeroA.getWidth() - targetA.getWidth()));

	    int tmpW = (int) Math.round(((amount - elm.getTotal()) / supBound)
		    * supWidth);

	    negB.setBounds(0, 0, negPos, 25);
	    negF.setBounds(0, 0, 0, 0);
	    zeroA.setLocation(negPos, 0);
	    posB.setBounds(0, 0, 0, 0);
	    posF.setBounds(negPos + zeroA.getWidth(), 0, posWidth, 25);
	    targetA.setLocation(targPos, 0);
	    supF.setBounds(targPos + targetA.getWidth(), 0, tmpW, 25);
	    supB.setBounds(targPos + targetA.getWidth() + tmpW, 0, supWidth
		    - tmpW, 25);
	} else {

	    double total_Size = elm.getTotal() * 1.4;

	    int negWidth = (int) Math.round(((elm.getTotal() / 5) / total_Size)
		    * (width - zeroA.getWidth() - targetA.getWidth()));
	    negPos = negWidth;
	    int posWidth = (int) Math
		    .round(((elm.getTotal() / total_Size) * (width
			    - zeroA.getWidth() - targetA.getWidth())));
	    targPos = negPos + posWidth + zeroA.getWidth();
	    int supWidth = negWidth;

	    int tmpW = (int) Math.round(((amount) / elm.getTotal()) * posWidth);

	    negB.setBounds(0, 0, negPos, 25);
	    negF.setBounds(0, 0, 0, 0);
	    zeroA.setLocation(negPos, 0);
	    posF.setBounds(negPos + zeroA.getWidth(), 0, tmpW, 25);
	    posB.setBounds(negPos + zeroA.getWidth() + tmpW, 0,
		    posWidth - tmpW, 25);
	    targetA.setLocation(targPos, 0);
	    supF.setBounds(0, 0, 0, 0);
	    supB.setBounds(targPos + targetA.getWidth(), 0, supWidth, 25);
	}

	amountP.setPreferredSize(new Dimension(getWidth(), 60));

	targetL.setBounds(0, 0, amountP.getWidth() / 2,
		amountP.getHeight() / 2 - 5);
	cTargetL.setBounds(amountP.getWidth() / 2, 0, amountP.getWidth() / 2,
		amountP.getHeight() / 2 - 5);
	currentL.setBounds(0, amountP.getHeight() / 2, amountP.getWidth() / 2,
		amountP.getHeight() / 2 - 5);
	cAmmountL.setBounds(amountP.getWidth() / 2, amountP.getHeight() / 2,
		amountP.getWidth() / 2, amountP.getHeight() / 2 - 5);

	cTargetL.setText(parent.getOptions().getPrefix()
		+ Number.round2Dp(elm.getTotal())
		+ parent.getOptions().getSuffix());
	cAmmountL.setText(parent.getOptions().getPrefix()
		+ Number.round2Dp(elm.getAmountSpentOn())
		+ parent.getOptions().getSuffix());

	Color c;
	if (elm.getAmountSpentOn() < 0)
	    c = Color.RED;
	else if (elm.getAmountSpentOn() > elm.getTotal())
	    c = Color.GREEN.darker();
	else
	    c = Color.BLACK;

	cAmmountL.setForeground(c);

	buttonsP.doLayout();
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

    @Override
    public void stateChanged(ChangeEvent e) {
	handler.stateChanged();

	if (e.getSource().equals(Element.SPENT)) {
	    update();
	} else if (e.getSource().equals(Element.TOTAL)) {
	    update();
	}
	if (e.getSource().equals(Element.NAME)) {
	    setBorder(BorderFactory.createTitledBorder(new LineBorder(
		    new Color(190, 190, 255)), elm.getName(),
		    TitledBorder.LEFT, TitledBorder.TOP));
	}

    }
}
