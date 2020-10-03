package fallofrome.game.board;

import fallofrome.game.Allegiance;
import fallofrome.game.Force;
import fallofrome.game.UnitType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Province {
    public static final String AEGYPTUS    = "Aegyptus";
    public static final String AFRICA      = "Africa";
    public static final String ARMENIA     = "Armenia";
    public static final String ASIA        = "Asia";
    public static final String BRITANNIA   = "Britannia";
    public static final String CYPRUS      = "Cyprus";
    public static final String DACIA       = "Dacia";
    public static final String GALLIA      = "Gallia";
    public static final String GERMANIA    = "Germania";
    public static final String GRAECIA     = "Graecia";
    public static final String HISPANIA    = "Hispaniola";
    public static final String ILLYRIA     = "Illyria";
    public static final String ITALIA      = "Italia";
    public static final String MESOPOTAMIA = "Mesopotamia";
    public static final String PERSIA      = "Persia";
    public static final String PICTUM      = "Pictum";
    public static final String SCYTHIA     = "Scythia";
    public static final String SICILIA     = "Sicilia";
    public static final String SYRIA       = "Syria";
    public static final String TAURICA     = "Taurica";
    public static final String THRACIA     = "Thracia";

    public static final List<String> PROVINCE_NAMES = new ArrayList<>();
    static {
        PROVINCE_NAMES.add(AEGYPTUS);
        PROVINCE_NAMES.add(AFRICA);
        PROVINCE_NAMES.add(ARMENIA);
        PROVINCE_NAMES.add(ASIA);
        PROVINCE_NAMES.add(BRITANNIA);
        PROVINCE_NAMES.add(CYPRUS);
        PROVINCE_NAMES.add(DACIA);
        PROVINCE_NAMES.add(GALLIA);
        PROVINCE_NAMES.add(GERMANIA);
        PROVINCE_NAMES.add(GRAECIA);
        PROVINCE_NAMES.add(HISPANIA);
        PROVINCE_NAMES.add(ILLYRIA);
        PROVINCE_NAMES.add(ITALIA);
        PROVINCE_NAMES.add(MESOPOTAMIA);
        PROVINCE_NAMES.add(PERSIA);
        PROVINCE_NAMES.add(PICTUM);
        PROVINCE_NAMES.add(SCYTHIA);
        PROVINCE_NAMES.add(SICILIA);
        PROVINCE_NAMES.add(SYRIA);
        PROVINCE_NAMES.add(TAURICA);
        PROVINCE_NAMES.add(THRACIA);
    }

    private String name;
    private String abbreviation;
    private List<Area> areas = new ArrayList<>();
    private int victoryPointAllowance;
    private int militiaStrengthPointAllowance;
    // Is the milita active for this province (ie. should it be placed if activated?)
    private boolean militiaActive;
    // Has the militia been activated (ie. placed on board)?
    private boolean militiaActivated;
    // Has the militia rebelled?
    private boolean militiaRebelling;

    private Allegiance originalController;
    private Allegiance controller;
    private int independentStateNumber; // used if controller == INDEPENDENT_STATE

    public Province(String name, String abbreviation, int vp, int militia, char maxAreaId){
        this.name = name;
        this.abbreviation = abbreviation;
        this.victoryPointAllowance = vp;
        this.militiaStrengthPointAllowance = militia;
        char areaId = 'A';
        while (areaId != maxAreaId){
            areas.add(new Area(this, areaId));
            areaId = (char) (areaId + 1);
        }
        areas.add(new Area(this, maxAreaId));
        militiaActive = false;
        militiaActivated = false;
        militiaRebelling = false;

        originalController = null;
        controller = null;
        independentStateNumber = 0;
    }

    public boolean hasOpposingForce(Allegiance allegiance){
        return areas.stream().anyMatch(a -> a.hasOpposingForce(allegiance));
    }

    public boolean hasForce(Allegiance allegiance){
        return areas.stream().anyMatch(a -> a.hasForce(allegiance));
    }

    public int getTotalForceStrength(Allegiance allegiance){
        return areas.stream().mapToInt(a -> a.getTotalForceStrength(allegiance)).sum();
    }

    public int getTotalForceStrength(Allegiance allegiance, UnitType unitType){
        return areas.stream().mapToInt(a -> a.getForce(allegiance, unitType) != null? a.getForce(allegiance, unitType).getStrength(): 0).sum();
    }

    public Force getLargestOpposingForce(Allegiance allegiance){
        return getLargestOpposingForce(allegiance, null);
    }

    public Force getLargestOpposingForce(Allegiance allegiance, Allegiance ignoreAllegiance){
        Optional<Force> of =
                areas.stream()
                        .map(a -> a.getLargestOpposingForce(allegiance, ignoreAllegiance))
                        .sorted(new Comparator<Force>() {
                            @Override
                            public int compare(Force o1, Force o2) {
                                return o1.getStrength() > o2.getStrength()? -1: o1.getStrength() < o2.getStrength()? 1: 0;
                            }
                        })
                        .findFirst();
        return of.isPresent()? of.get(): null;
    }

    public Force getSmallestForce(Allegiance allegiance){
        Optional<Force> of =
                areas.stream()
                        .map(a -> a.getSmallestForce(allegiance))
                        .sorted(new Comparator<Force>() {
                            @Override
                            public int compare(Force o1, Force o2) {
                                return o1.getStrength() < o2.getStrength()? -1: o1.getStrength() > o2.getStrength()? 1: 0;
                            }
                        })
                        .findFirst();
        return of.isPresent()? of.get(): null;
    }

    public List<Force> getForces(Allegiance allegiance){
        List<Force> forces = new ArrayList<>();
        areas.stream().forEach(a -> {
            List<Force> f = a.getForces(allegiance);
            if (f != null && !f.isEmpty())
                forces.addAll(f);
        });
        return forces;
    }

    public List<Force> getForces(Allegiance allegiance, UnitType unitType){
        List<Force> forces = new ArrayList<>();
        areas.stream().forEach(a -> {
            Force force = a.getForce(allegiance, unitType);
            if (force != null)
                forces.add(force);
        });
        return forces;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public Area getArea(char id){
        return areas.stream().filter(a -> a.getId() == id).findFirst().get();
    }

    public Allegiance getController() {
        return controller;
    }

    public void setController(Allegiance controller) {
        this.controller = controller;
    }

    public Allegiance getOriginalController() {
        return originalController;
    }

    public void setOriginalController(Allegiance originalController) {
        this.originalController = originalController;
    }

    public int getVictoryPointAllowance() {
        return victoryPointAllowance;
    }

    public int getMilitiaStrengthPointAllowance() {
        return militiaStrengthPointAllowance;
    }

    public boolean isMilitiaActive() {
        return militiaActive;
    }

    public void setMilitiaActive(boolean militiaActive) {
        this.militiaActive = militiaActive;
    }

    public boolean isMilitiaActivated() {
        return militiaActivated;
    }

    public void setMilitiaActivated(boolean militiaActivated) {
        this.militiaActivated = militiaActivated;
    }

    public boolean isMilitiaRebelling() {
        return militiaRebelling;
    }

    public void setMilitiaRebelling(boolean militiaRebelling) {
        this.militiaRebelling = militiaRebelling;
    }

    public int getIndependentStateNumber() {
        return independentStateNumber;
    }

    public void setIndependentStateNumber(int independentStateNumber) {
        this.independentStateNumber = independentStateNumber;
    }
}
