package util.saves;

public class LoadException extends Exception {

	private Saver saver;
	
	public LoadException(Saver saver){
		super();
		this.saver= saver;
	}
	
	public LoadException(Saver saver, String message){
		super(message);
		this.saver= saver;
	}
	
	
}
