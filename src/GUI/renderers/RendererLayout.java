package GUI.renderers;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import core.Main;

public class RendererLayout implements LayoutManager {

    private Main m;

    public RendererLayout(Main m) {
	this.m = m;
    }

    @Override
    public void addLayoutComponent(String arg0, Component cont) {
	// do nothing

    }

    @Override
    public void layoutContainer(Container cont) {
	int compCnt = cont.getComponentCount();
	int wCnt = getWCnt(cont);
	Dimension prefSize = m.getOptions().getPrefAccSize();
	int col = 0;
	int row = 0;

	Component[] comps = cont.getComponents();

	while (row * wCnt + col < compCnt) {
	    if (col >= wCnt) {
		col = 0;
		row++;
	    }

	    int x = (col + 1) * 5 + col * prefSize.width;
	    int y = (row + 1) * 5 + row * prefSize.height;

	    comps[row * wCnt + col].setBounds(x, y, prefSize.width,
		    prefSize.height);
	    comps[row * wCnt + col].doLayout();

	    col++;
	}
    }

    @Override
    public Dimension minimumLayoutSize(Container cont) {
	int wCnt = getWCnt(cont);
	if (wCnt == 0) {
	    return new Dimension(0, 0);
	}
	int hCnt = (cont.getComponentCount() / wCnt);

	int w = wCnt * m.getOptions().getMinAccSize().width + (wCnt + 1) * 5;
	int h = hCnt * m.getOptions().getMinAccSize().height + (hCnt + 1) * 5;
	return new Dimension(w, h);
    }

    @Override
    public Dimension preferredLayoutSize(Container cont) {
	int wCnt = getWCnt(cont);
	if (wCnt == 0) {
	    return new Dimension(0, 0);
	}
	int hCnt = (cont.getComponentCount() / wCnt);

	int w = wCnt * m.getOptions().getPrefAccSize().width + (wCnt + 1) * 5;
	int h = hCnt * m.getOptions().getPrefAccSize().height + (hCnt + 1) * 5;
	return new Dimension(w, h);
    }

    public int getWCnt(Container cont) {
	return cont.getWidth() / m.getOptions().getPrefAccSize().width;
    }

    @Override
    public void removeLayoutComponent(Component cont) {
	// do nothing
    }

}
