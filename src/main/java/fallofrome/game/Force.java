package fallofrome.game;

import fallofrome.game.board.Area;
import fallofrome.game.board.AreaConnection;

import java.util.List;

public class Force {
    private Allegiance allegiance;
    private UnitType unitType;
    private int strength;

    private int movementPoints;

    // Used by Barbarians
    private Area destinationArea;
    private List<AreaConnection> pathToDestination;

    public Force(Allegiance allegiance, UnitType unitType, int strength){
        this.allegiance = allegiance;
        this.unitType = unitType;
        this.strength = strength;

        movementPoints = 0;
        destinationArea = null;
        pathToDestination = null;
    }

    public Allegiance getAllegiance() {
        return allegiance;
    }

    public void setAllegiance(Allegiance allegiance) {
        this.allegiance = allegiance;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }

    public Area getDestinationArea() {
        return destinationArea;
    }

    public void setDestinationArea(Area destinationArea) {
        this.destinationArea = destinationArea;
    }

    public List<AreaConnection> getPathToDestination() {
        return pathToDestination;
    }

    public void setPathToDestination(List<AreaConnection> pathToDestination) {
        this.pathToDestination = pathToDestination;
    }

    public String toString(){
        return allegiance + " " + unitType + " (" + strength + ")";
    }
}
