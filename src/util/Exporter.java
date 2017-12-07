package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import parts.Element;
import core.Main;

public class Exporter {

	private static Calendar getCal(File archive){
		String name=getDate(archive);
		String[] parts = name.split(" ");
		String[] tmp = parts[0].split("-");
		int day = Integer.parseInt(tmp[0]);
		int month = Integer.parseInt(tmp[1])-1;
		int year = Integer.parseInt(tmp[2]);
		int hr=12;
		int min=00;
		int sec=00;
		if(parts.length>1){
			tmp=parts[1].split(",");
			hr=Integer.parseInt(tmp[0]);
			min=Integer.parseInt(tmp[1]);
			sec=Integer.parseInt(tmp[2]);
		}
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hr, min,sec);
		return cal;
	}

	private static String getDate(File archive){
		String name = archive.getName();
		name=name.substring(0,name.indexOf('.'));
		return name;
	}

	public static int export(Main main){
		//List All the archives
		File[] files=new File(main.getConf().getSaveDir().getAbsoluteFile()+"/Old/").listFiles();


		//Sort the archives, with earliest first.
		for(int i=0;i<files.length-1;i++){
			int min=i;
			Calendar minC= getCal(files[i]);
			for(int j=i;j<files.length;j++){
				Calendar tmpC=getCal(files[j]);
				if(tmpC.before(minC)){
					min=j;
					minC=tmpC;
				}
			}
			File tmp=files[i];
			files[i]=files[min];
			files[min]=tmp;
		}


		//Generate the data to be written to file
		String left="";
		String percLeft="";	//Percentage Left
		String used="";
		String percUsed="";	//Percentage Used
		String total="";

		ArrayList<String> titles = new ArrayList<String>();

		for(int i=0;i<files.length;i++){
			ArrayList<Element> elms = new ArrayList<Element>();
			String name=getDate(files[i]);
			if(!main.load( name, files[i].getParentFile().getAbsolutePath(),elms, null)){
				continue;
			}
			
			Calendar tmpCal=getCal(files[i]);
			name=tmpCal.get(Calendar.YEAR)+"/"+(tmpCal.get(Calendar.MONTH)+1)+"/"+tmpCal.get(Calendar.DAY_OF_MONTH)+" "
					+tmpCal.get(Calendar.HOUR_OF_DAY)+":"+tmpCal.get(Calendar.MINUTE)+":"+tmpCal.get(Calendar.SECOND);
			left += 	name+",";
			percLeft += name+",";
			used += 	name+",";
			percUsed += name+",";
			total += 	name+",";

			ArrayList<ElementRep> elmReps=new ArrayList<ElementRep>();

			for(Element elm : elms){
				elmReps.add(new ElementRep(elm));
			}


			for(String s : titles){
				double tmpL=0;
				double tmpPL=0;
				double tmpU=0;
				double tmpPU=0;
				double tmpT=0;
				for(ElementRep elmRep: elmReps){
					if(elmRep.getName().equals(s) && !elmRep.isUsed()){
						elmRep.setUsed(true);
						tmpL=elmRep.getLeft();
						tmpPL=elmRep.getPercLeft();
						tmpU=elmRep.getSpentOn();
						tmpPU=elmRep.getPercUsed();
						tmpT=elmRep.getTotal();
						break;
					}
				}
				left += 	tmpL+",";
				percLeft += 	tmpPL+",";
				used += 	tmpU+",";
				percUsed += 	tmpPU+",";
				total += 	tmpT+",";
			}

			for(ElementRep elmRep : elmReps){
				if(!elmRep.isUsed()){
					titles.add(elmRep.getName());
					left +=	elmRep.getLeft()+",";
					percLeft +=	elmRep.getPercLeft()+",";
					used +=	elmRep.getSpentOn()+",";
					percUsed +=	elmRep.getPercUsed()+",";
					total +=	elmRep.getTotal()+",";
					elmRep.setUsed(true);
				}
			}
			left += 	"\r\n";
			percLeft +=	"\r\n";
			used += 	"\r\n";
			percUsed += "\r\n";
			total += 	"\r\n";


		}

		String title=",";
		for(String s: titles){
			title += s+",";
		}

		JFileChooser fChooser=new JFileChooser();

		fChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return ".csv";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".csv") || f.isDirectory();
			}
		});

		int result=fChooser.showSaveDialog(main.getFrame());
		if(result!=JFileChooser.APPROVE_OPTION){
			return 1;
		}
		File f=fChooser.getSelectedFile();
		if(!f.getAbsolutePath().toUpperCase().endsWith(".CSV")){
			f=new File(f.getAbsolutePath()+".csv");
		}
		PrintWriter out=null;
		try {
			out = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 2;
		}



		out.println("Totals,");
		out.println(title);
		out.println(total);
		out.println();

		out.println("Used,");
		out.println(title);
		out.println(used);
		out.println();

		out.println("Percentage Used,");
		out.println(title);
		out.println(percUsed);
		out.println();

		out.println("Left;");
		out.println(title);
		out.println(left);
		out.println();

		out.println("Percentage Left,");
		out.println(title);
		out.println(percLeft);
		out.println();

		out.flush();
		out.close();

		return 0;
	}

}
class ElementRep{

	private Element elm;
	private boolean used;

	public ElementRep(Element elm) {
		super();
		this.elm = elm;
		this.used = false;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Element getElm() {
		return elm;
	}

	public String getName(){
		return elm.getName();
	}

	public double getLeft(){
		return elm.getTotal()-elm.getAmountSpentOn();
	}

	public double getPercLeft(){
		return 100*(getLeft()/getTotal());
	}

	public double getTotal(){
		return elm.getTotal();
	}

	public double getSpentOn(){
		return elm.getAmountSpentOn();
	}

	public double getPercUsed(){
		return 100*(getSpentOn()/getTotal());
	}

}
