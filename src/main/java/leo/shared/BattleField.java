///////////////////////////////////////////////////////////////////////
// Name: BattleField
// Desc: The battleField
// Date: 5/10/2003 - Gabe Jones
// TODO: Dirty and inneficient. May require future optimization
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports

import leo.shared.inquisition.Hero;

import java.util.Iterator;
import java.util.Vector;


public class BattleField {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Vector<Unit> units = new Vector<Unit>();
    private final Castle castle1;
    private final Castle castle2;
    private int sequence = 0;
    private final Vector<Short> graves = new Vector<Short>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public BattleField(Castle newCastle1, Castle newCastle2) {
        castle1 = newCastle1;
        castle2 = newCastle2;
    }


    /////////////////////////////////////////////////////////////////
    // Get a new sequence number
    /////////////////////////////////////////////////////////////////
    public int getSequence() {
        return sequence++;
    }

    /////////////////////////////////////////////////////////////////
    // Graves: stores locations of dead units
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getGraves() {
        return graves;
    }

    // Add a grave
    public void addGrave(Short grave) {
        if (grave != castle1.getLocation() && grave != castle2.getLocation()) {
            graves.add(grave);
        }
    }

    // Remove a grave
    public void removeGrave(Short grave) {
        graves.remove(grave);
    }

    /////////////////////////////////////////////////////////////////
    // Get castle targets
    /////////////////////////////////////////////////////////////////
    public void addBonusCastleTargets(Unit looker, Vector<Short> targets, Castle castle) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit tmp = it.next();

            Vector<Short> bonusTargets = tmp.getBonusCastleTargets(looker);
            if (bonusTargets != null) addBonusTargets(bonusTargets, targets);
        }
    }

    private void addBonusTargets(Vector<Short> newTargets, Vector<Short> allTargets) {
        for (int i = 0; i < newTargets.size(); i++) {
            Short b = newTargets.elementAt(i);
            addBonusTarget(b, allTargets);
        }
    }

    private void addBonusTarget(Short target, Vector<Short> targets) {
        for (int i = 0; i < targets.size(); i++) {
            Short b = targets.elementAt(i);
            if (b.byteValue() == target.byteValue()) return;
        }
        targets.add(target);
    }


    /////////////////////////////////////////////////////////////////
    // Get castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets(Castle castle) {
        Vector<Short> targets = new Vector<Short>();
        short startingPoint = castle.getLocation();
        short x = BattleField.getX(startingPoint);
        short y = BattleField.getY(startingPoint);

        // Up
        checkCastleLocation(targets, x, (byte) (y - 1));

        // Up right
        checkCastleLocation(targets, (byte) (x + 1), (byte) (y - 1));

        // Right
        checkCastleLocation(targets, (byte) (x + 1), y);

        // Down right
        checkCastleLocation(targets, (byte) (x + 1), (byte) (y + 1));

        // Down
        checkCastleLocation(targets, x, (byte) (y + 1));

        // Down left
        checkCastleLocation(targets, (byte) (x - 1), (byte) (y + 1));

        // Left
        checkCastleLocation(targets, (byte) (x - 1), y);

        // Up left
        checkCastleLocation(targets, (byte) (x - 1), (byte) (y - 1));

        // Add done
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Check castle location
    /////////////////////////////////////////////////////////////////
    private void checkCastleLocation(Vector<Short> targets, short x, short y) {
        short location = BattleField.getLocation(x, y);
        Unit obstacle = getUnitAt(location);
        if (x >= 0 && x < Constants.BOARD_SIZE && y >= 0 && y < Constants.BOARD_SIZE)
            if (obstacle == null || obstacle.isPowerUp())
                addTarget(targets, new Short(location));
    }


    /////////////////////////////////////////////////////////////////
    // Get targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets(Unit unit, short targetType, short range, boolean includeEmpty, boolean organic, boolean inorganic, short selectionType, Castle castle) {
        // Start with bonus targets
        Vector<Short> targets = unit.getTargets(targetType);
        //Vector<Short> targets = new Vector<Short>();
        // add lines
        switch (targetType) {
            case TargetType.ANY_LINE:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, false, true, true, organic, inorganic, selectionType, castle));
                break;

            case TargetType.ANY_LINE_JUMP:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, true, true, true, organic, inorganic, selectionType, castle));
                break;

            case TargetType.ANY_AREA:
                targets.addAll(getArea(unit, unit.getLocation(), range, true, true, organic, inorganic, selectionType, castle));
                break;

            case TargetType.LOCATION_LINE:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, false, true, false, organic, inorganic, selectionType, castle));
                break;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            case TargetType.LOCATION_LINE_JUMP:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, true, true, false, organic, inorganic, selectionType, castle));
                break;

            case TargetType.LOCATION_AREA:
                targets.addAll(getArea(unit, unit.getLocation(), range, true, false, organic, inorganic, selectionType, castle));
                break;

            case TargetType.UNIT_LINE:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, false, includeEmpty, true, organic, inorganic, selectionType, castle));
                break;

            case TargetType.UNIT_LINE_JUMP:
                targets.addAll(getAllLines(unit, unit.getLocation(), range, true, includeEmpty, true, organic, inorganic, selectionType, castle));
                break;

            case TargetType.UNIT_AREA:
                targets.addAll(getArea(unit, unit.getLocation(), range, includeEmpty, true, organic, inorganic, selectionType, castle));
                break;

        }
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get a block area
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getArea(Unit looker, short startingPoint, short range, boolean locations, boolean units, boolean organic, boolean inorganic, short selectionType, Castle castle) {
        Vector<Short> targets = new Vector<Short>();

        // Convert the starting point to x and y
        short x = BattleField.getX(startingPoint);
        short y = BattleField.getY(startingPoint);

        // Define the loop boundaries
        short leftX = ((byte) (x - range) < 0 ? 0 : ((byte) (x - range)));
        short rightX = ((byte) (x + range) > Constants.BOARD_SIZE - 1 ? Constants.BOARD_SIZE - 1 : ((byte) (x + range)));
        short topY = ((byte) (y - range) < 0 ? 0 : ((byte) (y - range)));
        short bottomY = ((byte) (y + range) > Constants.BOARD_SIZE - 1 ? Constants.BOARD_SIZE - 1 : ((byte) (y + range)));

        // Loop through and acquire locations
        for (short ly = topY; ly <= bottomY; ly++)
            for (short lx = leftX; lx <= rightX; lx++) {
                short location = BattleField.getLocation(lx, ly);
                Unit unit = getUnitAt(location);
                // exception for moving onto powerups
                if (unit != null)
                    if (selectionType == TargetType.MOVE && unit.isPowerUp())
                        addTarget(targets, new Short(location));
                /////////////////////////////
                //if (location != startingPoint && (looker.getCastle().getLocation() != location || units) && ((units && unit != null) || (locations && unit == null)))
                if ((looker.getCastle().getLocation() != location || units) && ((units && unit != null) || (locations && unit == null)))
                    if (
                            (unit == null) ||
                                    (unit != null && unit.targetable(looker) && (organic == unit.getOrganic(looker) || inorganic != unit.getOrganic(looker)) &&
                                            (selectionType == TargetType.BOTH || unit.getID() == Unit.ROCK || (selectionType == TargetType.FRIENDLY && unit.getCastle() == castle) || (selectionType == TargetType.ENEMY && unit.getCastle() != castle))
                                    )
                    )
                        addTarget(targets, new Short(location));
            }

        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Make sure to order targets by sequence. Prevents desynch
    /////////////////////////////////////////////////////////////////
    private void addTarget(Vector<Short> targets, Short target) {
        Unit adder = getUnitAt(target.byteValue());
        if (adder != null) {
            for (int i = 0; i < targets.size(); i++) {
                Short loc = targets.elementAt(i);
                Unit unit = getUnitAt(loc.byteValue());
                if
                (unit != null &&
                        (adder.getPriority() > unit.getPriority() ||
                                (adder.getSequence() < unit.getSequence() && adder.getPriority() == unit.getPriority()))
                ) {
                    targets.add(i, target);
                    return;
                }
            }
        }
        targets.add(target);
    }


    /////////////////////////////////////////////////////////////////
    // Make sure to order targets by sequence. Prevents desynch
    /////////////////////////////////////////////////////////////////
    private void addTarget(Vector<Unit> targets, Unit adder) {
        for (int i = 0; i < targets.size(); i++) {
            Unit unit = targets.elementAt(i);
            if
            (unit != null &&
                    (unit != null &&
                            (adder.getPriority() > unit.getPriority() ||
                                    (adder.getSequence() < unit.getSequence() && adder.getPriority() == unit.getPriority()))
                    )
            ) {
                targets.add(i, adder);
                return;
            }
        }
        targets.add(adder);
    }


    /////////////////////////////////////////////////////////////////
    // Get each direction
    /////////////////////////////////////////////////////////////////
    private Vector<Short> getAllLines(
            Unit unit,
            short startingPoint,
            short range,
            boolean jumps,
            boolean locations,
            boolean units,
            boolean organic,
            boolean inorganic,
            short selectionType,
            Castle castle) {
        Vector<Short> targets = new Vector<Short>();
        Vector<Short> tmp = new Vector<Short>();

        // Where ye stands
        tmp = getArea(unit, unit.getLocation(), (byte) 0, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Up
        tmp = getLine(unit, startingPoint, range, 0, -1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Up/Right
        tmp = getLine(unit, startingPoint, range, 1, -1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Right
        tmp = getLine(unit, startingPoint, range, 1, 0, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Right/Down
        tmp = getLine(unit, startingPoint, range, 1, 1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Down
        tmp = getLine(unit, startingPoint, range, 0, 1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Down/Left
        tmp = getLine(unit, startingPoint, range, -1, 1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Left
        tmp = getLine(unit, startingPoint, range, -1, 0, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Up/Left
        tmp = getLine(unit, startingPoint, range, -1, -1, jumps, locations, units, organic, inorganic, selectionType, castle);
        for (int i = 0; i < tmp.size(); i++) {
            addTarget(targets, tmp.elementAt(i));
        }

        // Give them up
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get line
    /////////////////////////////////////////////////////////////////
    private Vector<Short> getLine(
            Unit unit,
            short startingPoint,
            short range,
            int byX,
            int byY,
            boolean jumps,
            boolean locations,
            boolean units,
            boolean organic,
            boolean inorganic,
            short selectionType,
            Castle castle) {
        Vector<Short> targets = new Vector<Short>();
        short x = BattleField.getX(startingPoint);
        short y = BattleField.getY(startingPoint);

        for (int i = 0; i < range; i++) {
            x += byX;
            y += byY;
            short location = BattleField.getLocation(x, y);
            Unit obstacle = getUnitAt(location);
            // exception for moving onto powerups
            if (obstacle != null)
                if (selectionType == TargetType.MOVE && obstacle.isPowerUp())
                    addTarget(targets, new Short(location));
            ////////////////////////////////////////
            if (x >= 0 && x < Constants.BOARD_SIZE && y >= 0 && y < Constants.BOARD_SIZE)
                if ((location != unit.getCastle().getLocation() || units) && (obstacle == null && locations) || (obstacle != null && units))
                    if ((obstacle == null) || (obstacle != null && obstacle.targetable(unit) && (organic == obstacle.getOrganic(unit) || inorganic != obstacle.getOrganic(unit)) &&
                            (selectionType == TargetType.BOTH || obstacle.getID() == Unit.ROCK || (selectionType == TargetType.FRIENDLY && obstacle.getCastle() == castle) || (selectionType == TargetType.ENEMY && obstacle.getCastle() != castle))))
                        addTarget(targets, new Short(location));

            // If they don't jump, give them what they've got so far (unless obstacle is a powerup)
            if (!jumps && obstacle != null && !obstacle.isPowerUp()) return targets;
        }
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // The movement event
    /////////////////////////////////////////////////////////////////
    public short move(Unit walker, short from, short to) {
        short result = event(
                Event.PREVIEW_MOVE,
                walker,
                null,
                from,
                to,
                Event.OK);

        if (result == Event.OK) {
            walker.setLocation(to);
            event(Event.WITNESS_MOVE, walker, null, from, to, Event.OK);
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Get the vector of units
    /////////////////////////////////////////////////////////////////
    public Vector<Unit> getUnits(Castle castle) {
        Vector<Unit> returnVector = new Vector<Unit>();
        Iterator<Unit> it = castle.getUnitsOut().iterator();
        while (it.hasNext()) {
            Unit tmp = it.next();
            if (tmp.getCastle() == castle) {
                addTarget(returnVector, tmp);
            }
            //returnVector.add(it.next());
        }
        return returnVector;
    }


    /////////////////////////////////////////////////////////////////
    // Get the vector of units
    /////////////////////////////////////////////////////////////////
    public Vector<Unit> getUnits() {
        Vector<Unit> returnVector = new Vector<Unit>();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit tmp = it.next();
            addTarget(returnVector, tmp);
            //returnVector.add(it.next());
        }
        return returnVector;
    }


    /////////////////////////////////////////////////////////////////
    // Fire an event
    /////////////////////////////////////////////////////////////////
    public short event(short type, Unit source, Unit target, short val1, short val2, short defaultResult) {
        short result = defaultResult;
        Vector<Event> events = new Vector<Event>();

        // build a list
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            Vector<Event> tmp = unit.getEvents(type);
            if (tmp != null) addEvents(events, tmp);
        }

        // perform the list
        Iterator<Event> et = events.iterator();
        while (et.hasNext()) {
            Event event = et.next();
            result = event.perform(source, target, val1, val2, result);
            if (result == Event.CANCEL || result == Event.DEAD || result == Event.END || result == Event.PARRY) {
     /*try
     { Thread.sleep(50);
     }catch (Exception e){}*/
                return result;
            }
        }
  /*try
  { Thread.sleep(50);
  }catch (Exception e){}*/
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Add events
    /////////////////////////////////////////////////////////////////
    private void addEvents(Vector<Event> events, Vector<Event> newList) {
        Iterator<Event> it = newList.iterator();
        while (it.hasNext()) {
            Event event = it.next();
            addEvent(events, event);
        }
    }

    private void addEvent(Vector<Event> events, Event event) {
        for (int i = 0; i < events.size(); i++) {
            Event tmp = events.elementAt(i);
            if
            (event.getPriority() > tmp.getPriority() ||
                    (event.getOwner().getSequence() < tmp.getOwner().getSequence() && event.getPriority() == tmp.getPriority())
            ) {
                events.add(i, event);
                return;
            }
        }
        events.add(event);
    }


    /////////////////////////////////////////////////////////////////
    // Get the unit at a specific location
    /////////////////////////////////////////////////////////////////
    public Unit getUnitAt(short x, short y) {
        short location = BattleField.getLocation(x, y);
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getLocation() == location) return unit;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Get the unit at a specific location
    /////////////////////////////////////////////////////////////////
    public Unit getUnitAt(short location) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getLocation() == location) return unit;
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////
    // Get all units at the location. Should only ever be 1,
    //   but in some cases (eg powerups) will momentarily be more.
    /////////////////////////////////////////////////////////////////
    public Vector<Unit> getUnitsAt(short location) {
        Vector<Unit> results = new Vector<Unit>();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getLocation() == location) results.add(unit);
        }
        return results;
    }


    /////////////////////////////////////////////////////////////////
    // Get the unit at a specific location
    /////////////////////////////////////////////////////////////////
    public Hero getHeroAt(short location) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getLocation() == location) {
                return (Hero) unit;
            }
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Add a new unit
    /////////////////////////////////////////////////////////////////
    public void add(Unit newUnit) {
        units.add(newUnit);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a unit
    /////////////////////////////////////////////////////////////////
    public void remove(Unit oldUnit) {
        units.remove(oldUnit);
    }


    /////////////////////////////////////////////////////////////////
    // Static utilities - get x from location
    /////////////////////////////////////////////////////////////////
    public static short getX(short location) {
        return (byte) (location % (byte) 11);
    }


    /////////////////////////////////////////////////////////////////
    // Static utilities - get y from location
    /////////////////////////////////////////////////////////////////
    public static short getY(short location) {
        return (byte) (location / (byte) 11);
    }


    /////////////////////////////////////////////////////////////////
    // Get location
    /////////////////////////////////////////////////////////////////
    public static short getLocation(short x, short y) {
        return (byte) ((y * (byte) 11) + x);
    }


    /////////////////////////////////////////////////////////////////
    // Get location
    /////////////////////////////////////////////////////////////////
    public static short getDistance(short location1, short location2) {
        short x1 = getX(location1);
        short x2 = getX(location2);
        short y1 = getY(location1);
        short y2 = getY(location2);

        short difference1 = (byte) Math.abs(x1 - x2);
        short difference2 = (byte) Math.abs(y1 - y2);
        return difference1 > difference2 ? difference1 : difference2;
    }


    /////////////////////////////////////////////////////////////////
    // is the target in range?
    /////////////////////////////////////////////////////////////////
    public static boolean inRange(Action action, short target) {
        Vector<Short> targets = action.getTargets();
        Iterator<Short> it = targets.iterator();
        while (it.hasNext()) {
            Short location = it.next();
            if (location.byteValue() == target) return true;
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public Castle getCastle1() {
        return castle1;
    }

    public Castle getCastle2() {
        return castle2;
    }


}
