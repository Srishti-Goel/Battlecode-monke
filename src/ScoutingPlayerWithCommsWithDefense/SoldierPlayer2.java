package ScoutingPlayerWithCommsWithDefense;

import battlecode.common.*;

import java.util.Random;

import static ScoutingPlayerWithCommsWithDefense.MoveStrategy.move;

public class SoldierPlayer2 {
    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();

    static Direction exploreDir = null;
    static MapLocation defendingLocation = null;
    static int soldier_mode = -1;

    // 0 = Attacker, 1 = Defender

    /**
     * Run a single turn for a Soldier.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runSoldier(RobotController rc) throws GameActionException {

        if(soldier_mode == -1){
            int randomness = rng.nextInt(100);
            if(randomness < 20){
                soldier_mode = 1;
            }
            else{
                soldier_mode = 0;
            }
        }

        int radius = rc.getType().visionRadiusSquared;
        Team opponent = rc.getTeam().opponent();
        MapLocation me = rc.getLocation();
        MapLocation toAttack = null;
        int targetDistance = Integer.MAX_VALUE;
        Direction targetDir = null;

        int archonsFound = rc.readSharedArray(0);

        if(soldier_mode == 1){
            rc.setIndicatorString("Defending " + defendingLocation);
            RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);

            for (RobotInfo enemy : enemies) {
                if (me.distanceSquaredTo(enemy.location) < targetDistance) {
                    toAttack = enemy.location;
                    targetDir = me.directionTo(enemy.location);
                }
            }
        }
        else if(soldier_mode == 0){
            if(archonsFound == 0) {
                rc.setIndicatorString("Attacking random");
                RobotInfo[] enemies = rc.senseNearbyRobots(radius, opponent);

                for (RobotInfo enemy : enemies) {
                    if (me.distanceSquaredTo(enemy.location) < targetDistance) {
                        toAttack = enemy.location;
                        targetDir = me.directionTo(enemy.location);
                    }
                }
            }
            else{
                int archonToAttack = rng.nextInt(archonsFound);
                toAttack = new MapLocation(rc.readSharedArray( 2 + (archonToAttack *2)), rc.readSharedArray( 3 + (archonToAttack *2)));

                if(rc.getLocation().distanceSquaredTo(toAttack) < rc.getType().visionRadiusSquared){
                    RobotInfo enemyArchon = rc.senseRobotAtLocation(toAttack);
                    if(enemyArchon!= null && (enemyArchon.type != RobotType.ARCHON || enemyArchon.team != rc.getTeam().opponent())){
                        archonsFound--;

                        System.out.println("Enemy archon destroyed");

                        MapLocation copyingLoc = new MapLocation(rc.readSharedArray( 2 + (archonsFound *2)), rc.readSharedArray( 3 + (archonsFound *2)));
                        int removingIndex = SensingNearby.isInSharedArray(rc, toAttack);
                        rc.writeSharedArray(2 + (removingIndex *2), copyingLoc.x);
                        rc.writeSharedArray(3 + (removingIndex *2), copyingLoc.y);
                        rc.writeSharedArray(0, archonsFound);
                    }
                }

                targetDir = me.directionTo(toAttack);
                rc.setIndicatorString("Attacking archon no: " + (archonToAttack + 1));
            }
        }

        if(toAttack != null && rc.canAttack(toAttack)){
            rc.attack(toAttack);
        }

        // Also try to move toward it
        if (toAttack != null) {
            if(rc.canAttack(toAttack)){
                rc.attack(toAttack);
                //System.out.println("Target attacked!");
            }
            else if(rc.canMove(targetDir)){
                rc.move(targetDir);
                //System.out.println("Target locked");
            }
            else{
                Direction dir = directions[rng.nextInt(directions.length)];
                if (rc.canMove(dir)) {
                    rc.move(dir);
                    //System.out.println("I moved!");
                }
            }
        }
        else{
            Direction dir = null;
            if(defendingLocation == null){
                dir = directions[rng.nextInt(directions.length)];
            }
            else{
                dir = rc.getLocation().directionTo(defendingLocation);
            }
            if (rc.canMove(dir)) {
                rc.move(dir);
                //System.out.println("I moved!");
            }
        }
        SensingNearby.Scout(rc);
    }

    static MapLocation ArchonFound = null;

    /*    static void runScoutingSoldier(RobotController rc) throws GameActionException{
            if(exploreDir == null){
                exploreDir = directions[rng.nextInt(directions.length)];
            }

            if(ArchonFound != null){
                int alreadyFound = rc.readSharedArray(0);
                rc.writeSharedArray((1 + (2 * alreadyFound)), ArchonFound.x);
                rc.writeSharedArray((2 + (2 * alreadyFound)), ArchonFound.y);
                ArchonFound = null;
            }

            while(rc.isMovementReady()){
                if(SensingNearby.senseArchonToAttack(rc) != null){
                    ArchonFound = SensingNearby.senseArchonToAttack(rc);
                    System.out.println("Enemy Archon found!");
                    int alreadyFound = rc.readSharedArray(0);

                    rc.writeSharedArray((2 + (2 * alreadyFound)), ArchonFound.x);
                    rc.writeSharedArray((3 + (2 * alreadyFound)), ArchonFound.y);

                    ArchonFound = null;
                }
                if(rc.canMove(exploreDir)){
                    soldierMove(rc, exploreDir);
                }
                else{
                    exploreDir = directions[rng.nextInt(directions.length)];
                }
            }
        }
    */
    static void soldierMove(RobotController rc, Direction dir) throws GameActionException{
        if(rc.canMove(dir)){
            move(rc, dir);
        }
        int radius = rc.getType().actionRadiusSquared;
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, rc.getTeam().opponent());
        MapLocation targetLoc = null;
        int targetDistance = Integer.MAX_VALUE;
        MapLocation me = rc.getLocation();

        for (RobotInfo enemy : enemies) {
            if (me.distanceSquaredTo(enemy.location) < targetDistance) {
                targetLoc = enemy.location;
            }
        }
        if(targetLoc != null && rc.canAttack(targetLoc)){
            rc.attack(targetLoc);
        }
    }
}
