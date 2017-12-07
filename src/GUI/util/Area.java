package GUI.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Area extends JComponent {

    /**
     * 0 = colour 1 = gradient 2 = image
     */
    private int type;

    /**
     * 0 = Horizontal 1 = Vertical
     */
    private int dir;

    public static final int Horizontal = 0;
    public static final int Vertical = 1;

    private Object o1;
    private Object o2;

    private Border b;

    public Area(ImageIcon i) {
	type = 2;
	o1 = i;
	b = new LineBorder(new Color(190, 190, 255));
	init();
    }

    public Area(ImageIcon i, Border b) {
	type = 2;
	o1 = i;
	this.b = b;
	init();
    }

    public Area(Color c1, Color c2, int dir) {
	o1 = c1;
	o2 = c2;
	type = 1;
	this.dir = dir;
	b = new LineBorder(new Color(190, 190, 255));
	init();
    }

    public Area(Color c1, Color c2, int dir, Border b) {
	o1 = c1;
	o2 = c2;
	type = 1;
	this.dir = dir;
	this.b = b;
	init();
    }

    public Area(Color c) {
	o1 = c;
	type = 0;
	b = new LineBorder(new Color(190, 190, 255));
	init();
    }

    public Area(Color c, Border b) {
	o1 = c;
	type = 0;
	this.b = b;
	init();
    }

    private void init() {
	setBorder(b);
	setOpaque(true);
    }
    
    public Border getBorder(){
    	return b;
    }
    
    public void setBorder(Border b){
    	this.b=b;
    }
    

    @Override
    public void paint(Graphics g) {
	// super.paint(g);

	switch (type) {
	case 0:// color
	{
	    Color c = (Color) o1;
	    g.setColor(c);
	    g.fillRect(0, 0, getWidth(), getHeight());
	}
	    break;
	case 1:// gradient
	{
	    Color c1 = (Color) o1;
	    Color c2 = (Color) o2;
	    Graphics2D g2 = (Graphics2D) g;
	    boolean hor = (dir == Horizontal);
	    Point2D p1 = new Point((hor ? 0 : (getWidth() / 2)),
		    (hor ? (getHeight() / 2) : 0));
	    Point2D p2 = new Point((hor ? getWidth() : (getWidth() / 2)),
		    (hor ? (getHeight() / 2) : getHeight()));
	    ;
	    Paint gp = new GradientPaint(p1, c1, p2, c2, true);
	    g2.setPaint(gp);
	    g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
	}
	    break;
	case 2:// image
	{
	    ImageIcon i = (ImageIcon) o1;

	    g.drawImage(i.getImage(), 0, 0, getWidth(), getHeight(), null);
	}
	    break;

	}
	if (b != null) {
	    b.paintBorder(this, g, 0, 0, getWidth(), getHeight());
	}

    }

}
