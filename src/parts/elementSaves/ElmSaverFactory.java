package parts.elementSaves;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import parts.Element;

import util.saves.LoadException;
import util.saves.SaveException;
import util.saves.Saver;

public class ElmSaverFactory {

	private static ElmSaverFactory instance = null;

	private ArrayList<Saver<Element>> savers;

	private ElmSaverFactory(){
		savers = new ArrayList<Saver<Element>>();

		savers.add(new ElmSaver1_3());
		savers.add(new ElmSaver1_4());
		sort();
	}

	public static ElmSaverFactory getInstance(){
		if(instance == null)
		{
			instance = new ElmSaverFactory();
		}
		return instance;
	}

	private void sort(){
		for(int i=0;i<savers.size()-1;i++){
			for(int j=i+1;j<savers.size();j++){
				if(savers.get(i).getVersion().compareTo(savers.get(j).getVersion()) <0){
					Saver<Element> tmp  = savers.get(i);
					savers.set(i,savers.get(j));
					savers.set(j, tmp);
				}
			}
		}
	}

	public Element load(InputStream in) throws LoadException{
		Element ret=null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		for(int i=0;i<savers.size();i++){
			try{


				reader.mark(5120);//5MB Limit for save files
				ret = savers.get(i).load(reader);
				ret.setLoadVersion(savers.get(i).getVersion()); 
				return ret;
			}catch(LoadException ex){
				try {
					reader.reset();
				} catch (IOException e) {
					throw new LoadException(null,"Input stream error: cannot reset position");
				}
			}catch(IOException e){
				throw new LoadException(null,"Input stream error: cannot mark position");
			}
		}

		if(ret == null){
			throw new LoadException(null,"Input stream error: No loads worked. The save file is invalid");
		}

		return ret; //We should never get here

	}

	public void saveByDialog(OutputStream out,Element elm,JComponent parent){
		if(isLatest(elm.getLoadVersion())){
			try{
				save(out,elm,savers.get(0).getVersion());
				return;
			}catch(SaveException ex){
				JOptionPane.showMessageDialog(parent, "Could not save","Save Error",JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}

		String[] opts = new String[]{"Same ("+elm.getLoadVersion()+")","Latest ("+savers.get(0).getVersion()+")"};

		String opt =(String)JOptionPane.showInputDialog(parent, elm.getName()+" was loaded at version: "+elm.getLoadVersion()+"\r\n"+
				"Would you like to save them at this version or at the latest version?",
				"Save Version",JOptionPane.QUESTION_MESSAGE,null,opts,opts[0]);		
		if(opt == null){
			JOptionPane.showMessageDialog(parent, "Save Canceled: saving at current level","Save Error",JOptionPane.ERROR_MESSAGE);
			opt=opts[0];
		}
		try{
			if(opt==opts[0]){
				save(out,elm,elm.getLoadVersion());
			}else{
				save(out,elm,savers.get(0).getVersion());
			}
		}catch(SaveException ex){
			JOptionPane.showMessageDialog(parent, "Could not save","Save Error",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public void save(OutputStream out,Element elm, String version) throws SaveException{
		
		//TODO savng que
		int ind =0;
		boolean found = false;
		
		for(ind=0;ind<savers.size();ind++){
			if(savers.get(ind).getVersion().equals(version)){
				found = true;
				break;
			}
		}
		if(!found){
			throw new SaveException("Could not find the saver specified");
		}
		elm.setLoadVersion(savers.get(ind).getVersion());
		savers.get(ind).save(out, elm);
	}

	public String getLatest(){
		return savers.get(0).getVersion();
	}
	
	private boolean isLatest(String version){
		return (getLatest().equals(version));
	}
	
}
