package SamplePlayerWithBSW;

import battlecode.common.*;

import java.util.*;

import static SamplePlayerWithBSW.MoveStrategy.move;

strictfp class BuilderPlayer {

    static Direction[] directions = RobotPlayer.directions;
    static Random rng = RobotPlayer.rng;

    static int watchTowers = 0;
    static Direction exploreDirection = null;

    static void runBuilder(RobotController rc, int turnCount) throws GameActionException{

        if(exploreDirection == null){
            exploreDirection = directions[rng.nextInt(directions.length)];
        }

        MapLocation me = rc.getLocation();
        for(int dx = -1; dx <2; dx++){
            for(int dy = -1; dy < 2; dy++){
                MapLocation repLocation = new MapLocation(me.x + dx, me.y + dy);
                while(rc.canRepair(repLocation)){
                    rc.repair(repLocation);
                }
            }
        }

        Team us = rc.getTeam();

        RobotInfo[] nearby = rc.senseNearbyRobots();
        Direction dir = null;
        int targetDist = Integer.MAX_VALUE;

        for(RobotInfo robot : nearby){
            if(robot.team.equals(us) && robot.type.isBuilding() && robot.health < robot.type.getMaxHealth(robot.level)){
                if(targetDist > me.distanceSquaredTo(robot.location)){
                    targetDist = me.distanceSquaredTo(robot.location);
                    dir = me.directionTo(robot.location);
                }
            }
        }
        if(dir != null){
            move(rc, dir);
            if(turnCount % 100 == 0 && rc.getTeamLeadAmount(us) > 7000 && rc.canBuildRobot(RobotType.WATCHTOWER, dir)){
                rc.buildRobot(RobotType.WATCHTOWER, dir.opposite());
            }
        }
        else{
            move(rc);
        }


    }

}
