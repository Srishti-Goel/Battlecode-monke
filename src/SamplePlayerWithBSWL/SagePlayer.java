package SamplePlayerWithBSWL;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import java.util.Random;

import static SamplePlayerWithBSWL.MoveStrategy.move;

public class SagePlayer {

    static final Direction[] directions = RobotPlayer.directions;
    static final Random rng = new Random();

    static void runSage(RobotController rc) throws GameActionException {
        move(rc);
    }
}
