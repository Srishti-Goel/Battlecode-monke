package SamplePlayerWithBSW;

import battlecode.common.*;

import java.util.*;

public class ArchonPlayer {
    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();

    static int miners = 0, soldiers = 0, builders = 0;

    /**
     * Run a single turn for an Archon.
     * This code is wrapped inside the infinite loop in run(), so it is called once per turn.
     */
    static void runArchon(RobotController rc) throws GameActionException {
        // Pick a direction to build in.
        RobotType newRobot = RobotType.SOLDIER;

        Direction dir = directions[rng.nextInt(directions.length)];
        Team us = rc.getTeam();
        Team opponent = rc.getTeam().opponent();
        int radius = rc.getType().visionRadiusSquared;

        if (rng.nextBoolean()) {
            // Let's try to build a miner / building

            if(miners <= builders*2){
                rc.setIndicatorString("Trying to build a miner");
                buildTowardsLowestRubble(rc, RobotType.MINER);
            }
            else{
                rc.setIndicatorString("Trying to build builder");
                buildTowardsLowestRubble(rc, RobotType.BUILDER);
            }
        } else {
            if(builders < 1 || (builders < soldiers/10 && builders < miners/2)){
                rc.setIndicatorString("Trying to build builder");
                buildTowardsLowestRubble(rc, RobotType.BUILDER);
            }
            else{
                rc.setIndicatorString("Trying to build a soldier");
                buildTowardsLowestRubble(rc, RobotType.SOLDIER);
            }
        }

        buildTowardsLowestRubble(rc, RobotType.SAGE);

/*        Team us = rc.getTeam();
        Team opponent = rc.getTeam().opponent();
        int radius = rc.getType().visionRadiusSquared;
*/
    }

    static void buildTowardsLowestRubble(RobotController rc, RobotType type) throws GameActionException{
        Direction[] dirs = Arrays.copyOf(directions, directions.length);
        Arrays.sort(dirs, (a,b) -> getRubble(rc, a) - getRubble(rc, b));
        for(Direction dir : dirs){
            if(rc.canBuildRobot(type, dir)){
                rc.buildRobot(type, dir);
                switch(type){
                    case MINER: miners++; break;
                    case SOLDIER: soldiers++; break;
                    case BUILDER: builders++; break;
                    default: break;
                }
            }
        }
    }

    static int getRubble(RobotController rc, Direction dir){
        try{
            MapLocation newLoc = rc.getLocation().add(dir);
            return rc.senseRubble(newLoc);
        }
        catch( GameActionException e ){
            e.printStackTrace();
            return 0;
        }
    }

}
