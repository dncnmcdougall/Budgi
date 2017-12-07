package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * This class is a warper for a ZipFile. It allows writing to and reading from a
 * zip file easily.
 * </p>
 * <p>
 * It should be treated as a whole file.
 * </p>
 * <p>
 * This should be used for either input or output, and disposed of when it is
 * finished being used.
 * </p>
 * 
 * @author duncan
 * 
 */

public class ZipFileUtil {

    private ZipFile zip;
    private ZipOutputStream out;

    /**
     * <p>
     * This contractor is used to create a new Zipfile to write to.
     * </p>
     * <UL>
     * Should be used with:
     * <LI>getOutputStream(String)</LI>
     * <LI>closeOut()</LI>
     * <LI>finishWriting()</LI>
     * </UL>
     * 
     * @param fileName
     *            The filename of the Zip File to create.
     * @throws FileNotFoundException
     */
    public ZipFileUtil(String fileName) throws FileNotFoundException {
	super();

	out = new ZipOutputStream(new FileOutputStream(new File(fileName)));
    }

    /**
     * <p>
     * This method is used to get an OutputStream to write data to the current
     * Zip entry.
     * <p>
     * NOTE: Do not close the OutputStream with its default close functions, as
     * the root object may be a child of the OutputStream class and nead
     * diferent parameters. Rather Close it with this class' finishWriting()
     * method.
     * </p>
     * When done writing the whole file use: close(); </p>
     * 
     * @param fileName
     *            The file name for this entry to have in the .zip file.
     * @return an OutputStream that writes data to the current entry.
     * @throws IOException
     */
    public OutputStream getOutputStream(String fileName) throws IOException {
	ZipEntry tmp = new ZipEntry(fileName);
	tmp.setMethod(ZipEntry.DEFLATED);
	out.putNextEntry(tmp);
	return out;
    }

    /**
     * Use in between writes. Use Close to finish the file.
     * 
     * @throws IOException
     */
    public void finishWriting() throws IOException {
	out.flush();
	out.closeEntry();
    }

    /**
     * <p>
     * This finalises the file and finishes its write.
     * </p>
     * <p>
     * This means that this class can no longer write to the file. So only use
     * it once The file is finished.
     * </P>
     * 
     * @throws IOException
     */
    public void closeOut() throws IOException {
	out.finish();
	out.close();
    }

    /**
     * <p>
     * This contractor is used to read a zipfile.
     * </p>
     * <UL>
     * Should be used with:
     * <LI>getInputStream(String)</LI>
     * <LI>getEntrieNames(FilenameFilter)</LI>
     * <LI>getInputStreams(FilenameFilter)</LI>
     * </UL>
     * 
     * @param fileName
     *            The filename of the Zip File to create.
     * @throws FileNotFoundException
     */
    public ZipFileUtil(ZipFile zip) {
	super();
	this.zip = zip;
    }

    /**
     * Used to get an Input stream for a specific entry.
     * 
     * @param name
     *            The name of the zip entry to get the input stream for
     * @return An input stream for the specified input stream.
     * @throws IOException
     */
    public InputStream getInputStream(String name) throws IOException {
	ZipEntry tmp = zip.getEntry(name);
	if (tmp != null)
	    return zip.getInputStream(tmp);
	return null;

    }

    /**
     * Used to get all the names of all the files that match the Filename filter
     * 
     * @param ff
     *            The criteria to select a file
     * @return a list of all the files that match ff, in the zip file
     */
    public ArrayList<String> getEntrieNames(FilenameFilter ff) {
	// I don't know why I have to do it this way, but Apparently I do.
	Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
	ArrayList<String> files = new ArrayList<String>();
	while (entries.hasMoreElements()) {
	    ZipEntry ze = entries.nextElement();
	    if (ff.accept(new File("./"), ze.getName())) {
		files.add(ze.getName());
	    }
	}
	return files;
    }

    /**
     * Gets Input streams for all the entries that fit ff
     * 
     * @param ff
     *            A filename filter that determines which files to retrieve
     *            input streams for.
     * @return an array containing the appropriate input streams
     * @throws IOException
     */
    public ArrayList<InputStream> getInputStreams(FilenameFilter ff)
	    throws IOException {
	// I don't know why I have to do it this way, but Apparently I do.
	Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
	ArrayList<InputStream> streams = new ArrayList<InputStream>();

	while (entries.hasMoreElements()) {
	    ZipEntry ze = entries.nextElement();
	    if (!ze.isDirectory())
		if (ff.accept(new File("./"), ze.getName()))
		    streams.add(zip.getInputStream(ze));
	}
	return streams;
    }

}
