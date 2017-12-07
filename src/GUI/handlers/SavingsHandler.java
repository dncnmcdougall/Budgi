package GUI.handlers;

import javax.swing.JOptionPane;

import parts.Element;
import parts.Spent;
import GUI.dialogs.SavingsEdit;
import GUI.dialogs.ViewEntries;
import core.Main;

public class SavingsHandler extends AccountHandler {

    public SavingsHandler(Main main, Element elm) {
	super(main, elm);
    }

    @Override
    public void add() {
	elm.addSpentByDialog(parent);
    }
    
    @Override
    public void edit(Spent sp){
    elm.editSpentByDialog(parent, sp);
    }

    public void edit() {
	SavingsEdit se = new SavingsEdit(parent.getFrame(), elm);
	se.setVisible(true);
	if (se.getOption() == OK) {
	    if (elm.getTotal() != se.target) {
		stateChanged();
	    }
	    elm.setTotal(se.target);

	    if (!elm.getName().equals(se.name)) {
		stateChanged();
	    }
	    elm.setName(se.name);

	    if (se.mainSav) {
		if (parent.getMainSav() != null) {
		    if (!parent.getMainSav().equals(elm)) {
			int opt = JOptionPane.showConfirmDialog(parent
				.getFrame(), "Do you want to replace \""
				+ parent.getMainSav().getName() + "\" with \""
				+ elm.getName()
				+ "\"\nas the main savings Account?",
				"Replace Main Savings Account",
				JOptionPane.YES_NO_OPTION);

			if (opt == JOptionPane.YES_OPTION) {
			    if (!elm.isCarryOver()) {
				stateChanged();
			    }
			    elm.setCarryOver(true);
			    parent.getMainSav().setCarryOver(false);
			    parent.setMainSav(elm);
			    stateChanged();
			} else {
			    if (elm.isCarryOver()) {
				stateChanged();
			    }
			    elm.setCarryOver(false);
			}
		    }
		} else {
		    if (!elm.isCarryOver()) {
			stateChanged();
		    }
		    elm.setCarryOver(true);
		    parent.setMainSav(elm);

		}
	    }

	}
    }

    public void view() {
	ViewEntries ve = new ViewEntries(parent.getFrame(), elm, this);
	ve.setVisible(true);
    }

    @Override
    public void stateChanged() {
	parent.flagForRefresh();

    }

}
