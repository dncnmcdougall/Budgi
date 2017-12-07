package GUI;

import java.util.ArrayList;

import javax.swing.JPanel;

import GUI.renderers.Renderer;
import GUI.renderers.RendererLayout;
import core.Main;

public class RendererPanel extends JPanel {

    private ArrayList<Renderer> renderers = new ArrayList<Renderer>();
    private Main m;

    public RendererPanel(Main m) {
	this.m = m;
	setLayout(new RendererLayout(m));
    }

    public void add(Renderer r) {
	renderers.add(r);
	super.add(r);
    }

    public boolean remove(Renderer r) {
	super.remove(r);
	return renderers.remove(r);
    }

    public void update() {
	for (Renderer r : renderers) {
	    r.update();
	}
    }
}
