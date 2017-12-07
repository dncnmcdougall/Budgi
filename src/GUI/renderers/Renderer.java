package GUI.renderers;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager.LookAndFeelInfo;

import parts.Element;
import core.Main;

public abstract class Renderer extends JPanel {

    protected Element elm;
    protected Main parent;
    
    protected Font TITLE_FONT;
    protected Font CONTENT_FONT;
    protected Font BUTTON_FONT;

    public Renderer(Element elm, Main parent) {
	super();
	this.elm = elm;
	this.parent = parent;

	TITLE_FONT = super.getFont();
	
	CONTENT_FONT = TITLE_FONT.deriveFont((float)(TITLE_FONT.getSize2D()));
	BUTTON_FONT = TITLE_FONT.deriveFont((float)(TITLE_FONT.getSize2D()));
	TITLE_FONT = TITLE_FONT.deriveFont(TITLE_FONT.getStyle() | Font.BOLD);
    }

    public Element getElement() {
	return elm;
    }

    public abstract void update();

}
