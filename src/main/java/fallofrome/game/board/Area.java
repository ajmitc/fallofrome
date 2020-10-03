package fallofrome.game.board;

import fallofrome.game.Allegiance;
import fallofrome.game.Force;
import fallofrome.game.UnitType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Area {
    private Province province;
    private char id;
    private List<Force> forces = new ArrayList<>();

    public Area(Province province, char id){
        this.province = province;
        this.id = id;
    }

    public Province getProvince() {
        return province;
    }

    public char getId() {
        return id;
    }

    public List<Force> getForces() {
        return forces;
    }

    public List<Force> getForces(Allegiance allegiance){
        return forces.stream().filter(f -> f.getAllegiance() == allegiance).collect(Collectors.toList());
    }

    public Force getForce(Allegiance allegiance, UnitType unitType){
        Optional<Force> of = forces.stream().filter(f -> f.getAllegiance() == allegiance && f.getUnitType() == unitType).findFirst();
        return of.isPresent()? of.get(): null;
    }

    public Force getLargestOpposingForce(Allegiance allegiance){
        return getLargestOpposingForce(allegiance, null);
    }

    public Force getLargestOpposingForce(Allegiance allegiance, Allegiance ignoreAllegiance){
        Optional<Force> of =
                forces.stream()
                        .filter(f -> f.getAllegiance() != allegiance && (ignoreAllegiance == null || ignoreAllegiance != f.getAllegiance()))
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
                forces.stream()
                        .filter(f -> f.getAllegiance() == allegiance)
                        .sorted(new Comparator<Force>() {
                            @Override
                            public int compare(Force o1, Force o2) {
                                return o1.getStrength() < o2.getStrength()? -1: o1.getStrength() > o2.getStrength()? 1: 0;
                            }
                        })
                        .findFirst();
        return of.isPresent()? of.get(): null;
    }

    public void addForce(Force force){
        // Consolidate force, if matching force already exists
        for (Force existingForce: forces){
            if (existingForce.getAllegiance() == force.getAllegiance() &&
                    existingForce.getUnitType() == force.getUnitType()){
                existingForce.setStrength(existingForce.getStrength() + force.getStrength());
                return;
            }
        }
        forces.add(force);
    }

    public boolean hasForce(Allegiance allegiance){
        return forces.stream().anyMatch(f -> f.getAllegiance() == allegiance);
    }

    public boolean hasForce(Allegiance allegiance, UnitType unitType, int minStrength){
        return forces.stream().anyMatch(f -> f.getAllegiance() == allegiance && f.getUnitType() == unitType && f.getStrength() >= minStrength);
    }

    public boolean hasOpposingForce(Allegiance allegiance){
        return forces.stream().anyMatch(f -> f.getAllegiance() != allegiance);
    }

    public int getTotalForceStrength(Allegiance allegiance){
        return forces.stream().filter(f -> f.getAllegiance() == allegiance).mapToInt(f -> f.getStrength()).sum();
    }

    public String toString(){
        return province.getAbbreviation() + "-" + id;
    }
}
