package GUI.renderers;

import parts.Element;
import parts.PartConsts;
import GUI.renderers.parts.AllowRenderer;
import GUI.renderers.parts.ExpenceRenderer;
import GUI.renderers.parts.SavingsRenderer;
import core.Main;

public class RendererFactory implements PartConsts {

    public static Renderer createRenderer(Element e, Main m) {
	switch (e.getType()) {
	case SAVINGS:
	    return new SavingsRenderer(e, m);
	case EXPENCE:
	    return new ExpenceRenderer(e, m);
	case ALLOWANCE:
	    return new AllowRenderer(e, m);
	}
	return null;
    }

}
