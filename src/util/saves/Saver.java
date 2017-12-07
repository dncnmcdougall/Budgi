package util.saves;

import java.io.BufferedReader;
import java.io.OutputStream;

public interface Saver<E> {

	public String getVersion();
	
	public void save(OutputStream out, E elm);
	
	public E load(BufferedReader in) throws LoadException;
	
	public String toString();
	
}
