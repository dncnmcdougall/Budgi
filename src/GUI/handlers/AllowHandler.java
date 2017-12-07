package GUI.handlers;

import java.util.Calendar;

import parts.Element;
import parts.Spent;
import GUI.dialogs.AllowEdit;
import GUI.dialogs.ViewEntries;
import core.Main;

public class AllowHandler extends AccountHandler {

    public AllowHandler(Main parent, Element elm) {
	super(parent, elm);
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
	AllowEdit ae = new AllowEdit(parent.getFrame(), elm);
	ae.setVisible(true);
	if (ae.getOption() == OK) {
	    if (!elm.getName().equals(ae.name)) {
		stateChanged();
	    }
	    elm.setName(ae.name);

	    boolean carryOverChanged = false;
	    if (elm.isCarryOver() != ae.carryOver) {
		stateChanged();
		carryOverChanged = true;
	    }
	    elm.setCarryOver(ae.carryOver);
	    elm.setShowProgress(ae.showTarget);

	    if (elm.isPercent() != ae.percentage) {
		stateChanged();
	    }
	    elm.setPercent(ae.percentage);

	    /*
	     * if((elm.isPercent() && (ae.amount!=elm.getAmount())) ||
	     * (!elm.isPercent() && (ae.amount!=elm.getTotal()))){
	     * stateChanged(); if(elm.isCarryOver() && !carryOverChanged){
	     * 
	     * } } elm.setAmount(ae.amount);
	     * 
	     * if(!ae.percentage){ elm.setTotal(ae.total); }
	     */

	    if (ae.amount != elm.getAmount()) {
		stateChanged();
	    }

	    if (ae.amount != elm.getTotal() && !elm.isPercent()
		    && !elm.isCarryOver()) {
		stateChanged();
		ae.total = ae.amount;
		elm.setTotal(ae.total);
	    }

	    elm.setAmount(ae.amount);

	    if (ae.total != elm.getTotal() && !elm.isPercent()
		    && elm.isCarryOver() && !ae.reset) {// is carry over, with a
		// changed total
		stateChanged();
		double tmp = elm.getTotal();
		double diff = tmp - ae.total;
		Calendar calendar = Calendar.getInstance();
		if (diff < 0) {
		    parent
			    .getMainSav()
			    .addSpent(
				    new Spent(
					    diff,
					    elm.getName()
						    + "'s total was incread. This is to compensate",
					    calendar.getTime()));
		} else {
		    parent
			    .getMainSav()
			    .addSpent(
				    new Spent(
					    diff,
					    elm.getName()
						    + "'s total was decreased. This is to compensate",
					    calendar.getTime()));
		}
		elm.setTotal(ae.total);
	    }
	    if (ae.reset && elm.isCarryOver()) {
		elm.clearSpent();
		elm.setTotal(0);
		stateChanged();
	    }

	    if (elm.getPriority() != ae.priority) {
		stateChanged();

	    }
	    elm.setPriority(ae.priority);
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
