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
                return die == 6;
            case B:
                return die >= 4;
            case C:
                return die == 6;
            case D:
                return die >= 5;
            case E:
                return die >= 5;
            case F:
                return die >= 2;
            case G:
                return die == 6;
        }
        return false;
    }

    public static List<ForcePlacement> getResults(GamePeriod gamePeriod){
        List<ForcePlacement> forcePlacements = new ArrayList<>();
        int die = Util.roll();
        switch (die){
            case 1:
                forcePlacements.add(new ForcePlacement(Province.GALLIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                forcePlacements.add(new ForcePlacement(Province.DACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                if (gamePeriod == GamePeriod.G)
                    forcePlacements.add(new ForcePlacement(Province.DACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 12, true));
                forcePlacements.add(new ForcePlacement(Province.ASIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.ARMENIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.MESOPOTAMIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.SYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.AFRICA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 2, true));
                break;
            case 2:
                if (gamePeriod == GamePeriod.G) {
                    forcePlacements.add(new ForcePlacement(Province.HISPANIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 5, true));
                    forcePlacements.add(new ForcePlacement(Province.ILLYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 6, true));
                    forcePlacements.add(new ForcePlacement(Province.GRAECIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                }
                forcePlacements.add(new ForcePlacement(Province.SCYTHIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 20, true));
                forcePlacements.add(new ForcePlacement(Province.ASIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.PERSIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 15, true));
                forcePlacements.add(new ForcePlacement(Province.AFRICA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                break;
            case 3:
                forcePlacements.add(new ForcePlacement(Province.PICTUM, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 8, true));
                forcePlacements.add(new ForcePlacement(Province.BRITANNIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 2, true));
                forcePlacements.add(new ForcePlacement(Province.GALLIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                forcePlacements.add(new ForcePlacement(Province.PERSIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 15, true));
                break;
            case 4:
                forcePlacements.add(new ForcePlacement(Province.PICTUM, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 8, true));
                forcePlacements.add(new ForcePlacement(Province.BRITANNIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.HISPANIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.ILLYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.DACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                forcePlacements.add(new ForcePlacement(Province.PERSIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 15, true));
                forcePlacements.add(new ForcePlacement(Province.AEGYPTUS, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 2, true));
                forcePlacements.add(new ForcePlacement(Province.CYPRUS, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                break;
            case 5:
                forcePlacements.add(new ForcePlacement(Province.PICTUM, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 8, true));
                forcePlacements.add(new ForcePlacement(Province.BRITANNIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.HISPANIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                forcePlacements.add(new ForcePlacement(Province.ILLYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 2, true));
                forcePlacements.add(new ForcePlacement(Province.SCYTHIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 20, true));
                forcePlacements.add(new ForcePlacement(Province.THRACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                if (gamePeriod == GamePeriod.G) {
                    forcePlacements.add(new ForcePlacement(Province.ASIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 5, true));
                    forcePlacements.add(new ForcePlacement(Province.AEGYPTUS, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 10, true));
                }
                forcePlacements.add(new ForcePlacement(Province.PERSIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 15, true));
                forcePlacements.add(new ForcePlacement(Province.SICILIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                break;
            case 6:
                forcePlacements.add(new ForcePlacement(Province.BRITANNIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                if (gamePeriod == GamePeriod.G) {
                    forcePlacements.add(new ForcePlacement(Province.HISPANIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 5, true));
                    forcePlacements.add(new ForcePlacement(Province.THRACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 10, true));
                }
                forcePlacements.add(new ForcePlacement(Province.GALLIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 3, true));
                forcePlacements.add(new ForcePlacement(Province.ILLYRIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 2, true));
                forcePlacements.add(new ForcePlacement(Province.SCYTHIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 20, true));
                forcePlacements.add(new ForcePlacement(Province.DACIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 12, true));

                forcePlacements.add(new ForcePlacement(Province.ARMENIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.MESOPOTAMIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 8, true));
                forcePlacements.add(new ForcePlacement(Province.AEGYPTUS, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 4, true));
                forcePlacements.add(new ForcePlacement(Province.SICILIA, Allegiance.REBELLIOUS_ROMAN, UnitType.MILITIA, 1, true));
                break;
        }
        return forcePlacements;
    }

    private InternalRevolutionTable(){}
}
