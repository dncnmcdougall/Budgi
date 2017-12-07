package GUI.dialogs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import GUI.Frame;

public class AboutDialog extends JDialog implements ActionListener {

    private JLabel icon;
    private JLabel name_Ver;
    private JLabel desc;
    private JButton ok;
    
    
    
    public AboutDialog(Frame parent, String version){
	super(parent);
	setTitle("About");
	setModal(true);
	setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
	setResizable(false);
	
	setSize(300,300);
	setLocation(parent.getX() + (parent.getWidth()) / 2 - getWidth() / 2, parent.getY());
	

	setLayout(null);
	
	int width=80;
	int height=80;
	
	ImageIcon tmp=new ImageIcon("./Budgi_ico.png", "Icon");
	tmp.setImage(tmp.getImage().getScaledInstance(width-2, height-2, Image.SCALE_SMOOTH));
	icon=new JLabel(tmp);
	icon.setSize(width,height);
	
	icon.setLocation(getWidth()/2 - width/2,10);
	add(icon);
	
	name_Ver=new JLabel("Budgi "+version);
	Font f= name_Ver.getFont();
	f=new Font(f.getFontName(), f.getStyle(), f.getSize()+5);
	name_Ver.setHorizontalAlignment(JLabel.CENTER);
	name_Ver.setFont(f);
	name_Ver.setLocation(5, height+20);
	name_Ver.setSize(getWidth()-10,25);
	add(name_Ver);
	
	desc = new JLabel();
	desc.setText("<HTML><P ALIGN=\"CENTER\">A simple budget program to<BR>let you visulise the state<BR>of your finances<BR><BR>" +
			"by<BR>" +
			"Duncan McDougall</P></HTML>");
	desc.setLocation(5, height+50);
	desc.setSize(getWidth()-10,getHeight()-(height+85+40));
	desc.setHorizontalAlignment(JLabel.CENTER);
	desc.setVerticalAlignment(JLabel.TOP);
	add(desc);
	
	ok = new JButton("OK");
	ok.setSize(75,25);
	ok.setLocation(getWidth()/2 - ok.getWidth()/2,getHeight()-(40+30));
	ok.addActionListener(this);
	add(ok);
	
	
	
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	setVisible(false);

    }

}
