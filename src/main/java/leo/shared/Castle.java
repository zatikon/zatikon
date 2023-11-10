///////////////////////////////////////////////////////////////////////
// Name: Castle
// Desc: The castle
// Date: 5/10/2003 - Gabe Jones
//   12/14/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports

import java.util.Iterator;
import java.util.Vector;


public class Castle {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private short team = Unit.TEAM_1;
    private short[] units = new short[UnitType.UNIT_COUNT.value() * 2];
    private final Vector<UndeployedUnit> barracks = new Vector<UndeployedUnit>();
    private final Vector<Unit> graveyard = new Vector<Unit>();
    private short commandsMax = Constants.MAX_COMMANDS;
    private short commandsLeft = commandsMax;
    private short location;
    private final Vector<Unit> unitsOut = new Vector<Unit>();
    private transient BattleField battleField = null;
    private transient Observer observer = null;
    private int value = 0;
    private short logistics = 0;
    private short permArmor = 0;
    private short permPower = 0;
    private short armor = 0;
    private short power = 0;
    private int alterDamageBy = 0;
    private boolean ai = false;
    private Unit lastUnit;
    private int armistice = 0;
    private short militia = 0;
    private String whoExploded = ""; // for use with explode relic


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Castle() {
    }


    /////////////////////////////////////////////////////////////////
    // Interpret command
    /////////////////////////////////////////////////////////////////
    public void process(short action, short ID, short target) {
    }


    /////////////////////////////////////////////////////////////////
    // Deploy a unit
    /////////////////////////////////////////////////////////////////
    public String deploy(short newTeam, Unit unit, short location) { // Check for cost
        if (getCommandsLeft() < unit.getDeployCost()) return Strings.CASTLE_1;

        deductCommands(null, unit.getDeployCost());

        remove(unit);

        // Cover the special case of the explode relic so we can tell players who exploded
        if (unit.getID() == UnitType.RELIC_EXPLODE.value()) {
            whoExploded = unit.preDetonate(location);
        } else {
            if (unit.getID() == UnitType.RELIC_GIFT_UNIT.value()) // So command relic can be added back into castle
                unit.setTeam(newTeam);
            unit.preDeploy(location);
        }

        if (!unit.isRelic()) {
            // remove an existing unit
            // Doesn't deploy to field if the unit is a relic
            Unit oldUnit = getBattleField().getUnitAt(location);
            if (oldUnit != null && !oldUnit.isPowerUp()) {
                oldUnit.die(false, oldUnit);
                getObserver().abilityUsed(location, location, Constants.IMG_POOF);
                //getObserver().abilityUsed(location, location, ClientImages.IMG_POOF);
            }

            unit.setBattleField(battleField);

            unit.setLocation(location);

            battleField.add(unit);

            addOut(newTeam, unit);

            checkPowerUpPickup(unit, oldUnit);

            if (
                    BattleField.getDistance(unit.getLocation(), getLocation()) <= 1
                            && unit.getMove() != null
                            && !unit.deployed()) {
                unit.setLocation(getLocation());
                unit.setLocation(location);
            } else {
                unit.setHidden(true);
                getObserver().unitEffect(unit, Action.EFFECT_FADE_IN);
            }
        }

        // check for victory
        Castle enemyCastle;
        if (this == getBattleField().getCastle1()) {
            enemyCastle = getBattleField().getCastle2();
        } else {
            enemyCastle = getBattleField().getCastle1();
        }
        if (unit.getLocation() == enemyCastle.getLocation()) {
            getObserver().endGame(this);
            return unit.getName() + Strings.CASTLE_2;
        }

        // Again added to cover the case of the explode relic
        //if(unit.getID() == Unit.RELIC_EXPLODE) return "" + whoExploded + Strings.CASTLE_4;

        return "" + unit.getName() + Strings.CASTLE_3 + (BattleField.getX(location) + 1) + ", " + (BattleField.getY(location) + 1);

    }

    /////////////////////////////////////////////////////////////////
    // Pick up a powerup
    /////////////////////////////////////////////////////////////////
    public void checkPowerUpPickup(Unit receiver, Unit powerup) {
        if (powerup != null && powerup.isPowerUp()) {
            powerup.die(false, powerup);
            powerup.setTeam(receiver.getTeam());
            powerup.setCastle(receiver.getCastle());

            // grow if it's targetable by the powerup
            if (powerup.canTarget(receiver)) {
                receiver.grow(powerup.getPower());
                // generate powerup image
                receiver.getCastle().getObserver().imageDraw(powerup, powerup.getLocation(), powerup.getAppearance(), 6);
            }
            // otherwise banish it
            else {
                receiver.getCastle().add(powerup);
                // generate banish effect
                receiver.getCastle().getObserver().abilityUsed(powerup.getLocation(), powerup.getLocation(), Constants.IMG_POOF);
                receiver.getCastle().getObserver().castleAddition(powerup);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh(short team) {
        if (armistice > 0) armistice--;
        Vector<Unit> mine = getBattleField().getUnits(this);
        Iterator<Unit> it = mine.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getTeam() == team) unit.refresh();
        }

        commandsLeft = commandsMax;
        militia = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn(short team) {
        Vector<Unit> mine = getBattleField().getUnits(this);
        Iterator<Unit> it = mine.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getTeam() == team) {
                unit.skinwalk();
                unit.startTurn();
            }
        }

        if (team == Unit.TEAM_1) {
            armor = permArmor;
            power = permPower;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Deduct commands
    /////////////////////////////////////////////////////////////////
    public void deductCommands(Action source, short amount) {
        if (source != null && source.getOwner().freelyActs())
            return;
        commandsLeft -= amount;
    }


    /////////////////////////////////////////////////////////////////
    // Get a unit number
    /////////////////////////////////////////////////////////////////
    public short getUnit(Unit unit) {
        Iterator it = barracks.iterator();
        short i = 0;
        while (it.hasNext()) {
            UndeployedUnit tmpUnit = (UndeployedUnit) it.next();
            if (tmpUnit.getID() == unit.getID()) return i;
            i++;
        }
        return 100;
    }


    /////////////////////////////////////////////////////////////////
    // Get a unit by number
    /////////////////////////////////////////////////////////////////
    public Unit getUnit(int number) {
        if (barracks.size() > number) {
            UndeployedUnit uu = barracks.elementAt(number);
            return uu.getUnit();
        } else {
            return null;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Get undy by id
    /////////////////////////////////////////////////////////////////
    private UndeployedUnit getUnitByID(int id) {
        Iterator<UndeployedUnit> it = barracks.iterator();
        while (it.hasNext()) {
            UndeployedUnit uu = it.next();
            if (uu.getID() == id)
                return uu;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Add a unit
    /////////////////////////////////////////////////////////////////
    public void add(Unit newUnit) {
        if (newUnit.getID() == UnitType.NONE.value()) return;
        lastUnit = newUnit;
        UndeployedUnit undy = getUnitByID(newUnit.getID());
        if (undy == null) {
            barracks.add(new UndeployedUnit(newUnit));
        } else {
            undy.add();
        }
        value += newUnit.getCastleCost();
        short id = newUnit.getID();
        if (id < 0)
            units[(0 - id) + UnitType.UNIT_COUNT.value()]++;
        else
            units[id]++;
    }


    /////////////////////////////////////////////////////////////////
    // Add a unit
    /////////////////////////////////////////////////////////////////
    public void add(int i, Unit newUnit) {
        if (newUnit.getID() == UnitType.NONE.value()) return;
        lastUnit = newUnit;
        barracks.add(i, new UndeployedUnit(newUnit));
        value += newUnit.getCastleCost();
        short id = newUnit.getID();
        if (id < 0)
            units[(0 - id) + UnitType.UNIT_COUNT.value()]++;
        else
            units[id]++;
    }


    /////////////////////////////////////////////////////////////////
    // Add a unit to the field
    /////////////////////////////////////////////////////////////////
    public void addOut(short newTeam, Unit newUnit) {
        newUnit.setSequence(getBattleField().getSequence());
        newUnit.setTeam(newTeam);
        unitsOut.add(newUnit);
        newUnit.entered();
    }


    /////////////////////////////////////////////////////////////////
    // Add a unit to the graveyard
    /////////////////////////////////////////////////////////////////
    public void addGraveyard(Unit newUnit) {
        graveyard.add(newUnit);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a unit from the field
    /////////////////////////////////////////////////////////////////
    public void removeOut(Unit oldUnit) {
        unitsOut.remove(oldUnit);
        oldUnit.removed();
    }


    /////////////////////////////////////////////////////////////////
    // Remove a unit
    /////////////////////////////////////////////////////////////////
    public void remove(Unit oldUnit) {
        UndeployedUnit undy = getUnitByID(oldUnit.getID());
        if (undy == null) return;
        undy.remove();

        if (undy.count() <= 0) {
            barracks.remove(undy);
        }

        value -= oldUnit.getCastleCost();

        short id = oldUnit.getID();
        if (id < 0)
            units[(0 - id) + UnitType.UNIT_COUNT.value()]--;
        else
            units[id]--;
    }


    /////////////////////////////////////////////////////////////////
    // Remove last
    /////////////////////////////////////////////////////////////////
    public void removeLast() {
        remove(lastUnit);
    }


    /////////////////////////////////////////////////////////////////
    // Clear the barracks
    /////////////////////////////////////////////////////////////////
    public void clear() {
        barracks.clear();
        value = 0;
        units = new short[UnitType.UNIT_COUNT.value() * 2];
    }


    /////////////////////////////////////////////////////////////////
    // Add armor
    /////////////////////////////////////////////////////////////////
    public void addArmor(short amount) {
        armor += amount;
    }


    /////////////////////////////////////////////////////////////////
    // Add power
    /////////////////////////////////////////////////////////////////
    public void addPower(short amount) {
        power += amount;
    }


    /////////////////////////////////////////////////////////////////
    // Add permanent armor
    /////////////////////////////////////////////////////////////////
    public void addPermArmor(short amount) {
        permArmor += amount;
        armor += amount;
    }


    /////////////////////////////////////////////////////////////////
    // Add permanent power
    /////////////////////////////////////////////////////////////////
    public void addPermPower(short amount) {
        permPower += amount;
        power += amount;
    }


    /////////////////////////////////////////////////////////////////
    // Is this castle depleted?
    /////////////////////////////////////////////////////////////////
    public boolean depleted() {
        return (barracks.size() + unitsOut.size() < 1);
    }


    /////////////////////////////////////////////////////////////////
    // Get the count
    /////////////////////////////////////////////////////////////////
    public int getCount() {
        int count = 0;
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit undy = barracks.elementAt(i);
            count += undy.count();
        }
        return count;
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public short getCommandsLeft() {
        return ai ? 10 : commandsLeft;
    }

    public short getCommandsMax() {
        return commandsMax;
    }

    public short getLocation() {
        return location;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public Vector<UndeployedUnit> getBarracks() {
        return barracks;
    }

    public Vector<Unit> getGraveyard() {
        return graveyard;
    }

    public Observer getObserver() {
        return observer;
    }

    public int getValue() {
        return value;
    }

    public short getLogistics() {
        return logistics;
    }

    public short getArmor() {
        return armor;
    }

    public short getPower() {
        return power;
    }

    public short[] getUnits() {
        return units;
    }

    public boolean isAI() {
        return ai;
    }

    public short getTeam() {
        return team;
    }

    public boolean armistice() {
        return armistice > 0;
    }

    public Vector<Unit> getUnitsOut() {
        return unitsOut;
    }

    public short getMilitia() {
        return militia;
    }


    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void startArmistice() {
        armistice = 2;
    }

    public void setCommandsMax(short amount) {
        commandsMax = amount;
    }

    public void setCommandsLeft(short amount) {
        commandsLeft = amount;
    }

    public void setLogistics(short amount) {
        logistics = amount;
    }

    public void setLocation(short newLocation) {
        location = newLocation;
    }

    public void setBattleField(BattleField newBattleField) {
        battleField = newBattleField;
    }

    public void setObserver(Observer newObserver) {
        observer = newObserver;
    }

    public void alterDamage(int amount) {
        alterDamageBy += amount;
    }

    public void ai() {
        ai = true;
    }

    public void setTeam(short newTeam) {
        team = newTeam;
    }

    public void setMilitia(short amount) {
        militia += amount;
    }
}
