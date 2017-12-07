package GUI.handlers;

import GUI.dialogs.DialogConsts;
import core.Main;

public abstract class Handler implements DialogConsts {

    protected Main parent;

    public Handler(Main parent) {
	super();
	this.parent = parent;
    }

}
