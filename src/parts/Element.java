package parts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import parts.elementSaves.ElmSaverFactory;
import util.saves.LoadException;

import GUI.dialogs.DialogConsts;
import GUI.dialogs.NewSpent;
import core.Main;

public class Element {

	public static final Integer TOTAL = new Integer(1);
	public static final Integer SPENT = new Integer(2);
	public static final Integer NAME = new Integer(3);
	public static final Integer PROPETRY = new Integer(4);

	private ChangeListener listener;

	protected ArrayList<Spent> amounts = new ArrayList<Spent>();

	/**
	 * This is the version that this element was loaded at.
	 * This is used by the saver factory to save at the right level.
	 */
	protected String loadVersion;

	/**
	 * The name of this element.<BR>
	 * This is displayed to the user.<BR>
	 */
	protected String name;
	/**
	 * The priority of this account.<BR>
	 * Lower numbers represent a higher priority, so 0 is highest while 100 is
	 * lower.<br>
	 * The user has direct access to this variable.<BR>
	 * The higher priorities (lower values) are processed before lower
	 * priorities when allocating amounts of money.<br>
	 * Savings accounts have this value set to -1, which is an invalid value, as
	 * they are always processed last.
	 */
	protected int priority;
	/**
	 * What type this element is.<br>
	 * can take one of:
	 * <UL>
	 * <LI>PartConsts.EXPENCE for an expense account</LI>
	 * <LI>PartConsts.ALLOWANCE for an allowance account</LI>
	 * <LI>PartConsts.SAVINGS for a savings account</LI>
	 * </UL>
	 */
	protected int type;

	/**
	 * how much has been used on this element:<br>
	 * Should be equal to the sum of the amounts of the spent items
	 */
	protected double spentOn;
	/**
	 * The total amount that can be spent on this element
	 */
	protected double total;

	/**
	 * For Expenses and allowances:<BR>
	 * Does this element carry over its expenses from month to month<BR>
	 * True: it does, False: it does not.<BR>
	 * <BR>
	 * For Savings Accounts:<BR>
	 * This var. is used to set the main savings account.<BR>
	 * only one savings account can be the main one at a time<BR>
	 * The core.Main class should be used for changing the main savings account<BR>
	 * Savings accounts are not reset from month to month.<BR>
	 */
	protected boolean carryOver;
	/**
	 * is it percentage based (true), or amount based (false)
	 */
	protected boolean percent;
	/**
	 * if percent == true, then this is a value between 0 and 1, representing
	 * the percent. if carryover == true, then this represents the amount to add
	 * each month Otherwise it is an amount of currency, that is, the same as
	 * total.
	 */
	protected double amount;
	
	/**
	 * is the value representing whether to show progress or not.
	 * if this is true a progress bar will be imposed on the element showing how much should have been spent so far.
	 */
	protected boolean showProgress;

	public Element(String name, int priority, int type, boolean carryOver,
			boolean percent, double amount,boolean showProgress) {
		super();
		this.name = name;
		this.priority = priority;
		this.type = type;
		this.carryOver = carryOver;
		this.percent = percent;
		this.amount = amount;
		this.showProgress = showProgress;
		
		spentOn = 0.0;
		total = 0.0;
		
		loadVersion =ElmSaverFactory.getInstance().getLatest();
		
	}
	
	public Element(String name, int priority, int type, boolean carryOver,
			boolean percent, double amount) {
		this(name,priority,type,carryOver,percent,amount,false);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!this.name.endsWith(name)) {
			this.name = name;
			ChangeEvent e = new ChangeEvent(NAME);
			fire(e);
		}
	}

	public void addSpentByDialog(Main main) {
		NewSpent sd = new NewSpent(main.getFrame());
		sd.setVisible(true);
		if (sd.getOption() == DialogConsts.OK) {
			Spent tmp = new Spent(sd.amount, sd.desc, sd.date);
			addSpent(tmp);
		}
	}

	public void editSpentByDialog(Main parent,Spent sp){
		if(sp==null){
			return;
		}

		int ind = amounts.indexOf(sp);
		if(ind<0){
			return;
		}

		NewSpent sd = new NewSpent(parent.getFrame(), sp);
		sd.setVisible(true);
		if (sd.getOption() == DialogConsts.OK) {
			Spent tmp = new Spent(sd.amount, sd.desc, sd.date);
			amounts.set(ind, tmp);
		}

	}



	/*
	 * private void setAmountSpentOn(double amount) { this.spentOn = amount;
	 * ChangeEvent e = new ChangeEvent(SPENT); fire(e); }
	 */

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void addSpent(Spent in) {
		in.setCatagory(this);
		amounts.add(in);
		spentOn += in.getAmount();
		ChangeEvent e = new ChangeEvent(SPENT);
		fire(e);
	}

	public void removeSpent(Spent s) {
		boolean test = amounts.remove(s);

		if (test) {
			spentOn -= s.getAmount();

			ChangeEvent e = new ChangeEvent(SPENT);
			fire(e);
		}
	}

	public void clearSpent() {
		amounts.clear();

		spentOn = 0;

		ChangeEvent e = new ChangeEvent(SPENT);
		fire(e);

	}

	public void setCarryOver(boolean carryOver) {
		this.carryOver = carryOver;
	}

	public void setPercent(boolean percent) {
		this.percent = percent;
	}

	public boolean isCarryOver() {
		return carryOver;
	}

	public boolean isPercent() {
		return percent;
	}

	public Spent[] getSpent() {
		return amounts.toArray(new Spent[0]);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public double getAmountSpentOn() {
		return spentOn;
	}

	/*
	 * public void setSpentOn(double in){ spentOn=in; }
	 */

	public double getTotal() {
		return total;
	}

	public void setTotal(double amount) {
		if (this.total != amount) {
			this.total = amount;
			ChangeEvent e = new ChangeEvent(TOTAL);
			fire(e);
		}

	}

	public double getInitTotal(double income) {
		if (percent) {
			return income * amount;
		} else {
			return amount;
		}
	}

	public int getType() {
		return type;
	}
	
	public String getLoadVersion() {
		return loadVersion;
	}

	public void setLoadVersion(String loadVersion) {
		this.loadVersion = loadVersion;
	}
	
	public boolean isShowProgress() {
		return showProgress;
	}

	public void setShowProgress(boolean showProgress) {
		if(this.showProgress!= showProgress){
		this.showProgress = showProgress;
		ChangeEvent e = new ChangeEvent(PROPETRY);
		fire(e);
		}
	}

	public ChangeListener getListener() {
		return listener;
	}

	public void setListener(ChangeListener listener) {
		this.listener = listener;
	}

	private void fire(final ChangeEvent e) {
		if (listener == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				listener.stateChanged(e);
			}
		}).start();
	}

	/**
	 * Writes this elements data to the given output stream.<br>
	 * This flushes but does not close the stream when it is finished.
	 * 
	 * @param out
	 *            The output stream to write this elements data to.
	 */
	public void save(OutputStream out) {
		
		ElmSaverFactory.getInstance().saveByDialog(out, this, null);
		
		/*PrintWriter writer = new PrintWriter(out);
		writer.println(name);
		writer.println("" + priority);
		writer.println("" + type);
		// SpentOn is recalculated on load, not saved
		writer.println("" + total);
		writer.println("" + carryOver);
		writer.println("" + percent);
		writer.println("" + amount);
		writer.println("" + amounts.size());
		writer.println();
		writer.flush();
		for (Spent s : amounts) {
			s.save(out);
		}*/

	}

	/**
	 * Creates an element by reading its data from the giver input stream<BR>
	 * Also creates its spent on objects.<br>
	 * This method will move the stream's pointer to after this elemnts data.<br>
	 * Does not close the stream.
	 * 
	 * @param in
	 *            The input stream to read this elements data from.
	 * @return The element read from the stream.
	 * @throws IOException
	 */
	public static Element load(InputStream in) throws IOException {

		try{
			return ElmSaverFactory.getInstance().load(in);
		}catch(LoadException ex){
			JOptionPane.showMessageDialog(null, "Error loading: "+ex.getMessage(),"Load Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return null;
		}
		/*
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String name = reader.readLine();
		int priority = Integer.parseInt(reader.readLine());
		int type = Integer.parseInt(reader.readLine());
		double total = Double.parseDouble(reader.readLine());
		boolean carryOver = Boolean.parseBoolean(reader.readLine());
		boolean percent = Boolean.parseBoolean(reader.readLine());
		double amount = Double.parseDouble(reader.readLine());
		int elmCnt = Integer.parseInt(reader.readLine());
		reader.readLine();

		Element elm = new Element(name, priority, type, carryOver, percent,
				amount);
		elm.setTotal(total);

		for (int i = 0; i < elmCnt; i++) {
			Spent tmp = Spent.load(reader);
			elm.addSpent(tmp);
		}

		return elm;*/
	}

}
