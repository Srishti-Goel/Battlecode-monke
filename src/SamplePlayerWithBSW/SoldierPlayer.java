package SamplePlayerWithBSW;

import battlecode.common.*;

import java.util.Random;

import static SamplePlayerWithBSW.MoveStrategy.move;

public class SoldierPlayer {
    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();
    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runSoldier(RobotController rc) throws GameActionException {
        // Try to attack someone
        int radius = rc.getType().visionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        MapLocation me = rc.getLocation();
        MapLocation toAttack = null;
        int targetDistance = Integer.MAX_VALUE;
        Direction targetDir = null;

        RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);

        for(RobotInfo enemy : enemies){
            if(me.distanceSquaredTo(enemy.location) < targetDistance){
                toAttack = enemy.location;
                targetDir = me.directionTo(enemy.location);
            }
        }
        // Also try to move toward it
        if (toAttack != null) {
            if(rc.canAttack(toAttack)){
                rc.attack(toAttack);
                //System.out.println("Target attacked!");
            }
            else {
                move(rc);
            }
        }
        else{
            move(rc);
        }

    }

}
