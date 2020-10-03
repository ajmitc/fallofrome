package fallofrome.game.scenario;

import fallofrome.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Scenario {
    public static final int GAME_IN_PROGRESS  = 0;
    public static final int GAME_OVER_VICTORY = 1;
    public static final int GAME_OVER_DEFEAT  = 2;

    protected GamePeriod gamePeriod;
    protected int maxTurns;
    // Province Name -> Allegiance
    protected Map<String, Allegiance> controlledProvinces = new HashMap<>();
    protected List<ForcePlacement> forcePlacements = new ArrayList<>();
    protected List<String> activeMilitias = new ArrayList<>();
    protected List<String> activatedMilitias = new ArrayList<>();
    protected int romanTreasury;
    protected int persianTreasury;

    public Scenario(){

    }

    public abstract int checkGameOver(Game game);

    public GamePeriod getGamePeriod() {
        return gamePeriod;
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public Map<String, Allegiance> getControlledProvinces() {
        return controlledProvinces;
    }

    public List<ForcePlacement> getForcePlacements() {
        return forcePlacements;
    }

    public int getPersianTreasury() {
        return persianTreasury;
    }

    public int getRomanTreasury() {
        return romanTreasury;
    }

    public List<String> getActiveMilitias() {
        return activeMilitias;
    }

    public List<String> getActivatedMilitias() {
        return activatedMilitias;
    }
}
