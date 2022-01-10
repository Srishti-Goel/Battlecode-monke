package ScoutingPlayerWithCommsWithDefense;

import battlecode.common.*;

public class LaboratoryPlayer {
    static void runLaboratory(RobotController rc) throws GameActionException {
        if(rc.getTeamLeadAmount(rc.getTeam()) > 5000 && rc.canTransmute()){
            rc.transmute();
        }
    }
}
