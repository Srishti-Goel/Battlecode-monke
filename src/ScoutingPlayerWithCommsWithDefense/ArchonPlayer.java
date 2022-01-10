package ScoutingPlayerWithCommsWithDefense;

import battlecode.common.*;

import java.util.Random;

public class ArchonPlayer {
    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();

    static int miners = 0, soldiers = 0, builders = 0, sages = 0;

    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */

    static void runArchon(RobotController rc) throws GameActionException {

        if(miners == 0){
            rc.writeSharedArray(0, 0);
        }

        Direction dir = directions[rng.nextInt(directions.length)];
        Team us = rc.getTeam();
        Team opponent = rc.getTeam().opponent();
        int radius = rc.getType().visionRadiusSquared;

        if ( rng.nextBoolean() && SensingNearby.senseSafety(rc, rc.getLocation()) > 0) {
            rc.setIndicatorString("Miner pregnancy");
            if (rc.canBuildRobot(RobotType.MINER, dir)) {
                rc.buildRobot(RobotType.MINER, dir);
                miners++;

                dir = directions[rng.nextInt(directions.length)];

                if(rc.canBuildRobot(RobotType.SOLDIER, dir)){
                    rc.buildRobot(RobotType.SOLDIER, dir);
                    rc.setIndicatorString("Soldier pregnancy " + soldiers);
                    soldiers++;
                }

                if(miners == 0){
                    rc.writeSharedArray(1, 0);
                }
                else if(miners == 5){
                    rc.writeSharedArray(1, 1);
                }
            }
        }
        else {
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
                rc.setIndicatorString("Soldier pregnancy " + soldiers);
                soldiers++;
            }
        }

        if(rc.getTeamGoldAmount(us) > rc.getTeamLeadAmount(opponent) + 50){
            rc.setIndicatorString("Soldier pregnancy");
            dir = directions[rng.nextInt(directions.length)];
            if(rc.canBuildRobot(RobotType.SOLDIER, dir)){
                rc.buildRobot(RobotType.SOLDIER, dir);
                sages++;
            }
        }
        if(rc.getTeamLeadAmount(us) > rc.getTeamLeadAmount(opponent) + 500){
            dir = directions[rng.nextInt(directions.length)];
            rc.setIndicatorString("Builder pregnancy");
            if(rc.canBuildRobot(RobotType.BUILDER, dir)){
                rc.buildRobot(RobotType.BUILDER, dir);
                builders++;
            }
        }
    }
}
