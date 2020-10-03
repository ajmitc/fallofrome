package fallofrome.game.table;

import fallofrome.game.GamePeriod;
import fallofrome.util.Util;

public class LegionRebellionTable {

    public static int getNumRebellingLegions(GamePeriod gamePeriod, int numLegions){
        if (numLegions <= 3)
            return 0;

        int numRebellingLegions = 0;
        int die = Util.roll();

        // Modify die by gamePeriod
        switch (gamePeriod){
            case A:
                die -= 1;
                break;
            case B:
            case E:
                die += 1;
                break;
            case F:
                die += 2;
                break;
            case C:
            case D:
            case G:
                break;
        }

        switch (die){
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }

        return numRebellingLegions;
    }

    private LegionRebellionTable(){}
}
