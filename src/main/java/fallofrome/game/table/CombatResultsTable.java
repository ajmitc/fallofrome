package fallofrome.game.table;

import fallofrome.game.CombatResult;
import fallofrome.game.Force;
import fallofrome.util.Util;

public class CombatResultsTable {

    public static CombatResult get(int ratio){
        if (ratio < 0)
            ratio = 1;
        if (ratio > 6)
            ratio = 6;
        int die = Util.roll();
        switch (ratio){
            case 1:
                switch (die){
                    case 1:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 2:
                        return CombatResult.HALF_EXCHANGE;
                    case 3:
                        return CombatResult.EXCHANGE;
                    case 4:
                    case 5:
                    case 6:
                        break;
                }
                break;
            case 2:
                switch (die){
                    case 1:
                    case 2:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 3:
                        return CombatResult.HALF_EXCHANGE;
                    case 4:
                        return CombatResult.EXCHANGE;
                    case 5:
                    case 6:
                        break;
                }
                break;
            case 3:
                switch (die){
                    case 1:
                        return CombatResult.DEFENDER_ELINIMATED;
                    case 2:
                    case 3:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 4:
                        return CombatResult.HALF_EXCHANGE;
                    case 5:
                    case 6:
                        break;
                }
                break;
            case 4:
                switch (die){
                    case 1:
                        return CombatResult.DEFENDER_ELINIMATED;
                    case 2:
                    case 3:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 4:
                        return CombatResult.HALF_EXCHANGE;
                    case 5:
                        return CombatResult.EXCHANGE;
                    case 6:
                        break;
                }
                break;
            case 5:
                switch (die){
                    case 1:
                    case 2:
                        return CombatResult.DEFENDER_ELINIMATED;
                    case 3:
                    case 4:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 5:
                        return CombatResult.HALF_EXCHANGE;
                    case 6:
                        break;
                }
                break;
            case 6:
                switch (die){
                    case 1:
                    case 2:
                        return CombatResult.DEFENDER_ELINIMATED;
                    case 3:
                    case 4:
                        return CombatResult.HALF_DEFENDER_ELIMINATED;
                    case 5:
                        return CombatResult.HALF_EXCHANGE;
                    case 6:
                        break;
                }
                break;
        }
        return null;
    }

    private CombatResultsTable(){}
}
