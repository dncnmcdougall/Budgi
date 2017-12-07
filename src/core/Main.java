package core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import parts.Element;
import parts.PartConsts;
import parts.Spent;
import util.ZipFileUtil;
import GUI.Frame;
import GUI.renderers.Renderer;
import GUI.renderers.RendererFactory;

public class Main implements PartConsts {

	private Options o;
	private Config conf;
	private Frame f;
	private ArrayList<Element> elements = new ArrayList<Element>();
	private Element mainSav;
	// Has anything changes since last save/load?
	private boolean changed = false;

	public Main() {
		super();

		o = new Options();
		conf = new Config();
		f = new Frame(this);
		if(!load()){
			close();
		}
		f.setTitle("Budget "+conf.getVersion());
		f.setIconImage(new ImageIcon("./Budgi_ico.png").getImage());
		f.setLocation(100, 100);
		f.setVisible(true);
		changed = false;
	}

	public void removeElement(Element e) {
		if(mainSav != null && e == mainSav){
			mainSav=null;
		}
		elements.remove(e);
		f.remove(e);
		f.update();
		changed = true;
	}

	public void addElement(Element e) {
		elements.add(e);
		Renderer r = RendererFactory.createRenderer(e, this);
		f.add(r);
		f.update();
		changed = true;
	}

	public void clearElements() {
		int elmCnt = getElementCnt();
		for (int i = 0; i < elmCnt; i++) {
			Element tmp = elements.get(0);
			removeElement(tmp);
		}
		changed = true;
	}

	public Frame getFrame() {
		return f;
	}

	public Config getConf() {
		return conf;
	}

	public void setConf(Config conf) {
		this.conf = conf;
	}

	public Options getOptions() {
		return o;
	}

	public void setOptions(Options o) {
		this.o = o;
	}

	/**
	 * Closes the program
	 */
	public void close() {
		System.exit(0);
	}

	public Element getMainSav() {
		return mainSav;
	}

	public void setMainSav(Element mainSav) {
		this.mainSav = mainSav;
	}

	/**
	 * This method is used to 'Refresh' the accounts
	 */
	public void proccessElements(){
		proccessElements(o.getIncome());
	}
	
	private void proccessElements(double amount) {
		changed = true;
		if (elements.size() == 0) {
			JOptionPane.showMessageDialog(f,
					"There are no elements to process", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (mainSav == null) {
			JOptionPane
			.showMessageDialog(
					f,
					"There is no Main savings account. Please create one and try again",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int option = JOptionPane
				.showConfirmDialog(
						f,
						"You are about to add "
								+ o.getPrefix()
								+ amount
								+ o.getSuffix()
								+ " to you budget.\nAll accounts that do not carry over there expendeture will be reset.\n Continue?",
								"Refresh", JOptionPane.YES_NO_OPTION);
		if (option != JOptionPane.YES_OPTION) {
			return;
		}
		option = JOptionPane
				.showConfirmDialog(
						getFrame(),
						"Would you like to archive the current state before you continue?",
						"Archive?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			saveOld();
		}

		if (!checkExpences())
			return;

		sortElements();


		double index = 0;
		double amountLeft = amount;
		do {
			System.out.println("Entering : "
					+ elements.get((int) index).getPriority() + " with "
					+ o.getPrefix() + amountLeft + o.getSuffix());
			double[] out = processPriorityLevel(index, amountLeft);
			index = out[0];
			amountLeft = out[1];

		} while (amountLeft > 0);

		if (amountLeft != 0) {
			amountLeft = -amountLeft;
			Element sav = getMainSav();
			double tmpAmount = sav.getAmountSpentOn();
			sav.clearSpent();
			sav.addSpent(new Spent(tmpAmount, "Credit brought forward", getNow()));
			sav.addSpent(new Spent(amountLeft, "Surplus income carried over.",
					getNow()));
		}
	}

	/**
	 * check for unpaid expenses
	 * 
	 * @return true if there are none, or if they are to be ignored
	 */
	private boolean checkExpences() {
		ArrayList<Element> tmp = new ArrayList<Element>();

		for (Element e : elements) {
			if (e.getType() == PartConsts.EXPENCE) {
				if (e.getAmountSpentOn() < e.getTotal()) {
					tmp.add(e);
				}
			}
		}
		if (tmp.size() == 0)
			return true;

		String message = "The following expences are not fully paid up:\n";

		for (Element e : tmp) {
			message += spaces(e.getName(), 40) + "by " + o.getPrefix()
					+ (e.getTotal() - e.getAmountSpentOn()) + o.getSuffix()
					+ (e.isCarryOver() ? " (Carry Over)" : "") + "\n";
		}
		message += "Do you wish to continue, ignoring these?\n\n";
		message += "(Warning: If these are not carry over accounts they will be cleared)";

		int opt = JOptionPane.showConfirmDialog(f, message, "Unpaid expences",
				JOptionPane.YES_NO_OPTION);

		if (opt == JOptionPane.YES_OPTION)
			return true;

		return false;
	}

	public double[] processPriorityLevel(double start, double incomeAmount) {
		int strt = (int) start;

		if (elements.get(strt).getType() == SAVINGS) {
			return new double[] { start, -incomeAmount };
		}

		int priority = elements.get(strt).getPriority();
		int len = 0;
		while (elements.get(strt + len).getPriority() == priority) {
			len++;
		}

		double end = strt + len;// Selects the element after this priority
		// level.

		double totAmount = 0.0;
		for (int i = strt; i < end; i++) {
			if (elements.get(i).isCarryOver()) {
				if (elements.get(i).isPercent()) {
					totAmount += incomeAmount * elements.get(i).getAmount();
				} else {
					totAmount += elements.get(i).getAmount();
				}
			} else {
				double tmpLeft = elements.get(i).getTotal()
						- elements.get(i).getAmountSpentOn();
				if (elements.get(i).isPercent()) {
					double tmpTot = incomeAmount * elements.get(i).getAmount();
					totAmount += tmpTot - tmpLeft;
				} else {
					totAmount += elements.get(i).getTotal() - tmpLeft;
				}
			}
		}

		if (totAmount < incomeAmount) {
			double amountLeft = incomeAmount;

			for (int i = strt; i < end; i++) {
				if (elements.get(i).isPercent()) {
					double amount = elements.get(i).getAmount() * incomeAmount;
					if (elements.get(i).isCarryOver()) {
						elements.get(i).setTotal(
								elements.get(i).getTotal() + amount);
						amountLeft -= amount;
					} else {
						amountLeft -= (amount - (elements.get(i).getTotal() - elements
								.get(i).getAmountSpentOn()));
						elements.get(i).clearSpent();
						elements.get(i).setTotal(amount);
					}

				} else {
					if (elements.get(i).isCarryOver()) {
						elements.get(i).setTotal(
								elements.get(i).getTotal()
								+ elements.get(i).getAmount());
						amountLeft -= elements.get(i).getAmount();
					} else {
						amountLeft -= elements.get(i).getAmount()
								- (elements.get(i).getTotal() - elements.get(i)
										.getAmountSpentOn());
						elements.get(i).clearSpent();
						elements.get(i).setTotal(elements.get(i).getAmount());
					}

				}
			}

			if (Math.round(amountLeft*100) != Math.round((incomeAmount - totAmount)*100)) {
				JOptionPane
				.showMessageDialog(
						f,
						"Error: the calculated total amount required and the actual used did not match.",
						"Error in processing Priority",
						JOptionPane.ERROR_MESSAGE);
				throw new ArithmeticException(
						"Error in processPriorityLevel, with start = " + start
						+ " amountLeft = " + amountLeft+" calculated amount = "+(incomeAmount - totAmount)
						+ " in enough income sub routene");
			}
			return new double[] { end, amountLeft };
		} else {
			double mult = incomeAmount / totAmount;// A multiplier that
			// represents how much each
			// account gets.

			double amountLeft = incomeAmount;

			for (int i = strt; i < end; i++) {
				if (elements.get(i).isPercent()) {
					double amount = elements.get(i).getAmount() * incomeAmount;
					if (elements.get(i).isCarryOver()) {
						elements.get(i).setTotal(
								elements.get(i).getTotal() + amount * mult);
						amountLeft -= amount * mult;
					} else {
						amountLeft -= amount * mult;
						elements.get(i).clearSpent();
						elements
						.get(i)
						.addSpent(
								new Spent(
										amount * (1 - mult),
										"There was not enough income to fill this level.",
										getNow()));
						elements.get(i).setTotal(amount);
					}

				} else {
					if (elements.get(i).isCarryOver()) {
						elements.get(i).setTotal(
								elements.get(i).getTotal()
								+ elements.get(i).getAmount() * mult);
						amountLeft -= elements.get(i).getAmount() * mult;
					} else {
						elements.get(i).clearSpent();
						elements
						.get(i)
						.addSpent(
								new Spent(
										elements.get(i).getAmount()
										* (1 - mult),
										"There was not enough income to fill this level.",
										getNow()));
						elements.get(i).setTotal(elements.get(i).getAmount());
						amountLeft -= elements.get(i).getAmount() * mult;

					}

				}
			}

			if (!(amountLeft < 1e-12 && amountLeft > -1e-12)) {
				JOptionPane
				.showMessageDialog(
						f,
						"Error: the calculated total amount required and the actual used did not match.",
						"Error in processing Priority",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("Amount left: " + amountLeft + " calc: " + 0
						+ " mult: " + mult);
				throw new ArithmeticException(
						"Error in processPriorityLevel, with start = " + start
						+ " incomeAmount = " + incomeAmount
						+ " in Not enough income sub routene");
			}
			return new double[] { end, 0 };
		}

	}

	private Date getNow() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	private void sortElements() {
		for (int i = 0; i < elements.size() - 1; i++) {
			for (int j = i + 1; j < elements.size(); j++) {
				if ((elements.get(i).getPriority() > elements.get(j)
						.getPriority() && elements.get(j).getType() != PartConsts.SAVINGS)
						|| (elements.get(i).getType() == PartConsts.SAVINGS && elements
						.get(j).getType() != PartConsts.SAVINGS)) {

					Element tmp = elements.get(i);
					elements.set(i, elements.get(j));
					elements.set(j, tmp);
				}
			}
		}
	}

	public void flagForRefresh() {
		setChanged(true);
	}

	/**
	 * Makes a string of the specified length (or longer) from the original
	 * string by filling with spaces
	 * 
	 * @param in
	 *            -the base string
	 * @param count
	 *            -the total size to make it
	 * @return in+"  ..." so that the total length of the return value equals
	 *         count
	 */
	private String spaces(String in, int count) {
		while (in.length() < count)
			in += " ";
		return in;

	}

	/**
	 * A method used to save the current state to the filename provided. Returns
	 * true on success, false otherwise.
	 * 
	 * @param filename
	 *            The filename to save the current state in.
	 * @return success state
	 */
	private boolean save(String filename) {

		try {
			if (elements.size() == 0) {
				JOptionPane.showMessageDialog(getFrame(),
						"WARNING: no elemts to save, not saving", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return false;
			}
			File f = new File(conf.getSaveDir() +"/"+ filename).getParentFile();
			if (!f.exists()) {
				mkdir(f);
			}

			ZipFileUtil saveCurr = new ZipFileUtil(conf.getSaveDir()+"/" + filename);
			OutputStream tmpOut = saveCurr.getOutputStream("#!OPTIONS");
			o.save(tmpOut);
			saveCurr.finishWriting();

			for (Element elm : elements) {
				OutputStream out = saveCurr.getOutputStream(elm.getName());
				elm.save(out);
				saveCurr.finishWriting();
			}
			saveCurr.closeOut();
			saveCurr = null;
		} catch (IOException e) {
			// e.printStackTrace();
			JOptionPane.showMessageDialog(getFrame(),
					"An error has occured while trying to save.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;

	}

	private void mkdir(File f) {
		if (!f.mkdir()) {
			mkdir(f.getParentFile());
			f.mkdir();
		}
	}

	public void save() {
		changed = !save("Current.zip");
	}

	public void saveOld() {
		Calendar calendar = Calendar.getInstance();
		save("Old/" + calendar.get(Calendar.DAY_OF_MONTH) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.YEAR) + " "
				+ calendar.get(Calendar.HOUR_OF_DAY) + ","
				+ calendar.get(Calendar.MINUTE) + ","
				+ calendar.get(Calendar.SECOND) + ".zip");
	}

	public int getElementCnt() {
		return elements.size();
	}

	public double getTotalBalance() {
		double out = 0;
		for (Element e : elements) {
			switch (e.getType()) {
			case PartConsts.SAVINGS:
				out += e.getAmountSpentOn();
				break;
			case PartConsts.ALLOWANCE:
			case PartConsts.EXPENCE:
				out += (e.getTotal() - e.getAmountSpentOn());
				break;
			}
		}
		return out;
	}

	/**
	 * This method runs the basic loading procedure for this program.
	 * It first looks for and then loads the config file then loads the current save file.
	 * 
	 * @return true on success, false on failure.
	 */
	public boolean load() {
		if(!loadConfig()){
			return false;
		}
		loadCurrent(conf.getSaveDir().toString());
		return true;
	}
	/**
	 * This method attempts to load the config file. If it succeeds return true, otherwise it returns false.
	 * 
	 * @return success state, true on success and false on failure.
	 */
	public boolean loadConfig(){
		File f= new File("./Saves/Config.conf");
		if(!f.exists()){
			int result = JOptionPane.showConfirmDialog(null, "The config file could not be found, would you like to create it now?\n"
					+"(Can not continue without it)","Warning",JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.NO_OPTION){
				return false;
			}

			f=f.getAbsoluteFile();
			String s= f.toString();
			s=s.replace("./", "");
			f= new File(s);

			if(!f.getParentFile().exists()){
				f.getParentFile().mkdir();
			}

			String dir =JOptionPane.showInputDialog("Please enter the directory where you want the save files to be saved.\n"+
					"(The default is saved)",f.getParentFile());

			System.out.println(dir);

			conf.setSaveDir(new File(dir));
			conf.create();


			return true;
		}

		return conf.load();

	}
	/**
	 * A method used to load the current file "Current.zip" into the program.
	 * Returns true on success, false otherwise.
	 * 
	 * @param saveDir
	 *            The string of the directory to load from
	 * @param name
	 *            The name of the file to load from
	 * @param elms
	 *            The ArrayList into which to load the elements.
	 * @param outputs
	 * 		  This is an ArrayList containing the options and the main savings element of this save.<BR>
	 * 		  So that:<BR>
	 * 				outputs[0] = options<BR>
	 * 		  		outputs[1] = Main Savings.
	 * 
	 * @return success state
	 */
	public boolean load(String name, String saveDir, ArrayList<Element> elms,ArrayList<Object> outputs) {
		try {
			File f = new File(saveDir + "/" + name + ".zip");
			if (!f.exists()) {
				JOptionPane.showMessageDialog(getFrame(),
						"Warning: Could not find the current save file ("
								+ saveDir + "/" + name + ")\n"
								+ "Continuing without it.", "Warning",
								JOptionPane.WARNING_MESSAGE);
				return false;
			}
			ZipFile currZip = new ZipFile(f);
			ZipFileUtil loadCurr = new ZipFileUtil(currZip);

			InputStream tmpIs = loadCurr.getInputStream("#!OPTIONS");
			if(outputs!=null){
				outputs.set(0, Options.load(tmpIs));
			}
			tmpIs.close();
			ArrayList<InputStream> streams = loadCurr
					.getInputStreams(new FilenameFilter() {

						@Override
						public boolean accept(File f, String s) {
							return !s.startsWith("#!OPTIONS");
						}
					});

			for (InputStream cStream : streams) {
				Element e = Element.load(cStream);
				if (e.isCarryOver() && e.getType()==SAVINGS) {
					if(outputs!=null){
						outputs.set(1, e);
					}
				}

				elms.add(e);
				cStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane
			.showMessageDialog(
					getFrame(),
					"An error has occured while trying to load the currrent file.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * A method used to load the current file "Current.zip" into the program.
	 * Returns true on success, false otherwise.
	 * 
	 * @param saveDir
	 *            The string of the directory to load from
	 * 
	 * @return success state
	 */
	public boolean loadCurrent(String saveDir) {
		ArrayList<Element> tmp = new ArrayList<Element>();
		Element mainSave=null;

		ArrayList<Object> outs = new ArrayList<Object>();
		outs.add(null);
		outs.add(null);

		boolean out = load("Current", saveDir, tmp,outs);


		if (out) {
			setOptions((Options)outs.get(0));
			this.f.setSize(o.getFrameSize());

			for (Element e : tmp) {
				addElement(e);
			}

			mainSave=(Element)outs.get(1);
			setMainSav(mainSave);

		}

		return out;

	}

	/**
	 * A method for setting whether or not the current state has changed since
	 * last save.
	 * 
	 * @param in
	 *            The state
	 */
	public void setChanged(boolean in) {
		this.changed = in;
	}

	/**
	 * A method for checking whether or not the current state has changed since
	 * last save.
	 * 
	 * @param in
	 *            The state
	 * @return whether (true) or not (false) the current state has changed
	 */
	public boolean isChanged() {
		return changed;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();

	}

}
