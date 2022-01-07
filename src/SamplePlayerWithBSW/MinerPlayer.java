package SamplePlayerWithBSW;

import battlecode.common.*;
import java.util.Random;
import static SamplePlayerWithBSW.MoveStrategy.move;

strictfp class MinerPlayer {

    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();

    static Direction exploreDir = null;

    /**
     * Run a single turn for a Miner.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    public static void runMiner(RobotController rc) throws GameActionException {

        if(exploreDir == null){
            exploreDir = directions[rng.nextInt(directions.length)];
        }

        MapLocation me = rc.getLocation();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                MapLocation mineLocation = new MapLocation(me.x + dx, me.y + dy);
                // Notice that the Miner's action cooldown is very low.
                // You can mine multiple times per turn!
                while (rc.canMineGold(mineLocation) && rc.senseLead(mineLocation) > 1) {
                    rc.mineGold(mineLocation);
                }
                while (rc.canMineLead(mineLocation)) {
                    rc.mineLead(mineLocation);
                }
            }
        }

        int visionRadius = rc.getType().visionRadiusSquared;
        MapLocation[] seeableLocations = rc.getAllLocationsWithinRadiusSquared(me, visionRadius);
        int targetDistance = Integer.MAX_VALUE;
        Direction targetdir = null;

        for(MapLocation activeLocation : seeableLocations ){
            if(rc.senseGold(activeLocation) > 0 || rc.senseLead(activeLocation) > 0){
                if(me.distanceSquaredTo(activeLocation) < targetDistance){
                    targetDistance = me.distanceSquaredTo(activeLocation);
                    targetdir = me.directionTo(activeLocation);
                }
            }
        }
        if(targetdir != null)
            move(rc, targetdir, exploreDir);
        else
            move(rc, exploreDir);
    }
}
