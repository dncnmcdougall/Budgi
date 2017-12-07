package GUI.handlers;

import parts.Element;
import parts.Spent;
import core.Main;

public abstract class AccountHandler extends Handler {
    protected Element elm;

    public AccountHandler(Main main, Element elm) {
	super(main);
	this.elm = elm;
    }

    public abstract void add();

    public abstract void edit(Spent sp);
    
    public abstract void stateChanged();
    
}
