package parts.elementSaves;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import parts.Element;
import parts.Spent;

import util.saves.LoadException;
import util.saves.Saver;

public class ElmSaver1_3 implements Saver<Element> {

	@Override
	public String getVersion() {
		return "1.3";
	}

	@Override
	public void save(OutputStream out, Element elm) {
		
		int priority =elm.getPriority() ;
		int type =elm.getType();
		double total =elm.getTotal();
		boolean carryOver =elm.isCarryOver();
		boolean percent = elm.isPercent();
		double amount = elm.getAmount();
		Spent[] amounts = elm.getSpent();
		
		String name = elm.getName();
		
		PrintWriter writer = new PrintWriter(out);
		writer.println(name);
		writer.println("" + priority);
		writer.println("" + type);
		// SpentOn is recalculated on load, not saved
		writer.println("" + total);
		writer.println("" + carryOver);
		writer.println("" + percent);
		writer.println("" + amount);
		writer.println("" + amounts.length);
		writer.println();
		writer.flush();
		for (Spent s : amounts) {
		    s.save(out);
		}
		
	}

	@Override
	public Element load(BufferedReader in) throws LoadException{
		try{
		
		String name = in.readLine();
		
		int priority ;
		int type ;
		double total ;
		boolean carryOver ;
		boolean percent;
		double amount;
		int elmCnt;
		
		try{
		priority = Integer.parseInt(in.readLine());
		type = Integer.parseInt(in.readLine());
		total = Double.parseDouble(in.readLine());
		carryOver = Boolean.parseBoolean(in.readLine());
		percent = Boolean.parseBoolean(in.readLine());
		amount = Double.parseDouble(in.readLine());
		elmCnt = Integer.parseInt(in.readLine());
		}catch(NumberFormatException e){
			throw new LoadException(this,e.getMessage());
		}
		String blankLine = in.readLine();
		if(blankLine==null || !blankLine.equals("")){
			throw new LoadException(this,"Invalid file format. Expected empty line.");
		}

		Element elm = new Element(name, priority, type, carryOver, percent,
			amount);
		elm.setTotal(total);

		for (int i = 0; i < elmCnt; i++) {
		    Spent tmp = Spent.load(in);
		    elm.addSpent(tmp);
		}

		return elm;
		}catch(IOException e){
		throw new LoadException(this,e.getMessage());
		}
	}
	
	public String toString(){
		return "Element saver 1.3";
	}

}
