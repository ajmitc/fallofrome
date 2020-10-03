package fallofrome.game.table;

import fallofrome.game.CombatResult;
import fallofrome.game.Force;

public class CombatResultsTable {

    public static CombatResult get(int ratio){
        return CombatResult.DEFENDER_ELINIMATED;
    }

    private CombatResultsTable(){}
}
