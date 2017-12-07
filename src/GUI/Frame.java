package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import parts.Element;
import GUI.handlers.MenuEventHandler;
import GUI.renderers.Renderer;
import GUI.renderers.RendererLayout;
import core.Main;

public class Frame extends JFrame {

    // Main
    public Main m;

    // Menu
    private JMenuBar menBar;
    private FileMenu fileMenu;
    private EditMenu editMenu;
    private HelpMenu helpMenu;

    // Renderers
    private RendererPanel rendP;
    private JScrollPane rendSP;
    private JPanel rendC;

    private MenuEventHandler menHandler;

    private boolean maximised = false;

    private ToolsMenu toolsMenu;



    public Frame(Main m) throws HeadlessException {
	super();
	this.m = m;

	setSize(500, 500);

	setLayout(new BorderLayout());
	initMenu();
	initRend();

	setIconImage(new ImageIcon("./Duncans budgi.png").getImage());

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	initWindowListener();
    }

    private void initMenu() {
	menHandler = new MenuEventHandler(m);

	menBar = new JMenuBar();

	fileMenu = new FileMenu(menHandler);
	
	editMenu = new EditMenu(menHandler);
	
	toolsMenu = new ToolsMenu(menHandler);
	
	helpMenu = new HelpMenu(menHandler);
	
	menBar.add(fileMenu);
	menBar.add(editMenu);
	menBar.add(toolsMenu);
	menBar.add(helpMenu);

	setJMenuBar(menBar);
    }

    private void initRend() {
	rendP = new RendererPanel(m);

	rendC = new JPanel(new BorderLayout());
	rendC.setBorder(new LineBorder(Color.GREEN));

	add(rendC, BorderLayout.CENTER);

	rendSP = new JScrollPane(rendP);

	rendC.add(rendSP, BorderLayout.CENTER);
    }

    private void initWindowListener() {

	addWindowListener(new WindowListener() {

	    @Override
	    public void windowOpened(WindowEvent arg0) {
	    }

	    @Override
	    public void windowIconified(WindowEvent arg0) {
	    }

	    @Override
	    public void windowDeiconified(WindowEvent arg0) {
	    }

	    @Override
	    public void windowDeactivated(WindowEvent arg0) {
	    }

	    @Override
	    public void windowClosing(WindowEvent arg0) {
		menHandler.close();
	    }

	    @Override
	    public void windowClosed(WindowEvent arg0) {
		System.out.println("closed");

	    }

	    @Override
	    public void windowActivated(WindowEvent arg0) {
	    }
	});

	addComponentListener(new ComponentListener() {

	    @Override
	    public void componentShown(ComponentEvent arg0) {
	    }

	    @Override
	    public void componentResized(ComponentEvent arg0) {
		if (!maximised)
		    m.getOptions().setFrameSize(getSize());
		adjustRendPSize();
		/*
		 * RendererLayout rl=((RendererLayout)rendP.getLayout()); int
		 * spW=rendSP.getWidth(); int spC=rl.getWCnt(rendSP); int
		 * accW=(int)m.getOptions().getPrefAccSize().getWidth(); int
		 * pW=rl.getWCnt(rendP)*accW; if((spW-pW)>(2*accW/3) ){
		 * rendP.setSize((spC+1)*accW,rendP.getHeight()); }else if(
		 * (spW-pW)<-(accW/3) ){
		 * rendP.setSize(spC*accW,rendP.getHeight()); }
		 */
	    }

	    @Override
	    public void componentMoved(ComponentEvent arg0) {
	    }

	    @Override
	    public void componentHidden(ComponentEvent arg0) {
	    }
	});

	addWindowStateListener(new WindowStateListener() {

	    @Override
	    public void windowStateChanged(WindowEvent e) {
		int newS = e.getNewState();
		int oldS = e.getOldState();
		if (newS == MAXIMIZED_BOTH && oldS == NORMAL)
		    maximised = true;
		else if (oldS == MAXIMIZED_BOTH && newS == NORMAL)
		    maximised = false;

	    }
	});

    }

    public void add(Renderer r) {
	r.addMouseListener(menHandler);
	rendP.add(r);
	doLayout();
    }

    public void update() {
	rendP.update();

    }

    private void adjustRendPSize() {
	if (rendP == null) {
	    return;
	}
	if (rendP.getSize().equals(new Dimension(0, 0))
		|| rendP.getComponentCount() == 0) {
	    rendP.setSize((int) m.getOptions().getPrefAccSize().getWidth(),
		    (int) m.getOptions().getPrefAccSize().getHeight());
	    return;
	}

	RendererLayout rl = ((RendererLayout) rendP.getLayout());
	int spW = rendSP.getWidth();
	int spC = rl.getWCnt(rendSP);
	int accW = (int) m.getOptions().getPrefAccSize().getWidth();
	int pW = rl.getWCnt(rendP) * accW;

	if ((spW - pW) > (2 * accW / 3)) {
	    rendP.setSize((spC + 1) * accW, rendP.getHeight());
	} else if ((spW - pW) < -(accW / 3)) {
	    rendP.setSize(spC * accW, rendP.getHeight());
	}
    }

    @Override
    public void setSize(int width, int height) {
	super.setSize(width, height);
	adjustRendPSize();
    }

    @Override
    public void setSize(Dimension d) {
	super.setSize(d);
	adjustRendPSize();
    }

    @Override
    public void doLayout() {
	super.doLayout();
	rendP.doLayout();
	rendP.update();
    }

    public void remove(Element e) {
	for (Component c : rendP.getComponents()) {
	    if (c instanceof Renderer) {
		Renderer tmp = (Renderer) c;
		if (tmp.getElement().equals(e)) {
		    System.out.println(rendP.remove(tmp));
		    doLayout();
		    rendP.repaint();
		    return;
		}
	    }
	}
    }

}
