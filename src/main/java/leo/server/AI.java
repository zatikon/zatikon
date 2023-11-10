///////////////////////////////////////////////////////////////////////
// Name: AI
// Desc: The Artificial Idiot
// Date: 5/8/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.server.game.Game;
import leo.server.script.*;
import leo.shared.*;
import leo.shared.crusades.*;

import java.util.Iterator;
import java.util.Vector;

public class AI {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int level;
    private final int maxCost;
    private final Game game;
    private final BattleField bf;
    private final Castle castle;
    private final Castle enemyCastle;
    private int turns = 0;
    private long points;
    private short want;
    private int diminish;
    private final DNA dna;
    private int consecutiveRelics;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AI(int newLevel, Game newGame, BattleField newBf, Castle newCastle, Castle newEnemyCastle) {
        if (newLevel <= 8) {
            level = newLevel;
        } else {
            level = 8 + ((newLevel - 8) / 3);
        }

        maxCost = 100 + (level * 10);

        game = newGame;
        bf = newBf;
        castle = newCastle;
        enemyCastle = newEnemyCastle;

        consecutiveRelics = 0;

        dna = new DNA(game.random(), level);

        // points
        points = 300 + (15 * level);

        diminish = level / 5;
        if (diminish < 1) diminish = 1;
    }


    /////////////////////////////////////////////////////////////////
    // Compute turn
    /////////////////////////////////////////////////////////////////
    public void computeTurn() {
        String lastUnit = "";
        Vector<Unit> units = null;
        try {

            // purge the castle
            Vector<UndeployedUnit> barracks = castle.getBarracks();
            for (int i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = barracks.elementAt(i);
                for (int c = 0; c < unit.count(); c++) {
                    Unit refund = unit.getUnit();
                    if (refund.getCastleCost() != 1001) points += refund.getCastleCost() / 2;
                }
            }
            castle.clear();

            // collect a list of what we've got
            units = listUnits();
            cleanScripts(units);

        } catch (Exception e) {
            Log.error("Script Error1: " + lastUnit + ", " + e);
        }
        try {

            // deploy new units
            if (turns == 0) {
                want = UnitType.GATE_GUARD.value();
                deployUnits(units);
            }

            while (!game.over() && deployUnits(units)) {
            }

        } catch (Exception e) {
            Log.error("Script Error2: " + lastUnit + ", " + e);
        }

        try {

            // use units
            Iterator<Unit> it = units.iterator();
            while (!game.over() && it.hasNext()) {
                if (game.over()) return;
                Unit unit = it.next();
                lastUnit = unit.getName();
                unit.getScript().perform();
                if (unit.boss() && !unit.isDead() && (bf.getUnitAt(unit.getLocation()) != null))
                    unit.getScript().perform();
            }
            turns++;
            points += calculatePointsIncrease(level, turns);

        } catch (Exception e) {
            Log.error("Script Error3: " + lastUnit + ", " + e);
        }
    }

    public static long calculatePointsIncrease(int aiLevel, int turnCount) {
        var exhaustionRatio = (double) (aiLevel+9) / (aiLevel+10);
        var exhaustionFactor = Math.pow(exhaustionRatio, turnCount);

        // Mostly trying to keep the original formula, but with some shifts
        var minimumIncrease = 20 + aiLevel * 2;

        return Math.round((101 + (4 * aiLevel)) * exhaustionFactor) + minimumIncrease;
    }


    /////////////////////////////////////////////////////////////////
    // Clean up the scripts
    /////////////////////////////////////////////////////////////////
    public void cleanScripts(Vector<Unit> units) {
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getScript() == null) {
                if (unit instanceof UnitGateGuard)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitWarrior)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitShaman)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitChangeling)
                    unit.setScript(new ScriptChangeling(unit, game, castle, enemyCastle, true, this));
                else if (unit instanceof UnitFireArcher)
                    unit.setScript(new ScriptFireArcher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitWarlock)
                    unit.setScript(new ScriptWarlock(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitDragon)
                    unit.setScript(new ScriptDragon(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitTemplar)
                    unit.setScript(new ScriptTemplar(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitHydra)
                    unit.setScript(new ScriptHydra(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitLycanthrope)
                    unit.setScript(new ScriptLycan(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitLycanWolf)
                    unit.setScript(new ScriptLycan(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitWerewolf)
                    unit.setScript(new ScriptLycan(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitCrossbowman)
                    unit.setScript(new ScriptCrossbowman(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitChanneler)
                    unit.setScript(new ScriptChanneler(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitSummoner)
                    unit.setScript(new ScriptSummoner(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitMagus)
                    unit.setScript(new ScriptMagus(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitDruid)
                    unit.setScript(new ScriptDruid(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitNecromancer)
                    unit.setScript(new ScriptNecromancer(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitFeatheredSerpent)
                    unit.setScript(new ScriptCoward(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitSycophant)
                    unit.setScript(new ScriptCoward(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitMourner)
                    unit.setScript(new ScriptMourner(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitConfessor)
                    unit.setScript(new ScriptMourner(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitHealer)
                    unit.setScript(new ScriptHealer(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitWitch)
                    unit.setScript(new ScriptWitch(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitDoppelganger)
                    unit.setScript(new ScriptDoppelganger(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitAbjurer)
                    unit.setScript(new ScriptAbjurer(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitCatapult)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, false));
                else if (unit instanceof UnitBallista)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, false));
                else if (unit instanceof UnitArcher)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitBowman)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitFootman)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitBerserker)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitSpirit)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitWillWisps)
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
                else if (unit instanceof UnitPaladin)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitWarElephant)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitMartyr)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitRider)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitCavalry)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitMountedArcher)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (unit instanceof UnitToad)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else if (game.random().nextInt(2) == 0)
                    unit.setScript(new ScriptRusher(unit, game, castle, enemyCastle));
                else
                    unit.setScript(new ScriptInterceptor(unit, game, castle, enemyCastle, true));
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Strip away targets
    /////////////////////////////////////////////////////////////////
    public static Vector<Short> refineRange(Vector<Short> moves, int closest, short targetLocation) {
        Vector<Short> newTargets = new Vector<Short>();
        Iterator<Short> it = moves.iterator();
        while (it.hasNext()) {
            Short targ = it.next();
            if (BattleField.getDistance(targ.byteValue(), targetLocation) == closest)
                newTargets.add(targ);
        }
        return newTargets;
    }


    /////////////////////////////////////////////////////////////////
    // Closest target range
    /////////////////////////////////////////////////////////////////
    public static int getClosestRange(Vector<Short> targets, short target) {
        Iterator<Short> it = targets.iterator();
        int closest = 100;
        while (it.hasNext()) {
            Short loc = it.next();
            int dist = BattleField.getDistance(target, loc.byteValue());
            if (dist < closest) closest = dist;
        }
        return closest;
    }


    /////////////////////////////////////////////////////////////////
    // Closest target range
    /////////////////////////////////////////////////////////////////
    public static int getFarthestRange(Vector<Short> targets, short target) {
        Iterator<Short> it = targets.iterator();
        int closest = 0;
        while (it.hasNext()) {
            Short loc = it.next();
            int dist = BattleField.getDistance(target, loc.byteValue());
            if (dist >= closest) closest = dist;
        }
        return closest;
    }


    /////////////////////////////////////////////////////////////////
    // Deploy units
    /////////////////////////////////////////////////////////////////
    private boolean deployUnits(Vector<Unit> units) {

        Unit unit = Unit.getUnit(want, castle);

        if (points < unit.getCastleCost()) return false;

        Vector<Short> locations = unit.getCastleTargets();

        if (locations.size() < 1) {
            // Don't get stuck with relics you can't deploy, replace with a unit
            if (unit.getID() <= UnitType.POWERUP.value()) {
                want = getNewWant();
                return deployUnits(units);
            }
            return false;
        }


        Short location = 0;
        // Deploy militia as far up as you can
        if (unit.getID() == UnitType.MILITIA.value()) {
            Iterator it = locations.iterator();
            while (it.hasNext()) {
                Short loc = (Short) it.next();
                if (location < 1 || BattleField.getY(loc) > BattleField.getY(location))
                    location = loc;
            }
        } else {
            location = locations.elementAt(game.random().nextInt(locations.size()));
        }

        try {

            castle.add(unit);

        } catch (Exception e) {
            Log.error("deployUnits: " + e);
            return false;
        }
        castle.deploy(Unit.TEAM_1, unit, location.byteValue());
        interpretAction(Action.DEPLOY, unit.getID(), location.byteValue());
        points -= unit.getCastleCost();

        // Old grow style
  /*if ( game.random().nextInt(6) == 0 || 
   points > 2000)
   grow(unit);*/

        // Get a new want, either relic or unit
        if (consecutiveRelics < 3 && (game.random().nextInt(4) == 0 || points > 2000)) {
            want = getNewRelic();
        } else {
            want = getNewWant();
        }

        return true;
        //}catch( Exception e ){
        //   Log.error("deployUnits: " + e);
        //   return false;
        //}
    }


    /////////////////////////////////////////////////////////////////
    // Grow a unit
    /////////////////////////////////////////////////////////////////
 /*public void grow(Unit unit)
 { if (!unit.getOrganic(unit)) return;
  if (unit.getID() == Unit.GATE_GUARD) return;
  if (level < 10) return;
  return;
  
  short bonus = (byte) game.random().nextInt(Action.GROW_AI_COUNT);

  if (game.random().nextInt(3) != 0 && enemyHas(Unit.PRIEST))
  { bonus = Action.GROW_CLOCKWORK;
  }
  boolean targetable = false;
  int i = 0;
  Unit power = Unit.getPowerUp(bonus,castle);
  while(!targetable)
  {
    if (i > 0) bonus = (byte) game.random().nextInt(Action.GROW_AI_COUNT);
    power = Unit.getPowerUp(bonus,castle);
    Vector<Short> targets = power.getCastleTargets();
    if (targets.contains(unit.getLocation())) targetable = true;
    i++;
  }
  
  points-=Action.GROW_COST[bonus];
  interpretAction(Action.GROW, unit.getLocation(), bonus);
 }*/


    /////////////////////////////////////////////////////////////////
    // Get a new unit
    /////////////////////////////////////////////////////////////////
    private short getNewWant() {
        short newWant = dna.getUnit();

        if (Unit.getUnit(newWant, castle).getCastleCost() > maxCost) {
            return getNewWant();
        }

        consecutiveRelics = 0;
        return newWant;
    }

    /////////////////////////////////////////////////////////////////
    // Get a new relic
    /////////////////////////////////////////////////////////////////
    private short getNewRelic() {
        short newWant = dna.getRelic();

        if (Unit.getUnit(newWant, castle).getCastleCost() > maxCost) {
            return getNewWant();
        }

        consecutiveRelics++;   // Don't want to overload on relics or won't
        return newWant;        //    be enough units to use them on
    }


    /////////////////////////////////////////////////////////////////
    // Is the bad man there?
    /////////////////////////////////////////////////////////////////
    private boolean enemyHas(short theUnit) {
        Iterator<Unit> it = bf.getUnits().iterator();

        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getCastle() != castle && unit.getID() == theUnit)
                return true;
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // List available units
    /////////////////////////////////////////////////////////////////
    private Vector<Unit> listUnits() {
        Vector<Unit> units = new Vector<Unit>();
        Iterator<Unit> it = bf.getUnits().iterator();

        while (it.hasNext()) {
            Unit unit = it.next();
            if (unit.getCastle() == castle && unit.ready())
                units.add(unit);
        }
        return units;
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("AI.interpretAction " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // add points
    /////////////////////////////////////////////////////////////////
    public void addPoints(int addition) {
        points += addition;
    }


    /////////////////////////////////////////////////////////////////
    // Get the level
    /////////////////////////////////////////////////////////////////
    public int getLevel() {
        return level;
    }
}
