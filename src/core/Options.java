package core;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Options {
    public static final byte ALWAYS = 1;
    public static final byte ASK = 0;
    public static final byte NEVER = -1;

    private double income;

    private byte saveOnExit = ALWAYS;
    private byte archiveOnExit = ASK;

    private Dimension prefAccSize;
    private Dimension minAccSize;

    private Dimension frameSize;

    private String prefix;
    private String suffix;

    public Options() {
	income = 1000.00;
	prefAccSize = new Dimension(210, 140);
	minAccSize = new Dimension(180, 90);
	frameSize = new Dimension(500, 500);
    }

    public double getIncome() {
	return income;
    }

    public void setIncome(double income) {
	this.income = income;
    }

    public Dimension getPrefAccSize() {
	return prefAccSize;
    }

    public void setPrefAccSize(Dimension prefAccSize) {
	this.prefAccSize = prefAccSize;
    }

    public Dimension getMinAccSize() {
	return minAccSize;
    }

    public void setMinAccSize(Dimension minAccSize) {
	this.minAccSize = minAccSize;
    }

    public byte getSaveOnExit() {
	return saveOnExit;
    }

    public void setSaveOnExit(byte saveOnExit) {
	this.saveOnExit = saveOnExit;
    }

    public byte getArchiveOnExit() {
	return archiveOnExit;
    }

    public void setArchiveOnExit(byte archiveOnExit) {
	this.archiveOnExit = archiveOnExit;
    }

    public Dimension getFrameSize() {
	return frameSize;
    }

    public void setFrameSize(Dimension frameSize) {
	this.frameSize = frameSize;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
	this.suffix = suffix;
    }

    public String getPrefix() {
	return prefix;
    }

    public String getSuffix() {
	return suffix;
    }

    public void save(OutputStream out) {
	// TODO
	PrintWriter writer = new PrintWriter(out);
	writer.println("" + income);
	writer.println("" + prefAccSize.getWidth());
	writer.println("" + prefAccSize.getHeight());
	writer.println("" + saveOnExit);
	writer.println("" + archiveOnExit);
	writer.println("" + frameSize.getWidth());
	writer.println("" + frameSize.getHeight());
	writer.println(prefix);
	writer.println(suffix);
	writer.flush();
    }

    public static Options load(InputStream in) throws NumberFormatException,
	    IOException {
	// TODO
	Options opt = new Options();
	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	double income = Double.parseDouble(reader.readLine());
	double width = Double.parseDouble(reader.readLine());
	double height = Double.parseDouble(reader.readLine());
	byte saveOnExit = Byte.parseByte(reader.readLine());
	byte archiveOnExit = Byte.parseByte(reader.readLine());
	double fWidth = Double.parseDouble(reader.readLine());
	double fHeight = Double.parseDouble(reader.readLine());
	String prefix = reader.readLine();
	String suffix = reader.readLine();
	reader.close();

	opt.setIncome(income);
	opt.setPrefAccSize(new Dimension((int) width, (int) height));
	opt.setSaveOnExit(saveOnExit);
	opt.setArchiveOnExit(archiveOnExit);
	opt.setFrameSize(new Dimension((int) fWidth, (int) fHeight));
	opt.setPrefix(prefix);
	opt.setSuffix(suffix);
	return opt;
    }

}
