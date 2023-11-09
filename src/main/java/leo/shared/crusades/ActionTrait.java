///////////////////////////////////////////////////////////////////////
//	Name:	ActionTrait
//	Desc:	Display a trait
//	Date:	7/16/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Strings;
import leo.shared.TargetType;
import leo.shared.Unit;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionTrait implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final String name;
    private String message;
    private String rangeMessage = null;
    private String costMessage = null;
    private Unit hiddenUnit = null;
    private short type = Action.OTHER;
    private String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionTrait(Unit newOwner, String newName, String newMessage, String newRangeMessage, String newCostMessage) {
        owner = newOwner;
        name = newName;
        message = newMessage;
        rangeMessage = newRangeMessage;
        costMessage = newCostMessage;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionTrait(Unit newOwner, String newName, String newMessage, String newCostMessage) {
        owner = newOwner;
        name = newName;
        message = newMessage;
        rangeMessage = "";
        costMessage = newCostMessage;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        Logger.warn("ActionTrait being called for: " + owner.getName());
        return Strings.INVALID_ACTION;
    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getTargets();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return message;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return rangeMessage;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return costMessage;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        return 0;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void setDescription(String newMessage) {
        message = newMessage;
    }

    public void setRangeDescription(String newMessage) {
        rangeMessage = newMessage;
    }

    public void setCostDescription(String newMessage) {
        costMessage = newMessage;
    }

    public void setHiddenUnit(Unit newUnit) {
        hiddenUnit = newUnit;
    }

    public void setType(short newType) {
        type = newType;
    }

    public void setDetail(String newDetail) {
        detail = newDetail;
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return 0;
    }

    public short getRange() {
        return 0;
    }

    public short getTargetType() {
        return TargetType.NONE;
    }

    public Unit getOwner() {
        return owner;
    }

    public Unit getHiddenUnit() {
        return hiddenUnit;
    }

    public boolean passive() {
        return true;
    }

    public short getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}
