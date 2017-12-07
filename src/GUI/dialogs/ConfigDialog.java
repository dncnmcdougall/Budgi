package GUI.dialogs;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import core.Config;
import core.Main;

public class ConfigDialog extends Dialog {

    private Config conf;

    private String text;

    private JLabel saveDirL;
    private JTextField saveDirT;
    private JButton okButton;
    private JButton cancelButton;
    private JButton defaultButton;

    public ConfigDialog(Main main){
	super(main.getFrame());

	this.conf=main.getConf();
	text=main.getConf().toString();
	init();
    }

    public void init(){
	setLayout(null);

	int width = 300;
	int height=5;

	saveDirL = new JLabel("Save Directory");
	saveDirL.setSize(width-25,25);
	saveDirL.setLocation(5,height);
	add(saveDirL);

	height+=30;

	saveDirT = new JTextField(conf.getSaveDir().toString());
	saveDirT.setSize(width -25,25);
	saveDirT.setLocation(5, height);
	add(saveDirT);

	height+=30;

	okButton = new JButton("Ok");
	okButton.setSize(85,25);
	okButton.setLocation(5, height);
	okButton.addActionListener(this);
	add(okButton);

	defaultButton = new JButton("Default");
	defaultButton.setSize(90, 25);
	defaultButton.setLocation(95, height);
	defaultButton.addActionListener(this);
	add(defaultButton);

	cancelButton = new JButton("Cancel");
	cancelButton.setSize(85, 25);
	cancelButton.setLocation(190, height);
	cancelButton.addActionListener(this);
	add(cancelButton);

	height+=30;
	height+=30;
	height+=25;
	setSize(width,height);
    }



    public String getText() {
	return text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if(e.getSource().equals(okButton)){
	    option = OK;

	    String saveDir = saveDirT.getText();
	    if(saveDir==null || saveDir.length()==0){
		int opt=JOptionPane.showConfirmDialog(this, "You are about to set the save path blank.\nDo you want to continue?","Warning", JOptionPane.YES_NO_OPTION);

		if(opt==JOptionPane.NO_OPTION){
		    return;
		}
	    }
	    text=saveDir;
	    setVisible(false);

	}else if(e.getSource().equals(defaultButton)){
	    
	    text=conf.getSaveDir().toString();
	    saveDirT.setText(text);
	    
	}else if(e.getSource().equals(cancelButton)){
	    option = CANCEL;

	    text=conf.getSaveDir().toString();
	    saveDirT.setText(text);
	    setVisible(false);
	}

    }

}
