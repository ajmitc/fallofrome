package fallofrome.game.table;

import fallofrome.game.Allegiance;
import fallofrome.game.ForcePlacement;
import fallofrome.game.GamePeriod;
import fallofrome.game.UnitType;
import fallofrome.game.board.Province;
import fallofrome.util.Util;

public class BarbarianCreationTable {
    public static int getBarbarianCreationRolls(GamePeriod gamePeriod, int turn){
        switch (gamePeriod){
            case A:
                return turn % 2 == 0? 1: 0;
            case C:
            case G:
                return 1;
            case B:
            case D:
                return 2;
            case E:
                return 3;
            case F:
                return 4;
        }
        return 1;
    }

    public static ForcePlacement getBarbarianCreation(){
        int die1 = Util.roll();
        int die2 = Util.roll();

        switch (die1){
            case 1:
                switch (die2){
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.REGULAR, 10);
                    case 4:
                        return new ForcePlacement(Province.SCYTHIA, Allegiance.SCYTHIAN_BARBARIAN, UnitType.REGULAR, 15);
                    case 5:
                        return new ForcePlacement(Province.TAURICA, Allegiance.TAURICAN_BARBARIAN, UnitType.RAIDING_PARTY, 2);
                    case 1:
                    case 3:
                    case 6:
                        return null;
                }
            case 2:
                switch (die2){
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.REGULAR, 15);
                    case 4:
                        return new ForcePlacement(Province.SCYTHIA, Allegiance.SCYTHIAN_BARBARIAN, UnitType.REGULAR, 20);
                    case 1:
                    case 3:
                    case 5:
                    case 6:
                        return null;
                }
            case 3:
                switch (die2){
                    case 1:
                        return new ForcePlacement(Province.PICTUM, Allegiance.PICTISH_BARBARIAN, UnitType.REGULAR, 3);
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.REGULAR, 20);
                    case 3:
                        return new ForcePlacement(Province.DACIA, Allegiance.DACIAN_BARBARIAN, UnitType.REGULAR, 6);
                    case 4:
                    case 5:
                    case 6:
                        return null;
                }
            case 4:
                switch (die2){
                    case 1:
                        return new ForcePlacement(Province.PICTUM, Allegiance.PICTISH_BARBARIAN, UnitType.RAIDING_PARTY, 1);
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.RAIDING_PARTY, 4);
                    case 3:
                        return new ForcePlacement(Province.DACIA, Allegiance.DACIAN_BARBARIAN, UnitType.REGULAR, 6);
                    case 4:
                        return new ForcePlacement(Province.SCYTHIA, Allegiance.SCYTHIAN_BARBARIAN, UnitType.RAIDING_PARTY, 5);
                    case 5:
                    case 6:
                        return null;
                }
            case 5:
                switch (die2){
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.REGULAR, 25);
                    case 3:
                        return new ForcePlacement(Province.DACIA, Allegiance.DACIAN_BARBARIAN, UnitType.RAIDING_PARTY, 4);
                    case 4:
                        return new ForcePlacement(Province.SCYTHIA, Allegiance.SCYTHIAN_BARBARIAN, UnitType.RAIDING_PARTY, 4);
                    case 1:
                    case 5:
                    case 6:
                        return null;
                }
            case 6:
                switch (die2){
                    case 1:
                        return new ForcePlacement(Province.PICTUM, Allegiance.PICTISH_BARBARIAN, UnitType.RAIDING_PARTY, 2);
                    case 2:
                        return new ForcePlacement(Province.GERMANIA, Allegiance.GERMAN_BARBARIAN, UnitType.RAIDING_PARTY, 6);
                    case 4:
                        return new ForcePlacement(Province.SCYTHIA, Allegiance.SCYTHIAN_BARBARIAN, UnitType.REGULAR, 10);
                    case 3:
                    case 5:
                    case 6:
                        return null;
                }
            default:
                break;
        }
        return null;
    }
}
