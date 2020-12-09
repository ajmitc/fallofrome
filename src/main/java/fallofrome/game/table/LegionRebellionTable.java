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
                die -= 2;
                break;
            case B:
                die -= 1;
                break;
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

        if (die < -1)
            die = -1;
        if (die > 7)
            die = 7;

        switch (die){
            case -1:
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
                if (numLegions >= 10){
                    return (int) Math.ceil(numLegions * 0.2);
                }
                return 0;
            case 4:
                if (numLegions == 8 || numLegions == 9)
                    return 2;
                if (numLegions >= 10)
                    return (int) Math.ceil(numLegions * 0.3);
                return 0;
            case 5:
                if (numLegions == 5 || numLegions == 6)
                    return 2;
                if (numLegions >= 7 && numLegions <= 9)
                    return 3;
                if (numLegions >= 10)
                    return (int) Math.ceil(numLegions * 0.5);
                return 0;
            case 6:
                if (numLegions == 4)
                    return 3;
                if (numLegions == 5 || numLegions == 6)
                    return 5;
                if (numLegions == 7)
                    return 4;
                if (numLegions == 8 || numLegions == 9)
                    return 6;
                if (numLegions >= 10)
                    return (int) Math.ceil(numLegions * 0.9);
                return 0;
            case 7:
                if (numLegions == 4)
                    return 4;
                if (numLegions == 5 || numLegions == 6)
                    return 6;
                if (numLegions >= 7)
                    return numLegions;
                return 0;
        }

        return 0;
    }

    private LegionRebellionTable(){}
}
