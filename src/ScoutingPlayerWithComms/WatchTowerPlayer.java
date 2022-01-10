package ScoutingPlayerWithComms;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
//import java.util.Random;

strictfp class WatchTowerPlayer {
    //static Direction[] directions = RobotPlayer.directions;
    //static Random rng = new Random();

    static void runWatchTower(RobotController rc) throws GameActionException{
        int radius = rc.getType().visionRadiusSquared;
        MapLocation toAttack = null;
        RobotInfo[] enemies = rc.senseNearbyRobots(radius, rc.getTeam().opponent());
        if(enemies.length > 0){
            toAttack = enemies[0].location;
            if(rc.canAttack(toAttack)){
                rc.attack(toAttack);
                rc.setIndicatorString("Attacking");
            }
        }
    }
}
