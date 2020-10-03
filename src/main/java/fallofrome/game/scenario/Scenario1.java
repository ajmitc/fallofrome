package fallofrome.game.scenario;

import fallofrome.game.Allegiance;
import fallofrome.game.ForcePlacement;
import fallofrome.game.Game;
import fallofrome.game.GamePeriod;
import fallofrome.game.UnitType;
import fallofrome.game.board.Province;

/**
 * TODO Capture active/inactive militia
 *
 * Roman Controlled Provinces:
 *   Gallia (6L),
 *   Hispania (1L),
 *   Italia (3L),
 *   Sicilia,
 *   Illyria (3L),
 *   Thracia (3L),
 *   Graecia,
 *   Cyprus,
 *   Asia,
 *   Syria (6L),
 *   Aegyptus (2L),
 *   Africa (1L).
 *
 *   No active militia.
 *   Additional four legions in Britannia A (not controlled).
 *
 * 2) Persian Controlled Provinces: Persia, Mesopotamia, Armenia.  All militia active.  Fifteen Regular (B) Strength Points in Mesopotamia B.
 *
 * 3) Active Militia: Scythia, Dacia, Germania, Pictum, Britannia, Taurica.
 *
 * 4) Britannia: four militia (N) Strength Points in A.
 *
 * 5) Treasuries: Roman: 50 Tax Credits.  Persian: 12 Tax Credits.
 *
 * 6) Game Length: fifteen Game-Turns, Period A.
 *
 * 7) Victory: the Player must have the Romans control Provinces worth 78 Victory Points at the end of the game.
 *
 * 8) Special Rules: In Place of the usual procedures of the Legion Rebellion Phase, the Player rolls the die each
 *    Rebellion Phase.  The first time a one is rolled, all Legion Strength Points exceeding three in a single Area
 *    automatically rebel.  Thereafter, the normal Rebellion rules are reinstated.
 */
public class Scenario1 extends Scenario{
    public Scenario1(){
        super();
        controlledProvinces.put(Province.GALLIA,   Allegiance.ROMAN);
        controlledProvinces.put(Province.HISPANIA, Allegiance.ROMAN);
        controlledProvinces.put(Province.ITALIA,   Allegiance.ROMAN);
        controlledProvinces.put(Province.SICILIA,  Allegiance.ROMAN);
        controlledProvinces.put(Province.ILLYRIA,  Allegiance.ROMAN);
        controlledProvinces.put(Province.THRACIA,  Allegiance.ROMAN);
        controlledProvinces.put(Province.GRAECIA,  Allegiance.ROMAN);
        controlledProvinces.put(Province.CYPRUS,   Allegiance.ROMAN);
        controlledProvinces.put(Province.ASIA,     Allegiance.ROMAN);
        controlledProvinces.put(Province.SYRIA,    Allegiance.ROMAN);
        controlledProvinces.put(Province.AEGYPTUS, Allegiance.ROMAN);
        controlledProvinces.put(Province.AFRICA,   Allegiance.ROMAN);

        controlledProvinces.put(Province.PERSIA,      Allegiance.PERSIAN);
        controlledProvinces.put(Province.MESOPOTAMIA, Allegiance.PERSIAN);
        controlledProvinces.put(Province.ARMENIA,     Allegiance.PERSIAN);

        // Roman Forces
        forcePlacements.add(new ForcePlacement(Province.GALLIA,   Allegiance.ROMAN, UnitType.REGULAR, 6));
        forcePlacements.add(new ForcePlacement(Province.HISPANIA, Allegiance.ROMAN, UnitType.REGULAR, 1));
        forcePlacements.add(new ForcePlacement(Province.ITALIA,   Allegiance.ROMAN, UnitType.REGULAR, 3));
        forcePlacements.add(new ForcePlacement(Province.ILLYRIA,  Allegiance.ROMAN, UnitType.REGULAR, 3));
        forcePlacements.add(new ForcePlacement(Province.THRACIA,  Allegiance.ROMAN, UnitType.REGULAR, 3));
        forcePlacements.add(new ForcePlacement(Province.SYRIA,    Allegiance.ROMAN, UnitType.REGULAR, 6));
        forcePlacements.add(new ForcePlacement(Province.AEGYPTUS, Allegiance.ROMAN, UnitType.REGULAR, 2));
        forcePlacements.add(new ForcePlacement(Province.AFRICA,   Allegiance.ROMAN, UnitType.REGULAR, 1));

        forcePlacements.add(new ForcePlacement(Province.BRITANNIA, "A", Allegiance.ROMAN, UnitType.REGULAR, 4));

        // Persian Forces
        forcePlacements.add(new ForcePlacement(Province.MESOPOTAMIA, "B", Allegiance.PERSIAN, UnitType.REGULAR, 15));

        // Other Forces
        forcePlacements.add(new ForcePlacement(Province.BRITANNIA, "A", Allegiance.PICTISH_BARBARIAN, UnitType.MILITIA, 4));

        // Active Militia
        activeMilitias.add(Province.PERSIA);
        activeMilitias.add(Province.MESOPOTAMIA);
        activeMilitias.add(Province.ARMENIA);
        activeMilitias.add(Province.SCYTHIA);
        activeMilitias.add(Province.DACIA);
        activeMilitias.add(Province.GERMANIA);
        activeMilitias.add(Province.PICTUM);
        activeMilitias.add(Province.BRITANNIA);
        activeMilitias.add(Province.TAURICA);

        // Activated Militia
        activatedMilitias.add(Province.BRITANNIA);

        romanTreasury = 50;
        persianTreasury = 12;

        gamePeriod = GamePeriod.A;
        maxTurns = 15;
    }

    /**
     * TODO Victory: the Player must have the Romans control Provinces worth 78 Victory Points at the end of the game.
     */
    public int checkGameOver(Game game){
        return GAME_IN_PROGRESS;
    }
}
