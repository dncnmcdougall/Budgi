package parts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class Spent {

    private Date date;
    private double amount;
    private String description;
    private Element catagory;

    public Spent(double amount, String description, Date date) {
	super();
	this.amount = amount;
	this.description = description;
	this.date = date;
    }

    public Element getCatagory() {
	return catagory;
    }

    public void setCatagory(Element catagory) {
	this.catagory = catagory;
    }

    public double getAmount() {
	return amount;
    }

    public String getDescription() {
	return description;
    }

    public Date getDate() {
	return date;
    }

    /**
     * Saves this Spent to the specified OutputStream. <BR>
     * This flushes but does not close the stream when it is finished.<BR>
     * That is left for the Element that called this method to do.
     * 
     * @param out
     *            The OutputStream to write this Spent Object's data to.
     */
    public void save(OutputStream out) {
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	PrintWriter writer = new PrintWriter(out);
	writer.println("" + cal.get(Calendar.DAY_OF_MONTH));
	writer.println("" + (cal.get(Calendar.MONTH)));
	writer.println("" + cal.get(Calendar.YEAR));
	writer.println("" + amount);
	int length = description.length();
	writer.println("" + length);

	for (int i = 0; i < length; i++) {
	    writer.write(description.charAt(i));
	}
	writer.println();
	writer.flush();
    }

    /**
     * Creates a Spent Object by reading from the current location in the input
     * stream(or null if it cannot)<BR>
     * This method moves the streams pointer to after the Spent Object Data.<BR>
     * This method does not close the stream.<BR>
     * 
     * @param reader
     * @return The Spent object read from the stream
     * @throws IOException
     */
    public static Spent load(BufferedReader reader) throws IOException {
	String buffer = reader.readLine();
	int day = Integer.parseInt(buffer);
	int month = Integer.parseInt(reader.readLine());
	int year = Integer.parseInt(reader.readLine());
	double amount = Double.parseDouble(reader.readLine());
	int charCnt = Integer.parseInt(reader.readLine());
	String desc = "";
	for (int i = 0; i < charCnt; i++) {
	    desc += (char) reader.read();
	}
	reader.readLine();
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.DAY_OF_MONTH, day);
	cal.set(Calendar.MONTH, month);
	cal.set(Calendar.YEAR, year);

	Spent s = new Spent(amount, desc, cal.getTime());

	return s;
    }

}
