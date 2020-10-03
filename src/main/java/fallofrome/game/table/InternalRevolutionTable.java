package fallofrome.game.table;

import fallofrome.game.Allegiance;
import fallofrome.game.ForcePlacement;
import fallofrome.game.GamePeriod;
import fallofrome.game.UnitType;
import fallofrome.game.board.Province;
import fallofrome.util.Util;

import java.util.ArrayList;
import java.util.List;

public class InternalRevolutionTable {

    public static boolean getProbability(GamePeriod gamePeriod){
        int die = Util.roll();
        switch (gamePeriod){
            case A:
                return false;
            case B:
                return die >= 4;
            case C:
                return false;
            case D:
                return false;
            case E:
                return false;
            case F:
                return die >= 2;
            case G:
                return false;
        }
        return false;
    }

    public static List<ForcePlacement> getResults(){
        List<ForcePlacement> forcePlacements = new ArrayList<>();
        int die = Util.roll();
        switch (die){
            case 1:
                forcePlacements.add(new ForcePlacement(Province.GALLIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3));
                break;
            case 2:
                forcePlacements.add(new ForcePlacement(Province.ILLYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4));
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
        return forcePlacements;
    }

    private InternalRevolutionTable(){}
}
