package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class Config {
	private File saveDir;
	private String version;

	public Config() {
		super();
		this.saveDir = null;
		this.version=null;
	}

	public File getSaveDir() {
		return saveDir;
	}

	public void setSaveDir(File saveDir) {
		this.saveDir = saveDir;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void create(){
		try{
			PrintWriter out = new PrintWriter(new File("./Saves/Config.conf"));
			if(version == null){
				version ="1.4.1";
			}
			out.println("Ver = "+version);
			String tmp=saveDir.getAbsolutePath();
			tmp=tmp.replace("./", "");
			out.println("SaveDir = "+tmp);
			out.flush();
			out.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean load(){
		try{

			BufferedReader in = new BufferedReader(new FileReader(new File("./Saves/Config.conf")));
			String buffer = in.readLine();

			while(buffer!=null){
				buffer=buffer.trim();
				if(buffer.startsWith("SaveDir")){
					String tmp=buffer;
					tmp=buffer.substring(buffer.indexOf("=")+1);
					tmp=tmp.trim();
					saveDir = new File(tmp);
				}else if(buffer.startsWith("Ver")){
					String tmp=buffer;
					tmp=buffer.substring(buffer.indexOf("=")+1);
					tmp=tmp.trim();
					version = tmp;
				}

				buffer = in.readLine();
			}

		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		checkVersion();
		return true;
	}

	private void checkVersion(){
		if(!version.equals("1.4.1")){
			version="1.4.1";
			int ret=JOptionPane.showConfirmDialog(null, "You are currently running an newer version of Budgi.\r\nWould you like to update your system now?\r\n" +
					"(This will modify the config file only)");
			if(ret==JOptionPane.YES_OPTION){
				create();
			}
		}
	}


}
