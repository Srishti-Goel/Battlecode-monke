package SamplePlayerWithBSW;

import battlecode.common.*;

import java.util.Random;

strictfp class MoveStrategy {
    static Direction[] directions = RobotPlayer.directions;
    static Random rng = RobotPlayer.rng;

    static void move(RobotController rc) throws GameActionException{
        Direction dir = directions[rng.nextInt(directions.length)];
        while(! rc.canMove(dir)){
            dir = directions[rng.nextInt(directions.length)];
        }
        if(rc.canMove(dir)) {
            rc.move(dir);
            return;
        }
        System.out.println("Something wrong with function 1");
    }
    static void move(RobotController rc, Direction targetDir) throws GameActionException{
        Direction dir = targetDir;
        while(! rc.canMove(dir)){
            dir = directions[rng.nextInt(directions.length)];
        }
        if(rc.canMove(dir)){
            rc.move(dir);
            return;
        }
        System.out.println("Something wrong in the 2nd function");
    }
    static void move(RobotController rc, Direction targetDir, Direction exploreDir) throws GameActionException{
        Direction dir = targetDir;
        if(rc.canMove(dir)){
            rc.move(dir);
            return;
        }
        else if(rc.canMove(exploreDir)){
            rc.move(exploreDir);
            return;
        }
        while(! rc.canMove(dir)){
            dir = directions[rng.nextInt(directions.length)];
        }
        if(rc.canMove(dir)){
            rc.move(dir);
            return;
        }
        System.out.println("Something wrong in 3rd function");
    }
}
