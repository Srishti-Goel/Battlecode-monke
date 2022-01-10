package ScoutingPlayerWithCommsWithDefense;

import battlecode.common.*;

public class SensingNearby {
    static int senseSafety(RobotController rc, MapLocation location){

        int radius = rc.getType().visionRadiusSquared;

        RobotInfo[] allies = rc.senseNearbyRobots(location, radius, rc.getTeam());
        RobotInfo[] enemies = rc.senseNearbyRobots(location, radius, rc.getTeam().opponent());

        int allies_HP = findAttackingHP(allies), enemy_HP = findAttackingHP(enemies);

        return allies_HP - enemy_HP;
    }

    static int findAttackingHP(RobotInfo[] robots){
        int total_HP = 0;
        for(RobotInfo robot : robots){
            if(RobotPlayer.canAttack(robot)){
                total_HP += robot.getHealth();
            }
        }
        return total_HP;
    }

    static MapLocation senseArchonToAttack(RobotController rc){
        RobotInfo[] robotsNearby = rc.senseNearbyRobots();
        for(RobotInfo robot : robotsNearby){
            if(robot.type == RobotType.ARCHON && robot.team == rc.getTeam().opponent()){
                return robot.location;
            }
        }
        return null;
    }

    static int isInSharedArray(RobotController rc, MapLocation ArchonFound)throws GameActionException {
        int alreadyFound = rc.readSharedArray(0);
        for(int i = 0; i < alreadyFound; i++){
            if(rc.readSharedArray(2+(2*i)) == ArchonFound.x && rc.readSharedArray(3 + (2*i)) == ArchonFound.y){
                return i;
            }
        }
        return -1;
    }

    static void Scout(RobotController rc)
    throws GameActionException {

        MapLocation ArchonFound = SensingNearby.senseArchonToAttack(rc);

      if (ArchonFound != null) {
        
        if(isInSharedArray(rc, ArchonFound) == -1){
            System.out.println("Enemy Archon found! " + ArchonFound.toString());
            
            int alreadyFound = rc.readSharedArray(0);

            rc.writeSharedArray((2 + (2 * alreadyFound)), ArchonFound.x);
            rc.writeSharedArray((3 + (2 * alreadyFound)), ArchonFound.y);
            int alreadyFoundArchons = rc.readSharedArray(0);
            rc.writeSharedArray(0, alreadyFoundArchons+1);
        }
      }
  }
}
