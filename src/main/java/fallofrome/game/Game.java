package fallofrome.game;

import fallofrome.game.board.Area;
import fallofrome.game.board.Board;
import fallofrome.game.scenario.Scenario;

import java.util.HashMap;
import java.util.Map;

public class Game {
    public static final int MOVEMENT_ALLOWANCE = 5;

    private Phase phase;   // Current game phase
    private PhaseStep phaseStep;
    private GamePeriod gamePeriod;
    private int turn;
    private Board board;

    private int romanTreasury;
    private int persianTreasury;

    private Scenario scenario;

    private int nextIndependentStateNumber;

    // Turn Available -> Strength
    private Map<Integer, Integer> romanReplacements = new HashMap<>();
    private Map<Integer, Integer> persianReplacements = new HashMap<>();

    public Game(){
        phase = Phase.GAMEOVER;
        phaseStep = PhaseStep.START_PHASE;

        gamePeriod = GamePeriod.A;
        turn = 0;
        board = new Board();
        romanTreasury = 0;
        persianTreasury = 0;
        scenario = null;

        nextIndependentStateNumber = 1;
    }

    public void setup(Scenario scenario){
        this.scenario = scenario;

        // Assign control of provinces
        for (Map.Entry<String, Allegiance> entry: scenario.getControlledProvinces().entrySet()){
            board.getProvince(entry.getKey()).setOriginalController(entry.getValue());
            board.getProvince(entry.getKey()).setController(entry.getValue());
        }

        // Place forces in specific areas
        // The Controller will handle asking the user to place forces in provinces with Area == null
        for (ForcePlacement forcePlacement: scenario.getForcePlacements()){
            if (forcePlacement.getArea() != null) {
                Force force = new Force(forcePlacement.getAllegiance(), forcePlacement.getUnitType(), forcePlacement.getStrength());
                Area area = board.getArea(forcePlacement.getProvince(), forcePlacement.getArea().charAt(0));
                area.getForces().add(force);
            }
        }

        // Active Militias
        for (String province: scenario.getActiveMilitias()){
            board.getProvince(province).setMilitiaActive(true);
        }

        // Activated Militias
        for (String province: scenario.getActiveMilitias()){
            board.getProvince(province).setMilitiaActivated(true);
        }

        romanTreasury = scenario.getRomanTreasury();
        persianTreasury = scenario.getPersianTreasury();

        gamePeriod = scenario.getGamePeriod();

        setPhase(Phase.SETUP);
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
        this.phaseStep = PhaseStep.START_PHASE;
    }

    public void setPhase(Phase phase, PhaseStep step) {
        this.phase = phase;
        this.phaseStep = step;
    }

    public PhaseStep getPhaseStep() {
        return phaseStep;
    }

    public void setPhaseStep(PhaseStep phaseStep) {
        this.phaseStep = phaseStep;
    }

    public boolean isPhase(Phase phase){
        return this.phase == phase;
    }

    public boolean isPhase(Phase phase, PhaseStep step){
        return this.phase == phase && this.phaseStep == step;
    }

    public GamePeriod getGamePeriod() {
        return gamePeriod;
    }

    public void setGamePeriod(GamePeriod gamePeriod) {
        this.gamePeriod = gamePeriod;
    }

    public int getTurn() {
        return turn;
    }

    public void endTurn(){
        turn += 1;
    }

    public Board getBoard() {
        return board;
    }

    public int getRomanTreasury() {
        return romanTreasury;
    }

    public void setRomanTreasury(int romanTreasury) {
        this.romanTreasury = romanTreasury;
    }

    public void adjRomanTreasury(int amount){
        this.romanTreasury += amount;
    }

    public int getPersianTreasury() {
        return persianTreasury;
    }

    public void setPersianTreasury(int persianTreasury) {
        this.persianTreasury = persianTreasury;
    }

    public void adjPersianTreasury(int amount){
        this.persianTreasury += amount;
    }

    public int getNextIndependentStateNumber() {
        return nextIndependentStateNumber;
    }

    public void incNextIndependentStateNumber(){
        ++nextIndependentStateNumber;
    }

    public Map<Integer, Integer> getRomanReplacements() {
        return romanReplacements;
    }

    public void addRomanReplacements(int strength) {
        int availableTurn = turn + 2;
        if (!romanReplacements.containsKey(availableTurn))
            romanReplacements.put(availableTurn, 0);
        romanReplacements.put(availableTurn, romanReplacements.get(availableTurn) + strength);
    }

    public Map<Integer, Integer> getPersianReplacements() {
        return persianReplacements;
    }

    public void addPersianReplacements(int strength) {
        int availableTurn = turn + 5;
        if (!persianReplacements.containsKey(availableTurn))
            persianReplacements.put(availableTurn, 0);
        persianReplacements.put(availableTurn, persianReplacements.get(availableTurn) + strength);
    }
}
