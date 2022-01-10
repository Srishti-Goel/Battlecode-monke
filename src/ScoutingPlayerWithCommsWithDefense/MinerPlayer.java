package ScoutingPlayerWithCommsWithDefense;

import battlecode.common.*;

import java.util.Random;

strictfp class MinerPlayer {
  static final Direction[] directions = RobotPlayer.directions;
  static final Random rng = new Random();

  static Direction exploreDir = null;

  /**
   * Run a single turn for a Miner.
   * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
   */
  public static void runMiner(RobotController rc) throws GameActionException {
    if (exploreDir == null) {
      exploreDir = directions[rng.nextInt(directions.length)];
    }
    
    MapLocation me = rc.getLocation();
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
        // Notice that the Miner's action cooldown is very low.
        // You can mine multiple times per turn!
        while (rc.canMineGold(mineLocation)) {
          rc.setIndicatorString("Mining Gold");
          rc.mineGold(mineLocation);
        }
        while (rc.canMineLead(mineLocation) && rc.senseLead(mineLocation) > 1) {
          rc.setIndicatorString("Mining Lead");
          rc.mineLead(mineLocation);
        }
      }
    }

    int visionRadius = rc.getType().visionRadiusSquared;
    MapLocation[] seeableLocations = rc.getAllLocationsWithinRadiusSquared(me,visionRadius);
    int targetDistance = Integer.MAX_VALUE;
    Direction targetdir = null;

    for (MapLocation activeLocation : seeableLocations) {
      if (
        rc.senseGold(activeLocation) > 0 || rc.senseLead(activeLocation) > 1
      ) {
        if (me.distanceSquaredTo(activeLocation) < targetDistance) {
          targetDistance = me.distanceSquaredTo(activeLocation);
          targetdir = me.directionTo(activeLocation);
        }
      }
    }
      rc.setIndicatorString("Moving" + exploreDir.toString());
      if (targetdir != null && rc.canMove(targetdir)) {
        rc.move(targetdir);
      } 
      else if (rc.canMove(exploreDir)) {
        rc.move(exploreDir);
      } 
      else {
        exploreDir = exploreDir.opposite();
        if (rc.canMove(exploreDir)) {
          rc.move(exploreDir);
        } else {
            exploreDir = directions[rng.nextInt(directions.length)];
        }
      }

    SensingNearby.Scout(rc);

  }

  static MapLocation ArchonFound = null;
  static MapLocation targetArchon = null;

  static void runScout(RobotController rc)
    throws GameActionException {
    if (exploreDir == null) {
      exploreDir = directions[rng.nextInt(directions.length)];
    }

    if (targetArchon == null && ArchonFound != null) {
      int alreadyFound = rc.readSharedArray(0);
      rc.writeSharedArray((2 + (2 * alreadyFound)), ArchonFound.x);
      rc.writeSharedArray((3 + (2 * alreadyFound)), ArchonFound.y);
      System.out.println(ArchonFound.toString());
      return;
    }
      if (SensingNearby.senseArchonToAttack(rc) != null) {
        ArchonFound = SensingNearby.senseArchonToAttack(rc);
        System.out.println("Enemy Archon found! " + ArchonFound.toString());
        int alreadyFound = rc.readSharedArray(0);

        rc.writeSharedArray((2 + (2 * alreadyFound)), ArchonFound.x);
        rc.writeSharedArray((3 + (2 * alreadyFound)), ArchonFound.y);
        int alreadyFoundArchons = rc.readSharedArray(0);
        rc.writeSharedArray(0, alreadyFoundArchons+1);

      }
  }
}
